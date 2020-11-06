package com.voak.android.tasklist.presenters

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.voak.android.tasklist.base.BaseApp
import com.voak.android.tasklist.db.dao.TaskDao
import com.voak.android.tasklist.db.entities.Task
import com.voak.android.tasklist.services.AlarmReceiver
import com.voak.android.tasklist.utils.AlarmHelper
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IFinishedTaskListFragmentView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class FinishedTaskListPresenter: MvpPresenter<IFinishedTaskListFragmentView>() {

    @Inject
    lateinit var taskDao: TaskDao
    @Inject
    lateinit var context: Context
    private var type: Int = -1

    init {
        BaseApp.instance.component.inject(this)
    }

    @SuppressLint("CheckResult")
    fun onCreateView(type: Int) {
        this.type = type
        taskDao.getFinishedTasksByType(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ tasks ->
                viewState.setTasks(tasks)
            }, {
                Log.e(this::class.simpleName, it.message.orEmpty())
            })
        viewState.setButtonsColor(type)
    }

    fun onRemoveTasksBtnClicked() {
        taskDao.deleteAllFinishedTasksByType(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun onItemClicked(id: String) {
        viewState.openTaskActivity(id)
    }

    fun onItemSwipedLeft(task: Task) {
        taskDao.delete(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    @SuppressLint("CheckResult")
    fun onItemSwipedRight(task: Task) {
        task.isFinished = false
        taskDao.update(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (task.needNotification && task.date >= System.currentTimeMillis()) {
                    AlarmHelper.startAlarm(context, task)
                }
            }, {
                Log.i(this::class.simpleName, it.message.orEmpty())
            })
    }
}