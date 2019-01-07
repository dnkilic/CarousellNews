package com.dnkilic.carousellnews.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dnkilic.carousellnews.repository.NewsRepository
import com.dnkilic.carousellnews.ui.news.NewsListViewModel
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.junit.Before
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class NewsListViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = Mockito.mock(NewsRepository::class.java)
    private var testScheduler: Scheduler = TestScheduler()
    private var newsListViewModel = NewsListViewModel(repository, testScheduler, testScheduler)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun checkIfNotNull() {
        MatcherAssert.assertThat(testScheduler, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(repository, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(newsListViewModel, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(newsListViewModel.repository, CoreMatchers.notNullValue())
    }
}