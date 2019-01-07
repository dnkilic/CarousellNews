package com.dnkilic.carousellnews.repository

import com.dnkilic.carousellnews.repository.local.db.NewsDataDao
import com.dnkilic.carousellnews.repository.model.News
import com.dnkilic.carousellnews.repository.remote.retrofit.ApiMethods
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class NewsRepository @Inject constructor(
        private val newsDataDao: NewsDataDao,
        private val apiMethods: ApiMethods) {

    private fun getNewsFromDb(): Observable<List<News>> {
        return newsDataDao.getNews().filter { it.isNotEmpty() }
    }

    private fun getNewsFromApi(): Observable<List<News>> {
        return apiMethods.getArticles()
                .doOnNext { insert(it) }
    }

    private fun insert(users: List<News>) = Observable.fromCallable { newsDataDao.insertAll(users) }

    fun getNews(): Observable<List<News>> {
            return Observable.concatArrayEager(
                getNewsFromDb(),
                getNewsFromApi()
                        .materialize()
                        .filter { !it.isOnError }
                        .dematerialize<List<News>>())
    }
}