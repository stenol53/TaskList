package com.voak.android.tasklist.views.interfaces.detailed_task_list

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface IFinishedTaskListFragmentView : IDetailedTaskListFragmentView {

}