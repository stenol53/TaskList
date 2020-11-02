package com.voak.android.tasklist.di.modules

import android.content.Context
import androidx.room.Room
import com.voak.android.tasklist.db.AppDatabase
import com.voak.android.tasklist.db.dao.TaskDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(appContext: Context): AppDatabase {
        return  Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "task_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }

}