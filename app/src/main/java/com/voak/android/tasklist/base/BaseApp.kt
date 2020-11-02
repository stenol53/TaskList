package com.voak.android.tasklist.base

import android.app.Application
import com.voak.android.tasklist.di.components.AppComponent
import com.voak.android.tasklist.di.components.DaggerAppComponent
import com.voak.android.tasklist.di.modules.AppModule

class BaseApp : Application() {

    lateinit var component: AppComponent

    companion object {
        lateinit var instance: BaseApp private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()
    }

    private fun setup() {
        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}