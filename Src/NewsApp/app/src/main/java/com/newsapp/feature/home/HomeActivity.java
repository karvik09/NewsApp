package com.newsapp.feature.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.tabs.TabLayout;
import com.newsapp.NewsApplication;
import com.newsapp.R;
import com.newsapp.baseComponent.BaseActivity;
import com.newsapp.baseComponent.BaseViewModelFactory;
import com.newsapp.callback.OnItemClickListener;
import com.newsapp.constants.AppConstant;
import com.newsapp.constants.Country;
import com.newsapp.databinding.ActivityHomeBinding;
import com.newsapp.feature.searchNews.SearchArticleActivity;
import com.newsapp.utils.ViewUtils;
import com.newsapp.viewModels.ArticleViewModel;

import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import javax.inject.Inject;


public class HomeActivity extends BaseActivity implements OnItemClickListener {

    private ActivityHomeBinding mBinder;
    private boolean isBackPressed = false;
    private ArrayList<ViewUtils.Menu> menus;
    private DrawerLayout mDrawer;
    private ArticleFragmentPagerAdapter fragmentAdapter;
    private MenuListAdapter menuListAdapter;

    private ArticleViewModel mViewModel;

    @Inject
    BaseViewModelFactory mViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_home);
        NewsApplication.getApp().getInjector().inject(this);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ArticleViewModel.class);
        initViews();
        mViewModel.getCountryLiveData().observe(this, country -> {
            menuListAdapter.selectCountry(country);
        });
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.headlines));
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        //ViewPager
        ArrayList<ViewUtils.Tab> tabs = ViewUtils.buildTabs(getResources().getStringArray(R.array.category_title_array));
        fragmentAdapter = new ArticleFragmentPagerAdapter(getSupportFragmentManager(), tabs);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(fragmentAdapter);

        //Tabs
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //Menus
        menus = ViewUtils.buildMenus(getResources().getStringArray(R.array.country_title_arrays));
        menuListAdapter = new MenuListAdapter(menus, this);
        mBinder.menuRecyclerView.setAdapter(menuListAdapter);

        //Decoration
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.menu_divider));
        mBinder.menuRecyclerView.addItemDecoration(decoration);

        //loading Navigation Image
        Glide.with(this)
                .load(AppConstant.NAVIGATION_HEADER_IMAGE_URL)
                .placeholder(R.drawable.image_placeholder)
                .transition(new DrawableTransitionOptions().crossFade())
                .centerCrop()
                .into(mBinder.headerImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //adding Search to menu
        MenuItem searchItem = menu.add(0, AppConstant.MenuItem.Search, 0, getString(R.string.search));
        searchItem.setIcon(R.drawable.ic_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case AppConstant.MenuItem.Search:
                startActivity(new Intent(this, SearchArticleActivity.class));
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (isBackPressed) {
                super.onBackPressed();
            }else {
                showToast(getString(R.string.back_press_message));
                isBackPressed = true;
                new Handler().postDelayed(() -> {
                    isBackPressed = false;
                }, 2000);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        if (menuListAdapter.getSelected() != menus.get(position).getType()) {
            mViewModel.getCountryLiveData().setValue(menus.get(position).getType());
        }
    }
}
