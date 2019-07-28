package com.newsapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.newsapp.NewsApplication;
import com.newsapp.constants.Category;
import com.newsapp.constants.Country;
import com.newsapp.db.entities.Article;
import com.newsapp.network.Resource;
import com.newsapp.repositories.ArticleRepository;
import com.newsapp.utils.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ArticleViewModel extends AndroidViewModel {

    private ArticleRepository mRepository;
    HashMap<Category, MediatorLiveData<Resource<List<Article>>>> liveDataHashMap = new HashMap<>();
    private MutableLiveData<Country> countryLiveData = new MutableLiveData<>();


    @Inject
    public ArticleViewModel(@NonNull Application application,ArticleRepository articleRepository) {
        super(application);
        this.mRepository = articleRepository;
        countryLiveData.setValue(Country.INDIA);
        createAllLiveData();
        ObserverAll();
    }

    private void ObserverAll() {
        for (Map.Entry<Category, MediatorLiveData<Resource<List<Article>>>> e : liveDataHashMap.entrySet()) {
            e.getValue().addSource(mRepository.getLiveData(e.getKey()), articleResource -> {
                e.getValue().setValue(articleResource);
            });
        }
    }

    public MutableLiveData<Country> getCountryLiveData() {
        return countryLiveData;
    }

    public void loadArticles(Category category, Resource.DataLoadStrategy loadStrategy){
        onLoading(category,loadStrategy);
        mRepository.loadArticles(category,loadStrategy,countryLiveData.getValue());
    }

    private void onLoading(Category category, Resource.DataLoadStrategy loadStrategy) {
        Resource<List<Article>> oldResource = liveDataHashMap.get(category).getValue();

        if (oldResource == null || loadStrategy== Resource.DataLoadStrategy.FRESH) {
            liveDataHashMap.get(category).setValue(Resource.loading(null, null, loadStrategy));
        }else {
            oldResource.status = Resource.Status.LOADING;
            oldResource.loadStrategy = loadStrategy;
            liveDataHashMap.get(category).setValue(oldResource);
        }
    }

    private void createAllLiveData() {
        liveDataHashMap.put(Category.UNKNOWN, new MediatorLiveData<>());
        liveDataHashMap.put(Category.BUSINESS, new MediatorLiveData<>());
        liveDataHashMap.put(Category.ENTERTAINMENT, new MediatorLiveData<>());
        liveDataHashMap.put(Category.GENERAL, new MediatorLiveData<>());
        liveDataHashMap.put(Category.HEALTH, new MediatorLiveData<>());
        liveDataHashMap.put(Category.SCIENCE, new MediatorLiveData<>());
        liveDataHashMap.put(Category.SPORTS, new MediatorLiveData<>());
        liveDataHashMap.put(Category.TECHNOLOGY, new MediatorLiveData<>());
    }

    public boolean hasMoreArticles(Category category) {
        Resource<List<Article>> resource = liveDataHashMap.get(category).getValue();
        if (resource==null || resource.data==null) return true;

        if (Utility.isNetworkAvailable(NewsApplication.getApp())) {
             return !resource.isAllLoadedFromNetwork;
        }else {
            return !resource.isAllLoadedFromDB;
        }
    }

    public boolean isLoading(Category category){
        Resource<List<Article>> resource = liveDataHashMap.get(category).getValue();
        if (resource==null) return false;
        return resource.status == Resource.Status.LOADING;
    }

    public MediatorLiveData<Resource<List<Article>>> getLiveData(Category category) {
        return liveDataHashMap.get(category);
    }

    @Override
    protected void onCleared() {
        if (mRepository != null) {
            mRepository.dispose();
        }
        super.onCleared();
    }
}
