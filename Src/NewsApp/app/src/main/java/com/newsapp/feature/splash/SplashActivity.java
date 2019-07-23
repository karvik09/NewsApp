package com.newsapp.feature.splash;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.newsapp.R;
import com.newsapp.activities.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
