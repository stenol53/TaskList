package com.voak.android.tasklist.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "task_type") var taskType: Int,
    @ColumnInfo(name = "is_finished") var isFinished: Boolean,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "details") var details: String,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "need_notification") var needNotification: Boolean
)