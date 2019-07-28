package com.newsapp.models;

public class ErrorObject {

    private String title;
    private String message;
    private int code;

    public ErrorObject(String title, String message, int code) {
        this.title = title;
        this.message = message;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
