package com.newsapp.callback;

import com.newsapp.db.entities.Feed;

public interface OnItemClickListener {
    void onItemClick(Feed feed);
}
