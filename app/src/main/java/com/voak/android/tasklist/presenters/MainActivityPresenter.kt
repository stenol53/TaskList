package com.voak.android.tasklist.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.voak.android.tasklist.views.interfaces.IMainView

@InjectViewState
class MainActivityPresenter() : MvpPresenter<IMainView>() {

    private var needFade: Boolean = false


    fun onAddTaskBtnAction() {
        needFade = !needFade
        viewState.startAnimation(needFade)
    }

    fun onColoredBtnAction(type: Int) {
        viewState.openTaskActivity(type)
        onAddTaskBtnAction()
    }
}