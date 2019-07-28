package com.newsapp.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.newsapp.annotations.ViewModelKey;
import com.newsapp.baseComponent.BaseViewModelFactory;
import com.newsapp.repositories.SearchArticleRepository;
import com.newsapp.viewModels.ArticleViewModel;
import com.newsapp.viewModels.SearchArticleViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract  class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ArticleViewModel.class)
    abstract ViewModel articleViewModel(ArticleViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchArticleViewModel.class)
    abstract ViewModel searchArticleViewModel(SearchArticleViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(BaseViewModelFactory viewModelFactory);
}
