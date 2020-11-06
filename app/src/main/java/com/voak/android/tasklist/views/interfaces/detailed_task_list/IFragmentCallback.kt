package com.voak.android.tasklist.views.interfaces.detailed_task_list

interface IFragmentCallback {
    fun updateType(type: Int)
    fun getTasksCount(): Int
}