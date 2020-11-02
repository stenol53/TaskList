package com.voak.android.tasklist.views.interfaces

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.*
import com.voak.android.tasklist.db.entities.Task

@StateStrategyType(value = AddToEndSingleStrategy ::class)
interface ITaskListVIew : MvpView {
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun setTasks(tasks: List<Task>)
    @StateStrategyType(value = SkipStrategy::class)
    fun openTaskActivity(taskId: String)
}