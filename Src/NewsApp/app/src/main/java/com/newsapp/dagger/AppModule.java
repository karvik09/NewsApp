package com.newsapp.dagger;

import android.app.Application;

import com.newsapp.db.NewsDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    NewsDatabase provideDatabase(Application application){
        return NewsDatabase.getInstance(application);
    }
}
