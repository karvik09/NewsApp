package com.newsapp.constants;

public interface AppConstant {

    String CATEGORY_KEY = "category_key";
    String COUNTRY_KEY = "country_key";
    int PAGE_SIZE = 25;
    String API_KEY = "73b7310e6861484586a73685a8cf773f";
    int SCROLL_THRESOLD = 2;
    String URL = "url";
    String NAVIGATION_HEADER_IMAGE_URL = "https://images.unsplash.com/photo-1531315630201" +
            "-bb15abeb1653?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60";
    String NOT_AVAILABLE = "-";

    interface Api{
        String Country = "country";
        String Category = "category";
        String Sources = "sources";
        String PageSize = "pageSize";
        String Page = "page";
        String ApiKey = "apiKey";
    }
    interface MenuItem{
        int Search = 120;
    }
}
