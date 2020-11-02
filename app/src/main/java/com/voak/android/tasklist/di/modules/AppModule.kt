package com.voak.android.tasklist.di.modules

import android.content.Context
import androidx.room.Room
import com.voak.android.tasklist.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = appContext



}