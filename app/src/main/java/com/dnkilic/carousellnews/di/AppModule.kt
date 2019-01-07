package com.dnkilic.carousellnews.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.dnkilic.carousellnews.BuildConfig
import com.dnkilic.carousellnews.repository.NewsRepository
import com.dnkilic.carousellnews.repository.local.db.NewsDataDao
import com.dnkilic.carousellnews.repository.local.db.NewsDatabase
import com.dnkilic.carousellnews.repository.remote.retrofit.ApiMethods
import dagger.Module
import javax.inject.Singleton
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

const val SCHEDULER_MAIN_THREAD = "mainThread"
const val SCHEDULER_IO = "io"

private const val WRITE_TIME_OUT = 1L
private const val READ_TIME_OUT = 1L
private const val CONNECT_TIME_OUT = 1L

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesNewsDatabase(context: Context): NewsDatabase = Room.databaseBuilder(context,
            NewsDatabase::class.java, "carousell.db").build()

    @Singleton
    @Provides
    fun providesNewsDataDao(newsDatabase: NewsDatabase): NewsDataDao = newsDatabase.newsDao()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MINUTES)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .addInterceptor(logging)
                .build()
    }

    @Singleton
    @Provides
    fun provideApiMethods(okHttpClient: OkHttpClient): ApiMethods {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiMethods::class.java)
    }

    @Singleton
    @Provides
    fun provideNewsRepository(newsDataDao: NewsDataDao,
                              apiMethods: ApiMethods): NewsRepository =
            NewsRepository(newsDataDao, apiMethods)

    @Provides
    @Named(SCHEDULER_MAIN_THREAD)
    fun provideAndroidMainThreadScheduler() : Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Named(SCHEDULER_IO)
    fun provideIoScheduler() : Scheduler = Schedulers.io()

}