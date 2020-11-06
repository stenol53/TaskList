package com.voak.android.tasklist.views.interfaces.detailed_task_list

interface IActivityCallback {
    fun onActiveTasksLoaded(count: Int)
    fun onFinishedTasksLoaded(count: Int)
    fun detachCallback()
}