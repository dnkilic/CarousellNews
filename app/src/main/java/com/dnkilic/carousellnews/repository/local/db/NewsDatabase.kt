package com.dnkilic.carousellnews.repository.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dnkilic.carousellnews.repository.model.News

@Database(
        entities = [News::class],
        version = 1,
        exportSchema = false)
abstract class NewsDatabase: RoomDatabase() {

    abstract fun newsDao(): NewsDataDao
}