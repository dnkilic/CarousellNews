package com.dnkilic.carousellnews.repository

import com.dnkilic.carousellnews.repository.local.db.NewsDataDao
import com.dnkilic.carousellnews.repository.model.News
import com.dnkilic.carousellnews.repository.remote.retrofit.ApiMethods
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class NewsRepository @Inject constructor(
        private val newsDataDao: NewsDataDao,
        private val apiMethods: ApiMethods) {

     private fun getNewsFromDb(): Observable<List<News>> = newsDataDao.getNews()

     private fun getNewsFromApi(): Observable<List<News>> {
        return apiMethods.getArticles()
                .doOnNext {
                    newsDataDao.insertAll(it)
                }
                .onErrorReturn {
                    emptyList()
                }
    }

    fun getNews(): Observable<List<News>> {
        return getNewsFromApi()
                .flatMap {
                    if (it.isEmpty()) {
                        return@flatMap getNewsFromDb()
                    } else {
                        return@flatMap Observable.just(it)
                    }
                }
    }
}