package com.newsapp.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.newsapp.annotations.ViewModelKey;
import com.newsapp.baseComponent.BaseViewModelFactory;
import com.newsapp.viewModels.FeedDetailsViewModel;
import com.newsapp.viewModels.FeedViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract  class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel.class)
    abstract ViewModel feedViewModel(FeedViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FeedDetailsViewModel.class)
    abstract ViewModel feedDetailsViewModel(FeedDetailsViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(BaseViewModelFactory viewModelFactory);
}
