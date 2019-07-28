package com.newsapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.newsapp.db.entities.Article;
import com.newsapp.network.Resource;
import com.newsapp.repositories.SearchArticleRepository;
import com.newsapp.utils.VLog;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SearchArticleViewModel extends AndroidViewModel {

    SearchArticleRepository mRepository;
    private MediatorLiveData<Resource<List<Article>>> articleLiveData = new MediatorLiveData<>();
    private CompositeDisposable mTimerDisposable = new CompositeDisposable();

    @Inject
    public SearchArticleViewModel(@NonNull Application application,SearchArticleRepository repository) {
        super(application);
        mRepository = repository;
        observeLiveData();
    }

    private void observeLiveData() {
        articleLiveData.addSource(mRepository.getArticleLiveData(), listResource -> {
            articleLiveData.setValue(listResource);
        });
    }

    public void searchArticles(String query, Resource.DataLoadStrategy loadStrategy){
        if (loadStrategy == Resource.DataLoadStrategy.FRESH) {
            startTimer(query);
        }else {

            onLoading(loadStrategy);
            mRepository.loadArticles(query,loadStrategy);
        }
    }

    private void startTimer(String query) {
        mTimerDisposable.clear();
        mTimerDisposable.add(
                Observable.timer(2, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            onLoading(Resource.DataLoadStrategy.FRESH);
                            mRepository.loadArticles(query, Resource.DataLoadStrategy.FRESH);
                        }, throwable -> {
                            VLog.i("Timer", "Stopped");
                        })
        );
    }

    private void onLoading(Resource.DataLoadStrategy loadStrategy) {
        if (loadStrategy == Resource.DataLoadStrategy.FRESH ) {
            articleLiveData.setValue(Resource.loading(null, Resource.Source.NETWORK,loadStrategy));
        }else {
            Resource<List<Article>> resource = articleLiveData.getValue();
            resource.status = Resource.Status.LOADING;
            resource.loadStrategy = loadStrategy;
            articleLiveData.setValue(resource);
        }
    }

    public LiveData<Resource<List<Article>>> getArticleLiveData() {
        return articleLiveData;
    }

    public boolean hasMoreItems() {
        Resource<List<Article>> articles = articleLiveData.getValue();
        return articles == null || !articles.isAllLoadedFromNetwork;
    }

    public boolean isLoading() {
        Resource<List<Article>> articles = articleLiveData.getValue();
        return articles != null && articles.status == Resource.Status.LOADING;
    }

    @Override
    protected void onCleared() {
        if (mRepository != null) {
            mRepository.dispose();
        }
        mTimerDisposable.clear();
        super.onCleared();
    }
}
