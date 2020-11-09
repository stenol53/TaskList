package com.voak.android.tasklist.views.interfaces.detailed_task_list

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.*
import com.voak.android.tasklist.db.entities.Task

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface IDetailedTaskListFragmentView : MvpView {
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setTasks(tasks: List<Task>)
    @StateStrategyType(value = SkipStrategy::class)
    fun openTaskActivity(id: String)
    fun setButtonsColor(type: Int)
}