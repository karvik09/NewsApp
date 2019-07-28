package com.newsapp.feature.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.newsapp.R;
import com.newsapp.baseComponent.BaseActivity;
import com.newsapp.feature.home.HomeActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            if (!isDestroyed() && !isFinishing()){
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }

        }, 2000);
    }
}
