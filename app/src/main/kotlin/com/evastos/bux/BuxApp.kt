package com.evastos.bux

import com.evastos.bux.di.component.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class BuxApp : DaggerApplication() {

    companion object {
        lateinit var instance: BuxApp
            private set
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.create()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        initLogging()
        initDateTimeLibrary()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDateTimeLibrary() {
        AndroidThreeTen.init(this)
    }
}