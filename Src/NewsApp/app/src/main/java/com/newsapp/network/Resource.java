package com.newsapp.network;

public class Resource<T> {

    public enum Status{
        LOADING,SUCCESS,ERROR
    }
    public enum Source{
        DB,NETWORK
    }

    public enum DataLoadStrategy {
        FRESH,REFRESH,LOAD_MORE
    }

    public Status status;

    public T data;

    public Source source;

    public Throwable error;

    public boolean isAllLoadedFromDB;

    public boolean isAllLoadedFromNetwork;

    public DataLoadStrategy loadStrategy = DataLoadStrategy.FRESH;


    public Resource(Status status, T data, Source source, Throwable error,DataLoadStrategy loadStrategy) {
        this.status = status;
        this.data = data;
        this.source = source;
        this.error = error;
        this.loadStrategy = loadStrategy;
    }

    public static <T> Resource<T> success(T data, Source source,DataLoadStrategy loadStrategy){
        return new Resource<>(Status.SUCCESS, data, source, null,loadStrategy);
    }

    public static <T> Resource<T> error(Throwable error,T data ,Source source,DataLoadStrategy loadStrategy){
        return new Resource<>(Status.ERROR, data, source, error,loadStrategy);
    }

    public static <T> Resource<T> loading(T data, Source source,DataLoadStrategy loadStrategy){
        return new Resource<>(Status.LOADING, data, source, null,loadStrategy);
    }

}
