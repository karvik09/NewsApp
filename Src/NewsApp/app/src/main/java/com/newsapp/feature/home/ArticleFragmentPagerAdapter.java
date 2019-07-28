package com.newsapp.feature.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.newsapp.constants.AppConstant;
import com.newsapp.constants.Category;
import com.newsapp.utils.ViewUtils;

import java.util.ArrayList;

public class ArticleFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<ViewUtils.Tab> tabs;
    ArticleFragmentPagerAdapter(@NonNull FragmentManager fm, ArrayList<ViewUtils.Tab> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.CATEGORY_KEY, tabs.get(position).getCategory().toString());
        Fragment fragment = new ArticleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

}
