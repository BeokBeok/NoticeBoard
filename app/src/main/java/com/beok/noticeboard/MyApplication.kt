package com.beok.noticeboard

import android.app.Application
import com.beok.noticeboard.di.DaggerAppComponent

class MyApplication : Application() {

    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}