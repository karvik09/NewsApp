package com.newsapp;

import android.app.Application;

import com.newsapp.dagger.AppModule;
import com.newsapp.dagger.DaggerInjector;
import com.newsapp.dagger.Injector;

public class NewsApplication extends Application {

    private static NewsApplication sInstance;
    private Injector mInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mInjector = DaggerInjector.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static NewsApplication getInstance() {
        return sInstance;
    }

    public Injector getInjector(){
        return mInjector;
    }
}
