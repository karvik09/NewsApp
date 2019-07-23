package com.newsapp.dagger;

import com.newsapp.activities.FeedActivity;
import com.newsapp.activities.FeedDetailsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class,RestModule.class,ViewModelModule.class})
public interface Injector {

    void inject(FeedActivity activity);
    void inject(FeedDetailsActivity activity);
}
