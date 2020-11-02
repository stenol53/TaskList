package com.voak.android.tasklist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.voak.android.tasklist.db.dao.TaskDao
import com.voak.android.tasklist.db.entities.Task

@Database(entities = [Task::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}