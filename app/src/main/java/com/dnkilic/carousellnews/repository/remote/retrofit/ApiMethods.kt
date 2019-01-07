package com.dnkilic.carousellnews.repository.remote.retrofit

import com.dnkilic.carousellnews.repository.model.News
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface ApiMethods {

    @GET("/carousell-interview-assets/android/carousell_news.json")
    fun getArticles(): Observable<List<News>>

}