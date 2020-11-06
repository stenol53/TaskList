package com.voak.android.tasklist.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IDetailedTaskListView

@InjectViewState
class DetailedTaskListPresenter : MvpPresenter<IDetailedTaskListView>() {

    var type: Int = -1

    fun onCreate(type: Int) {
        this.type = type
        viewState.setupViews(type)
    }

    fun onColoredBtnClicked(type: Int) {
        if (this.type != type) {
            viewState.setupViews(type)
            viewState.updateFragmentsType(type)
            viewState.updateButtonsScale(this.type)
            this.type = type
        }
    }

    fun onNeedUpdateTitleCount(count: Int?) {
        viewState.updateTitleCount(count)
    }

}