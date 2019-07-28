package com.newsapp.constants;

public enum Country {

    UNITED_URAB_EMIRATES("ae"),
    INDIA("in"),
    UNITED_STATE("us"),
    AUSTRALIA("au"),
    RUSSIA("ru"),
    CHINA("cn"),
    BRAZIL("brazil"),
    MALAYSIA("my");


    private String name;
    Country(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
