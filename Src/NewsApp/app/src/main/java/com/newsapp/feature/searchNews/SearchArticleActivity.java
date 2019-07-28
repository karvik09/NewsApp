package com.newsapp.feature.searchNews;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.newsapp.NewsApplication;
import com.newsapp.R;
import com.newsapp.baseComponent.BaseActivity;
import com.newsapp.baseComponent.BaseViewModelFactory;
import com.newsapp.callback.OnItemClickListener;
import com.newsapp.callback.RecyclerScrollListener;
import com.newsapp.databinding.ActivitySearchBinding;
import com.newsapp.db.entities.Article;
import com.newsapp.feature.home.ArticleListAdapter;
import com.newsapp.feature.webView.WebViewActivity;
import com.newsapp.models.ErrorObject;
import com.newsapp.network.Resource;
import com.newsapp.utils.ErrorHandler;
import com.newsapp.utils.Utility;
import com.newsapp.viewModels.SearchArticleViewModel;

import java.util.List;

import javax.inject.Inject;

public class SearchArticleActivity extends BaseActivity implements SearchView.OnQueryTextListener, OnItemClickListener {

    private ActivitySearchBinding mBinder;
    @Inject
    BaseViewModelFactory mViewModelFactory;
    SearchArticleViewModel mViewModel;
    private ArticleListAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_search);
        NewsApplication.getApp().getInjector().inject(this);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SearchArticleViewModel.class);
        initViews();
        observeLiveData();

    }
    private void observeLiveData() {

        mViewModel.getArticleLiveData().observe(this, listResource -> {
            switch (listResource.status) {
                case ERROR:
                    handleError(listResource);
                    break;
                case LOADING:
                    handleLoading(listResource);
                    break;
                case SUCCESS:
                    handleSuccess(listResource);
                    break;
            }
        });
    }
    private void handleLoading(Resource<List<Article>> listResource) {
        switch (listResource.loadStrategy) {
            case FRESH:
                listAdapter.addArticles(listResource.data);
                mBinder.progressBar.setVisibility(View.VISIBLE);
                mBinder.notAvailable.setVisibility(View.GONE);
                break;
            case REFRESH:
                break;
            case LOAD_MORE:
                listAdapter.addLoader();
                break;
        }
    }


    private void handleError(Resource<List<Article>> listResource) {
        mBinder.progressBar.setVisibility(View.GONE);
        listAdapter.removeLoader();
        ErrorObject errorObject = ErrorHandler.buildErrorObject(listResource.error, this);
        showToast(errorObject.getMessage());
    }
    private void handleSuccess(Resource<List<Article>> listResource) {
        mBinder.progressBar.setVisibility(View.GONE);
        listAdapter.addArticles(listResource.data);
        mBinder.notAvailable.setVisibility(hasArticles(listResource) ? View.GONE : View.VISIBLE);
    }
    private boolean hasArticles(Resource<List<Article>> listResource) {
        return listResource != null && listResource.data != null && !listResource.data.isEmpty();
    }

    private void initViews() {
        setSupportActionBar(mBinder.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Initialise List
        listAdapter = new ArticleListAdapter(this);
        mBinder.articleList.setAdapter(listAdapter);
        mBinder.articleList.addOnScrollListener(mScrollListener);

        //search view
        mBinder.searchView.setQueryHint(getString(R.string.search_articles));
        mBinder.searchView.setOnQueryTextListener(this);
        mBinder.searchView.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }else return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        hideKeyboard();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            searchForArticles(Resource.DataLoadStrategy.FRESH);
        }
        return true;
    }

    private RecyclerScrollListener mScrollListener = new RecyclerScrollListener() {
        @Override
        public boolean hasMoreItems() {
            return mViewModel.hasMoreItems();
        }

        @Override
        public boolean isLoading() {
            return mViewModel.isLoading();
        }

        @Override
        public void loadMore() {
            searchForArticles(Resource.DataLoadStrategy.LOAD_MORE);
        }
    };

    private void searchForArticles(Resource.DataLoadStrategy loadStrategy) {
        if (Utility.isNetworkAvailable(this)) {
            mViewModel.searchArticles(mBinder.searchView.getQuery().toString(),loadStrategy);
        }else {
            showToast(getString(R.string.connection_error));
        }
    }

    @Override
    public void onItemClick(int position) {
        Resource<List<Article>> articles = mViewModel.getArticleLiveData().getValue();
        if (articles==null || articles.data==null ) return;
        WebViewActivity.start(this,articles.data.get(position).getUrl());
    }
}
