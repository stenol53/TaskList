package com.voak.android.tasklist.presenters

import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.voak.android.tasklist.views.interfaces.IMainView

@InjectViewState
class MainActivityPresenter() : MvpPresenter<IMainView>() {

    fun onAddTaskBtnAction() {
        viewState.changeAddBtnVisibility(false)
    }

    fun onDropActionTrue(type: Int) {
        viewState.openTaskActivity(type)
        viewState.changeAddBtnVisibility(true)
    }

    fun onDropActionFalse() {
        viewState.changeAddBtnVisibility(true)
    }
}