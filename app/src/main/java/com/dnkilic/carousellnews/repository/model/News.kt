package com.dnkilic.carousellnews.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class News (
        @PrimaryKey val id: String,
        @ColumnInfo val title: String,
        @ColumnInfo val description: String,
        @ColumnInfo @SerializedName("banner_url") val bannerUrl: String,
        @ColumnInfo @SerializedName("time_created") val timeCreated: Long,
        @ColumnInfo val rank: Int
): Comparable<News> {
    override fun compareTo(other: News) = when {
        other.timeCreated < timeCreated -> -1
        other.timeCreated > timeCreated -> 1
        else -> 0
    }
}