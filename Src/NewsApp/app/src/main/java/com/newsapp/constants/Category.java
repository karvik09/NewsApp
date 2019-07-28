package com.newsapp.constants;

import android.text.TextUtils;

public enum Category {

    UNKNOWN("unknown"),
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");

    private String name;

    Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Category fromString(String name) {
        if (TextUtils.isEmpty(name)) return UNKNOWN;

        if (TextUtils.equals(name, UNKNOWN.toString())) {
            return UNKNOWN;
        } else if (TextUtils.equals(name, BUSINESS.toString())) {
            return BUSINESS;
        } else if (TextUtils.equals(name, ENTERTAINMENT.toString())) {
            return ENTERTAINMENT;
        } else if (TextUtils.equals(name, GENERAL.toString())) {
            return GENERAL;
        } else if (TextUtils.equals(name, HEALTH.toString())) {
            return HEALTH;
        } else if (TextUtils.equals(name, SCIENCE.toString())) {
            return SCIENCE;
        } else if (TextUtils.equals(name, SPORTS.toString())) {
            return SPORTS;
        } else if (TextUtils.equals(name, TECHNOLOGY.toString())) {
            return TECHNOLOGY;
        } else return UNKNOWN;
    }
}
