package com.voak.android.tasklist.views.interfaces

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import java.util.*

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ITaskView : MvpView {
    @StateStrategyType(value = SkipStrategy::class)
    fun goBack()
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun openDatePicker(date: Date)
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun openTimePicker()
    fun setDate(dateString: String)
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun setTitle(title: String)
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun setDetails(details: String)
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun setViewsColor(type: Int)
    fun setNotificationSwitchChecked(isChecked: Boolean)
    @StateStrategyType(value = SkipStrategy::class)
    fun showToast()
}