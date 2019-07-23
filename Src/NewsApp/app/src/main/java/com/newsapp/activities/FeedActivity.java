package com.newsapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;


import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.NewsApplication;
import com.newsapp.R;
import com.newsapp.adapters.FeedListAdapter;
import com.newsapp.baseComponent.BaseViewModelFactory;
import com.newsapp.callback.OnItemClickListener;
import com.newsapp.databinding.ActivityFeedBinding;
import com.newsapp.db.entities.Feed;
import com.newsapp.network.Resource;
import com.newsapp.utils.Utility;
import com.newsapp.viewModels.FeedViewModel;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import javax.inject.Inject;

public class FeedActivity extends BaseActivity implements LifecycleOwner, OnItemClickListener {

    private FeedViewModel mViewModel;
    private ActivityFeedBinding mBinder;
    private FeedListAdapter mListAdapter;

    @Inject BaseViewModelFactory mViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_feed);
        NewsApplication.getInstance().getInjector().inject(this);
        mViewModel=ViewModelProviders.of(this,mViewModelFactory).get(FeedViewModel.class);
        initViews();
        ObserveEvent();
        mViewModel.loadFeeds();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    private void initViews() {
        mBinder.feedList.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#e2dede")));
        mBinder.feedList.addItemDecoration(itemDecoration);
        mListAdapter = new FeedListAdapter(this);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mListAdapter);
        mBinder.feedList.addItemDecoration(headersDecor);

        mBinder.feedList.setAdapter(mListAdapter);
        mListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

        mBinder.feedRefreshView.setEnabled(false);
    }

    private void ObserveEvent() {
        mViewModel.getFeedLiveData().observe(this,resource -> {
            switch (resource.status){
                case SUCCESS:
                    mBinder.progressBar.setVisibility(View.GONE);
                    mListAdapter.updateWithNewList(resource.data);
                    break;
                case ERROR:
                    if(resource.source== Resource.Source.NETWORK){
                        showToast("Something Went Wrong!");
                    }
                    mBinder.progressBar.setVisibility(View.GONE);
                    break;
                case LOADING:
                    mBinder.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mBinder.offlineTag.setVisibility(Utility.isNetworkAvailable(context) ?
                    View.GONE : View.VISIBLE);
        }
    };

    @Override
    public void onItemClick(Feed feed) {
        Intent intent = new Intent(this, FeedDetailsActivity.class);
        intent.putExtra("name",feed.getName());
        startActivity(intent);
    }
}
