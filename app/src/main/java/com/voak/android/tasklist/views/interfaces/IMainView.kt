package com.voak.android.tasklist.views.interfaces

import android.view.View
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface IMainView : MvpView {
//    fun startAnimation(needFade: Boolean)
    @StateStrategyType(value = SkipStrategy::class)
    fun openTaskActivity(type: Int)

    fun changeAddBtnVisibility(visibility: Boolean)
}