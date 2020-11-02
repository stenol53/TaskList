package com.voak.android.tasklist.presenters

import android.annotation.SuppressLint
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.voak.android.tasklist.base.BaseApp
import com.voak.android.tasklist.db.dao.TaskDao
import com.voak.android.tasklist.views.interfaces.ITaskListVIew
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class TaskListPresenter(private val taskType: Int) : MvpPresenter<ITaskListVIew>() {

    @Inject lateinit var taskDao: TaskDao

    init {
        BaseApp.instance.component.inject(this)
    }


    @SuppressLint("CheckResult")
    fun onCreateView() {
        taskDao.getActiveTasksByType(taskType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.setTasks(it)
            },
            {
                Log.e(TaskListPresenter::class.simpleName, it.message.orEmpty())
            })
    }

    fun onItemClicked(taskId: String) {
        viewState.openTaskActivity(taskId)
    }
}