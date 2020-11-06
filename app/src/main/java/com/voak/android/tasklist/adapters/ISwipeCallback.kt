package com.voak.android.tasklist.adapters

import com.voak.android.tasklist.db.entities.Task

interface ISwipeCallback {

    fun onSwipeLeft(task: Task)
    fun onSwipeRight(task: Task)

}