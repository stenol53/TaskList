package com.voak.android.tasklist.views.interfaces.detailed_task_list

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface IActiveTaskListFragmentView : IDetailedTaskListFragmentView {

    @StateStrategyType(value = SkipStrategy::class)
    fun openTaskActivity(type: Int)

}