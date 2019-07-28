package com.newsapp.feature.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.newsapp.R;
import com.newsapp.baseComponent.BaseActivity;
import com.newsapp.baseComponent.BaseFragment;
import com.newsapp.callback.OnItemClickListener;
import com.newsapp.callback.RecyclerScrollListener;
import com.newsapp.constants.AppConstant;
import com.newsapp.constants.Category;
import com.newsapp.constants.Country;
import com.newsapp.databinding.FragmentArticleBinding;
import com.newsapp.db.entities.Article;
import com.newsapp.feature.webView.WebViewActivity;
import com.newsapp.models.ErrorObject;
import com.newsapp.network.Resource;
import com.newsapp.utils.ErrorHandler;
import com.newsapp.viewModels.ArticleViewModel;

import java.util.List;


public class ArticleFragment extends BaseFragment implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private FragmentArticleBinding mBinder;
    private Category mCategory;
    private ArticleListAdapter articleListAdapter;
    private ArticleViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_article, container, false);
        return mBinder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCategory = Category.fromString(getArguments().getString(AppConstant.CATEGORY_KEY));
        mViewModel = ViewModelProviders.of(getActivity()).get(ArticleViewModel.class);
        initViews();
        observeLiveData();
//        mViewModel.loadArticles(mCategory, Resource.DataLoadStrategy.FRESH);
    }

    private void observeLiveData() {

        mViewModel.getLiveData(mCategory).observe(this, listResource -> {
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

        mViewModel.getCountryLiveData().observe(this, country -> {
            mViewModel.loadArticles(mCategory, Resource.DataLoadStrategy.FRESH);
        });
    }

    private void handleSuccess(Resource<List<Article>> listResource) {
        mBinder.progressBar.setVisibility(View.GONE);
        mBinder.refreshView.setRefreshing(false);
        articleListAdapter.addArticles(listResource.data);
        mBinder.notAvailable.setVisibility(hasArticles(listResource) ? View.GONE : View.VISIBLE);
    }

    private boolean hasArticles(Resource<List<Article>> listResource) {
        return listResource != null && listResource.data != null && !listResource.data.isEmpty();
    }

    private void handleLoading(Resource<List<Article>> listResource) {
        switch (listResource.loadStrategy) {
            case FRESH:
                articleListAdapter.addArticles(listResource.data);
                mBinder.progressBar.setVisibility(View.VISIBLE);
                mBinder.notAvailable.setVisibility(View.GONE);
                break;
            case REFRESH:
                break;
            case LOAD_MORE:
                articleListAdapter.addLoader();
                break;
        }
    }

    private void handleError(Resource<List<Article>> listResource) {
        mBinder.progressBar.setVisibility(View.GONE);
        mBinder.refreshView.setRefreshing(false);
        articleListAdapter.removeLoader();
        ErrorObject errorObject = ErrorHandler.buildErrorObject(listResource.error, getContext());
        showToast(errorObject.getMessage());
    }

    private void initViews() {
        mBinder.refreshView.setEnabled(false);
        mBinder.refreshView.setOnRefreshListener(this);
        articleListAdapter = new ArticleListAdapter(this);
        mBinder.articleRecyclerView.addOnScrollListener(mScrollListener);
        mBinder.articleRecyclerView.setAdapter(articleListAdapter);

    }

    private RecyclerScrollListener mScrollListener = new RecyclerScrollListener() {
        @Override
        public boolean hasMoreItems() {
            return mViewModel.hasMoreArticles(mCategory);
        }

        @Override
        public boolean isLoading() {
            return mViewModel.isLoading(mCategory);
        }

        @Override
        public void loadMore() {
            mViewModel.loadArticles(mCategory, Resource.DataLoadStrategy.LOAD_MORE);
        }
    };

    @Override
    public void onItemClick(int position) {
        Resource<List<Article>> articles = mViewModel.getLiveData(mCategory).getValue();
        if (articles==null || articles.data==null ) return;
        WebViewActivity.start(getContext(),articles.data.get(position).getUrl());
    }

    @Override
    public void onRefresh() {
        if (mViewModel.isLoading(mCategory)) {
            mBinder.refreshView.setRefreshing(false);
        }else {
            mViewModel.loadArticles(mCategory, Resource.DataLoadStrategy.REFRESH);
        }
    }
}
