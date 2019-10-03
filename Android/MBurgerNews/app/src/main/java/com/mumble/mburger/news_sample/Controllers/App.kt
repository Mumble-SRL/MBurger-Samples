package com.mumble.mburger.news_sample.Controllers

import android.app.Application
import mumble.mburger.sdk.MBurger

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MBurger.initialize(Config.MBurgerAPiKey, false)
    }
}