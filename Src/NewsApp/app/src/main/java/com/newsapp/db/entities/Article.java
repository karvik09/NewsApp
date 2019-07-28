package com.newsapp.db.entities;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "article")
public class Article {

    @Ignore
    @SerializedName(value = "source")
    private Source sourceObj;
    @SerializedName(value = "sourceString")
    private String source;
    private String author;
    private String category;

    @PrimaryKey
    @NonNull
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Source getSourceObj() {
        return sourceObj;
    }

    public void setSourceObj(Source sourceObj) {
        this.sourceObj = sourceObj;
    }

    public String getSource() {
        if (!TextUtils.isEmpty(source)) {
            return source;
        }else {
            return sourceObj == null ? "" : sourceObj.getName();
        }
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
