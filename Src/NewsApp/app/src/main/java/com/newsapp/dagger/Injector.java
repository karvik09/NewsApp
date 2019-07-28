package com.newsapp.dagger;


import com.newsapp.feature.home.HomeActivity;
import com.newsapp.feature.searchNews.SearchArticleActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class,RestModule.class,ViewModelModule.class})
public interface Injector {

   void inject(HomeActivity activity);
   void inject(SearchArticleActivity activity);
}
