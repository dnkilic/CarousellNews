package com.dnkilic.carousellnews.di

import com.dnkilic.carousellnews.MainActivity
import com.dnkilic.carousellnews.ui.news.NewsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ContributorModule {

    @ContributesAndroidInjector
    abstract fun bind(): MainActivity

    @ContributesAndroidInjector()
    abstract fun provideListFragment(): NewsListFragment
}