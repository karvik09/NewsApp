package com.newsapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.newsapp.constants.AppConstant;
import com.newsapp.db.entities.Article;
import com.newsapp.network.ApiService;
import com.newsapp.network.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchArticleRepository extends BaseRepository {

    private MutableLiveData<Resource<List<Article>>> articleLiveData = new MutableLiveData<>();
    ApiService apiService;

    @Inject
    SearchArticleRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void add(Disposable disposable) {
        super.add(disposable);
    }

    public void loadArticles(String query, Resource.DataLoadStrategy loadStrategy) {
        int pageNumber = getPageNumber(loadStrategy);
        dispose();
        add(
                apiService.serchArticles(query, pageNumber, AppConstant.PAGE_SIZE)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(headlineResponse -> {
                    handleResponse(loadStrategy,headlineResponse.getArticles());
                },throwable -> {
                    handleError(loadStrategy,throwable);
                })

        );
    }

    private void handleResponse(Resource.DataLoadStrategy loadStrategy,List<Article> articles) {
        if (loadStrategy == Resource.DataLoadStrategy.FRESH || loadStrategy == Resource.DataLoadStrategy.REFRESH) {
            articleLiveData.setValue(Resource.success(articles, null, loadStrategy));
        } else {
            Resource<List<Article>> oldResource = articleLiveData.getValue();
            List<Article> newArticles;
            if (oldResource == null || oldResource.data == null) {
                newArticles = new ArrayList<>();
            } else {
                newArticles = oldResource.data;
            }
            newArticles.addAll(articles);
            articleLiveData.setValue(Resource.success(newArticles, null, loadStrategy));
        }
        Resource<List<Article>> updatedResource = articleLiveData.getValue();
        updatedResource.isAllLoadedFromNetwork = articles.size() < AppConstant.PAGE_SIZE;
    }
    private void handleError(Resource.DataLoadStrategy loadStrategy,Throwable throwable) {
        Resource<List<Article>> oldResource = articleLiveData.getValue();
        if (oldResource==null){
            articleLiveData.setValue(Resource.error(throwable, null, null, loadStrategy));
        }else {
            oldResource.status = Resource.Status.ERROR;
            oldResource.error = throwable;
            articleLiveData.setValue(oldResource);
        }
    }

    public LiveData<Resource<List<Article>>> getArticleLiveData() {
        return articleLiveData;
    }

    private int getPageNumber(Resource.DataLoadStrategy loadStrategy) {
        try{
            if (loadStrategy == Resource.DataLoadStrategy.REFRESH || loadStrategy == Resource.DataLoadStrategy.FRESH) {
                return 1;
            }else {
                return articleLiveData.getValue().data.size() / AppConstant.PAGE_SIZE + 1;
            }
        }catch (NullPointerException e){
            return 1;
        }
    }
}
