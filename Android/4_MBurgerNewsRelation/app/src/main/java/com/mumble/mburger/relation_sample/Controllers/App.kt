package com.mumble.mburger.relation_sample.Controllers

import android.app.Application
import mumble.mbpush.MBPush
import mumble.mburger.sdk.MBurger

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MBurger.initialize(Config.MBurgerAPiKey, false)
        MBPush.init(Config.MPushKey)
    }
}