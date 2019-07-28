package com.newsapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.newsapp.NewsApplication;
import com.newsapp.constants.AppConstant;
import com.newsapp.constants.Category;
import com.newsapp.constants.Country;
import com.newsapp.db.NewsDatabase;
import com.newsapp.db.entities.Article;
import com.newsapp.network.ApiService;
import com.newsapp.network.Resource;
import com.newsapp.utils.Utility;
import com.newsapp.utils.VLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ArticleRepository extends BaseRepository {

    private MutableLiveData<Resource<List<Article>>> topStoriesLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Article>>> businessLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Article>>> entertainmentLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Article>>> generalLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Article>>> healthLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Article>>> scienceLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Article>>> sportsLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Article>>> technologyLiveData = new MutableLiveData<>();

    HashMap<Category, MutableLiveData<Resource<List<Article>>>> liveDataMap = new HashMap<>();

    ApiService apiService;
    NewsDatabase db;

    @Inject
    public ArticleRepository(ApiService apiService,NewsDatabase newsDatabase) {
        this.apiService = apiService;
        db = newsDatabase;
        liveDataMap.put(Category.UNKNOWN, topStoriesLiveData);
        liveDataMap.put(Category.BUSINESS, businessLiveData);
        liveDataMap.put(Category.ENTERTAINMENT, entertainmentLiveData);
        liveDataMap.put(Category.GENERAL, generalLiveData);
        liveDataMap.put(Category.HEALTH, healthLiveData);
        liveDataMap.put(Category.SCIENCE, scienceLiveData);
        liveDataMap.put(Category.SPORTS, sportsLiveData);
        liveDataMap.put(Category.TECHNOLOGY, technologyLiveData);
    }

    public MutableLiveData<Resource<List<Article>>> getLiveData(Category category) {
        return liveDataMap.get(category);
    }

    public void loadArticles(Category category, Resource.DataLoadStrategy loadStrategy, Country country) {
        boolean isOnline = Utility.isNetworkAvailable(NewsApplication.getApp());
        int pageNumber = getPageNumber(category,loadStrategy);
        if (isOnline) {
            loadOnlineArticle(pageNumber, AppConstant.PAGE_SIZE, category, loadStrategy,country);
        }else {

            loadOfflineArticle(pageNumber, AppConstant.PAGE_SIZE, category, loadStrategy,country);
        }
    }

    private void loadOfflineArticle(int pageNumber, int pageSize, Category category, Resource.DataLoadStrategy loadStrategy,Country country) {
        add(
                db.getNewsDao().loadCategoryArticle(pageNumber,pageSize,category,country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articles -> {
                    handleResponse(category,loadStrategy, Resource.Source.DB,articles,country);
                },throwable -> {
                    handleError(category,loadStrategy, Resource.Source.DB ,throwable);
                })
        );
    }

    private void handleResponse(Category category, Resource.DataLoadStrategy loadStrategy, Resource.Source source,List<Article> articles,Country country) {
        if (loadStrategy == Resource.DataLoadStrategy.FRESH || loadStrategy == Resource.DataLoadStrategy.REFRESH) {
            liveDataMap.get(category).setValue(Resource.success(articles,source,loadStrategy));
        }else {
            Resource<List<Article>> oldResource = liveDataMap.get(category).getValue();
            List<Article> newArticles;
            if (oldResource == null || oldResource.data == null) {
                newArticles = new ArrayList<>();
            }else {
                newArticles = oldResource.data;
            }
            newArticles.addAll(articles);
            liveDataMap.get(category).setValue(Resource.success(newArticles, source, loadStrategy));
        }
        Resource<List<Article>> updatedResource = liveDataMap.get(category).getValue();
        if (source == Resource.Source.DB) {
            updatedResource.isAllLoadedFromDB = articles.size() < AppConstant.PAGE_SIZE;

        } else {
            updatedResource.isAllLoadedFromNetwork = articles.size() < AppConstant.PAGE_SIZE;
        }
        saveArticles(articles,category,country);
    }

    private void saveArticles(List<Article> articles, Category category,Country country) {
        add(
                db.getNewsDao().insertCategoryArticles(articles,category,country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(longs -> {

                    VLog.d("Articles Saved!", " Count: " + longs.length);
                },throwable -> {
                    VLog.d("Error Saving Articles!",throwable.getMessage());
                })
        );
    }

    private void handleError(Category category, Resource.DataLoadStrategy loadStrategy, Resource.Source source, Throwable throwable) {
        Resource<List<Article>> oldResource = liveDataMap.get(category).getValue();
        if (oldResource==null){
            liveDataMap.get(category).setValue(Resource.error(throwable, null, source, loadStrategy));
        }else {
            oldResource.status = Resource.Status.ERROR;
            oldResource.error = throwable;
            liveDataMap.get(category).setValue(oldResource);
        }
    }

    private void loadOnlineArticle(int pageNumber, int pageSize, Category category, Resource.DataLoadStrategy loadStrategy,Country country) {
//        switch (category) {
//            case UNKNOWN:
//                loadTopHeadLineFromApi(pageNumber,pageSize,category,loadStrategy);
//                break;
//            case BUSINESS:
//                loadBusiness(pageNumber,pageSize);
//                break;
//            case ENTERTAINMENT:
//                loadEntertainment(pageNumber, pageSize);
//                break;
//            case GENERAL:
//                loadGeneral(pageNumber, pageSize);
//                break;
//            case HEALTH:
//                loadHealth(pageNumber, pageSize);
//                break;
//            case SCIENCE:
//                loadScience(pageNumber, pageSize);
//                break;
//            case SPORTS:
//                loadSport(pageNumber, pageSize);
//                break;
//            case TECHNOLOGY:
//                loadTechnology(pageNumber, pageSize);
//                break;
//        }
        if (category == Category.UNKNOWN) {
            loadTopHeadLineFromApi(pageNumber,pageSize,category,loadStrategy,country);
        }else {
            loadCategoryHeadLineFromApi(pageNumber,pageSize,category,loadStrategy,country);
        }
    }

    private void loadTopHeadLineFromApi(int pageNumber, int pageSize, Category category, Resource.DataLoadStrategy loadStrategy,Country country) {
        add(
                apiService.loadArticle(pageNumber,pageSize,country.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(headlineResponse -> {
                    handleResponse(category,loadStrategy, Resource.Source.NETWORK,headlineResponse.getArticles(),country);
                },throwable -> {
                    handleError(category,loadStrategy, Resource.Source.NETWORK,throwable);
                })
        );
    }

    private void loadCategoryHeadLineFromApi(int pageNumber, int pageSize, Category category, Resource.DataLoadStrategy loadStrategy,Country country) {
        add(
                apiService.loadCategoryArticle(pageNumber,pageSize,category.toString(),country.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(headlineResponse -> {
                            handleResponse(category,loadStrategy, Resource.Source.NETWORK,headlineResponse.getArticles(),country);
                        },throwable -> {
                            handleError(category,loadStrategy, Resource.Source.NETWORK,throwable);
                        })
        );
    }

    private int getPageNumber(Category category, Resource.DataLoadStrategy loadStrategy) {
        try{
            if (loadStrategy == Resource.DataLoadStrategy.FRESH || loadStrategy == Resource.DataLoadStrategy.REFRESH) {
                return 1;
            }else {
                return liveDataMap.get(category).getValue().data.size() / AppConstant.PAGE_SIZE + 1;
            }
        }catch (NullPointerException e){
            return 1;
        }
    }
}
