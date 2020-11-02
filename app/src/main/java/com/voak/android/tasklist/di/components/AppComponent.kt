package com.voak.android.tasklist.di.components

import android.content.Context
import com.voak.android.tasklist.base.BaseActivity
import com.voak.android.tasklist.db.AppDatabase
import com.voak.android.tasklist.db.dao.TaskDao
import com.voak.android.tasklist.di.modules.AppModule
import com.voak.android.tasklist.di.modules.DatabaseModule
import com.voak.android.tasklist.navigation.ViewRouter
import com.voak.android.tasklist.presenters.TaskListPresenter
import com.voak.android.tasklist.presenters.TaskPresenter
import com.voak.android.tasklist.services.StartupService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class])
interface AppComponent {
    fun provideContext(): Context
    fun provideViewRouter(): ViewRouter
    fun provideAppDatabase(): AppDatabase
    fun provideTaskDao(): TaskDao

    // Activities
    fun inject(activity: BaseActivity)

    // Presenters
    fun inject(presenter: TaskPresenter)
    fun inject(presenter: TaskListPresenter)

    // Services
    fun inject(service: StartupService)
}