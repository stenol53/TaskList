package com.voak.android.tasklist.views.interfaces.detailed_task_list

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface IDetailedTaskListView : MvpView {
    fun updateFragmentsType(type: Int)
    fun setupViews(type: Int)
    fun updateButtonsScale(type: Int)
    fun updateTitleCount(count: Int?)
}