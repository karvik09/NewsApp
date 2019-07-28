package com.newsapp.feature.webView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;

import com.newsapp.R;
import com.newsapp.baseComponent.BaseActivity;
import com.newsapp.constants.AppConstant;
import com.newsapp.databinding.ActivityWebViewBinding;

public class WebViewActivity extends BaseActivity {

    private ActivityWebViewBinding mBinder;
    private String articleUrl;

    public static void start(Context context, String articleUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(AppConstant.URL, articleUrl);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                Window.PROGRESS_VISIBILITY_ON);

        articleUrl = getIntent().getStringExtra(AppConstant.URL);
        initViews();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mBinder.webview.canGoBack()) {
                        mBinder.webview.goBack();
                    } else {
                        onBackPressed();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void initViews() {
        setSupportActionBar(mBinder.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mBinder.webProgress.setNormalColor(Color.WHITE);
        mBinder.webProgress.setProgressColor(getResources().getColor(R.color.colorAccent));
        initWebView();
    }
    /**
     * method to initialize webview
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mBinder.webview.getSettings().setJavaScriptEnabled(true);
        mBinder.webview.loadUrl(articleUrl);
//        mBinder.webview.setWebViewClient(new WebViewClient());
        mBinder.webview.setInitialScale(1);
        mBinder.webview.getSettings().setBuiltInZoomControls(true);
        mBinder.webview.getSettings().setUseWideViewPort(true);
        mBinder.webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mBinder.webProgress.setProgress(newProgress);
                mBinder.webProgress.setVisibility(newProgress<100? View.VISIBLE:View.GONE);
            }
        });
    }
}
