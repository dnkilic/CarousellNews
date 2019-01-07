package com.dnkilic.carousellnews.fragment

import androidx.databinding.DataBindingComponent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.dnkilic.carousellnews.R
import com.dnkilic.carousellnews.binding.FragmentBindingAdapters
import com.dnkilic.carousellnews.repository.model.Command
import com.dnkilic.carousellnews.repository.model.News
import com.dnkilic.carousellnews.ui.news.NewsListFragment
import com.dnkilic.carousellnews.ui.news.NewsListViewModel
import com.dnkilic.carousellnews.util.SingleFragmentActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

// This test run only for Android P devices

@RunWith(AndroidJUnit4::class)
class NewsListFragmentTest {

    @get:Rule
    var activityRule: ActivityTestRule<SingleFragmentActivity> = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private val newsLiveData = MutableLiveData<Command>()
    private lateinit var viewModel: NewsListViewModel
    private lateinit var mockBindingAdapter: FragmentBindingAdapters
    private lateinit var newsListFragment: NewsListFragment

    @Before
    fun setup() {
        viewModel = Mockito.mock(NewsListViewModel::class.java)
        mockBindingAdapter = Mockito.mock(FragmentBindingAdapters::class.java)
        Mockito.`when`(viewModel.news).thenReturn(newsLiveData)
        newsListFragment = NewsListFragment()
        newsListFragment.viewModeFactory = createViewModelFactory(viewModel)
        newsListFragment.dataBindingComponent = object : DataBindingComponent {
            override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
                return mockBindingAdapter
            }
        }
        activityRule.activity.setFragment(newsListFragment)
    }

    @Test
    fun checkProgressDisplayed() {
        viewModel.news.postValue(Command.Loading)
        newsLiveData.postValue(Command.Loading)
        Espresso.onView(ViewMatchers.withId(R.id.progress)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    companion object {
        @JvmStatic
        private val NEWS = News("id", "title", "description",
                "bannerUrl", 1000L, 1)
    }

    private fun <T : ViewModel> createViewModelFactory(viewModel: T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
                if (viewModelClass.isAssignableFrom(viewModel.javaClass)) {
                    @Suppress("UNCHECKED_CAST")
                    return viewModel as T
                }
                throw IllegalArgumentException("Unknown view model class " + viewModelClass)
            }
        }
    }
}
