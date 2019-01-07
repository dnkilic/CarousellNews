package com.dnkilic.carousellnews.repository.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.dnkilic.carousellnews.repository.model.News
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface NewsDataDao {

    @Insert(onConflict = REPLACE)
    fun insert(news: News)

    @Insert(onConflict = REPLACE)
    fun insertAll(news: List<News>)

    @Query("DELETE FROM news WHERE id == :id")
    fun deleteNewsBy(id: String)

    @Query("SELECT * FROM news WHERE id =:id AND timeCreated !=:timeCreated")
    fun needToUpdate(id: String, timeCreated: Long): Boolean

    @Query("SELECT * FROM news")
    fun getNews(): Observable<List<News>>

    @Query("SELECT * FROM news WHERE id == :id")
    fun getNewsById(id: String): News

}