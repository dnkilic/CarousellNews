package com.dnkilic.carousellnews.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel: ViewModel() {

    protected var disposables = CompositeDisposable()

    override fun onCleared() = disposables.clear()
}