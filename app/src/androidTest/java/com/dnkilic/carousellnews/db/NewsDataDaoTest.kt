package com.dnkilic.carousellnews.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dnkilic.carousellnews.repository.local.db.NewsDatabase
import com.dnkilic.carousellnews.repository.model.News
import io.reactivex.functions.Predicate
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var db: NewsDatabase

    @Before
    fun setup() {

        db = Room.inMemoryDatabaseBuilder(context,
                NewsDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun close() {
        db.close()
    }

    @Test
    fun checkDbNotNull() {
        MatcherAssert.assertThat(db, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(db.newsDao(), CoreMatchers.notNullValue())
    }

    @Test
    fun insertAndRead() {
        // act
        db.newsDao().insert(ARTICLE)
        val loaded = db.newsDao().getNewsById("id")

        // verify
        MatcherAssert.assertThat(loaded, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.title, CoreMatchers.`is`("title"))
        MatcherAssert.assertThat(loaded.description, CoreMatchers.`is`("desc"))
        MatcherAssert.assertThat(loaded.id, CoreMatchers.`is`("id"))
    }

    @Test
    fun insertAndDelete() {
        // act
        db.newsDao().insert(ARTICLE)
        db.newsDao().deleteNewsBy("id")
        val loaded = db.newsDao().getNewsById("id")

        // verify
        MatcherAssert.assertThat(loaded, CoreMatchers.nullValue())
    }

    @Test
    fun insertMultipleAndGetMultiple() {
        // arrange
        val articles = createArticles(3, "title", "desc")

        // act
        db.newsDao().insertAll(articles)
        val loaded = db.newsDao().getNews().test()

        // verify
        loaded.assertValue(Predicate {
            return@Predicate  it.size == articles.size
        })
    }

    @Test
    fun insertMultipleArticlesWithSameId() {
        // arrange
        val article1 = createArticle("id1", "title", "desc")
        val article2 = createArticle("id1", "title", "desc")
        val article3 = createArticle("id1", "title", "latest")
        val news = listOf(article1, article2, article3)

        // act
        db.newsDao().insertAll(news)
        val loaded = db.newsDao().getNews().test()

        // verify
        loaded.assertValue(Predicate {
            return@Predicate  it.size != news.size && it[0].description == "latest"
        })
    }

    @Test
    fun insertAndCheck() {
        val article = createArticle("id1", "title", "desc")
        db.newsDao().insert(article)
        val needToUpdate = db.newsDao().needToUpdate("id1", 0L)
        MatcherAssert.assertThat(needToUpdate,  CoreMatchers.`is`(false))
    }

    companion object {
        // arrange
        @JvmStatic
        val ARTICLE = createArticle("id", "title", "desc")

        @JvmStatic
        private fun createArticles(count: Int, title: String, description: String): List<News> {
            return (0 until count).map {
                createArticle(
                        title = title + it,
                        description = description + it,
                        id = "id$it"
                )
            }
        }

        @JvmStatic
        fun createArticle(id: String,
                                  title: String,
                                  description: String) = News(
                id = id,
                title = title,
                description = description,
                timeCreated = 0,
                bannerUrl = "",
                rank = 1
        )
    }
}
