package com.newsapp.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.constants.AppConstant;

public abstract class RecyclerScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        int childCount = linearLayoutManager.getItemCount();

        if (childCount>0 && lastVisibleItemPosition + AppConstant.SCROLL_THRESOLD >= childCount
                && hasMoreItems() && !isLoading()) {
            loadMore();
        }
    }

    public abstract boolean hasMoreItems();

    public abstract boolean isLoading();

    public abstract void loadMore();
}
