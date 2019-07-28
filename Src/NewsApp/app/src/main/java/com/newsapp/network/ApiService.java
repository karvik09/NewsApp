package com.newsapp.network;

import com.newsapp.constants.AppConstant;
import com.newsapp.models.HeadlineResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("top-headlines")
    Single<HeadlineResponse> loadArticle(@Query(AppConstant.Api.Page) int page,
                                         @Query(AppConstant.Api.PageSize) int pageSize,
                                         @Query(AppConstant.Api.Country) String country);

    @GET("top-headlines")
    Single<HeadlineResponse> loadCategoryArticle(@Query(AppConstant.Api.Page) int pageNumber,
                                                 @Query(AppConstant.Api.PageSize) int pageSize,
                                                 @Query(AppConstant.Api.Category) String category,
                                                 @Query(AppConstant.Api.Country) String country);

    @GET("top-headlines")
    Single<HeadlineResponse> serchArticles(@Query("q") String query,
                                           @Query(AppConstant.Api.Page) int pageNumber,
                                           @Query(AppConstant.Api.PageSize) int pageSize);
}
