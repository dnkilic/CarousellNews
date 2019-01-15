package com.dnkilic.carousellnews.ui.news

import androidx.lifecycle.MutableLiveData
import com.dnkilic.carousellnews.di.SCHEDULER_IO
import com.dnkilic.carousellnews.di.SCHEDULER_MAIN_THREAD
import com.dnkilic.carousellnews.ui.base.BaseViewModel
import com.dnkilic.carousellnews.repository.NewsRepository
import com.dnkilic.carousellnews.repository.model.Command
import io.reactivex.Scheduler
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class NewsListViewModel @Inject constructor(
        var repository: NewsRepository,
        @Named(SCHEDULER_IO) var subscribeOnScheduler:Scheduler,
        @Named(SCHEDULER_MAIN_THREAD) var observeOnScheduler: Scheduler): BaseViewModel() {

    private var syncNews = MutableLiveData<Command>()

    val news : MutableLiveData<Command>
        get() = syncNews

    fun getNews() {
        if (syncNews.value == null) {
            disposables.add(repository.getNews()
                    .debounce(DEBOUNCE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    .subscribeOn(subscribeOnScheduler)
                    .observeOn(observeOnScheduler)
                    .doOnSubscribe { syncNews.value = Command.Loading }
                    .subscribe({ news ->
                        if (news.isNotEmpty()) {
                            syncNews.value = Command.Success(news.sorted())
                        } else {
                            syncNews.value = Command.Error(null)
                        }

                    }, {
                        onSynchronizationError(it)
                    }))
        }
    }

    fun sortNewsByDate() {
        val command = syncNews.value
        if (command is Command.Success) {
            syncNews.value = Command.Success(command.news.sorted())
        }
    }

    fun sortNewsByRankAndDate() {
        val command = syncNews.value
        if (command is Command.Success) {
            val items = command.news
            val sorted = items.sortedWith(Comparator { o1, o2 ->
                when {
                    o1.rank > o2.rank -> 1
                    o1.rank < o2.rank -> -1
                    else -> when {
                        o1.timeCreated < o2.timeCreated -> 1
                        o1.timeCreated > o2.timeCreated -> -1
                        else -> 0
                    }
                }
            })

            syncNews.value = Command.Success(sorted)
        }
    }

    private fun onSynchronizationError(exception: Throwable) {
        if (exception is Exception) {
            syncNews.value = Command.Error(exception.hashCode())
        } else {
            syncNews.value = Command.Error(exception.hashCode())
        }
    }

    companion object {
        private const val DEBOUNCE_TIMEOUT_MS = 400L
    }
}
