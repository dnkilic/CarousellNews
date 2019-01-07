package com.dnkilic.carousellnews.application

import com.dnkilic.carousellnews.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class CarousellNewsApplication: DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        setupLeakCanary()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent =
                DaggerAppComponent
                        .builder()
                        .application(this)
                        .build()

        appComponent.inject(this)
        return appComponent
    }

    private fun setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

}

