package com.newsapp.models;

import androidx.room.Embedded;

import com.newsapp.db.entities.Feed;
import com.newsapp.db.entities.Like;

public class FeedModel {

    @Embedded
    private Feed feed;

    @Embedded
    private Like like;

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Like getLike() {
        return like;
    }

    public void setLike(Like like) {
        this.like = like;
    }
}
