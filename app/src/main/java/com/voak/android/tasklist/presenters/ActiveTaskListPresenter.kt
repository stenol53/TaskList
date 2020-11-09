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
import com.voak.android.tasklist.utils.Constants
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IActiveTaskListFragmentView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class ActiveTaskListPresenter : MvpPresenter<IActiveTaskListFragmentView>() {

    @Inject
    lateinit var taskDao: TaskDao
    @Inject
    lateinit var context: Context

    private var type: Int = -1

    init {
        BaseApp.instance.component.inject(this)
    }

    @SuppressLint("CheckResult")
    fun updateType(type: Int) {
        this.type = type
        taskDao.getActiveTasksByTypeSingle(this.type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ tasks ->
//                Log.i(this::class.simpleName, "updateType ${this.type}")
                viewState.setTasks(tasks)
            }, {
                Log.e(this::class.simpleName, it.message.orEmpty())
            })
        viewState.setButtonsColor(this.type)
    }

    @SuppressLint("CheckResult")
    fun onCreateView(type: Int) {
        this.type = type

        observeDb(Constants.IMPORTANT_URGENT)
        observeDb(Constants.IMPORTANT_NOT_URGENT)
        observeDb(Constants.NOT_IMPORTANT_URGENT)
        observeDb(Constants.NOT_IMPORTANT_NOT_URGENT)

        viewState.setButtonsColor(this.type)
    }

    @SuppressLint("CheckResult")
    private fun observeDb(type: Int) {
        taskDao.getActiveTasksByType(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ tasks ->
                if (this.type == type) {
//                    Log.i(this::class.simpleName, "onCreateView $type")
                    viewState.setTasks(tasks)
                }
            }, {
                Log.e(this::class.simpleName, it.message.orEmpty())
            })
    }

    fun onAddTaskBtnClicked() {
        viewState.openTaskActivity(type)
    }

    fun onItemClicked(id: String) {
        viewState.openTaskActivity(id)
    }

    @SuppressLint("CheckResult")
    fun onItemSwipedLeft(task: Task) {
        taskDao.delete(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (task.needNotification) {
                    AlarmHelper.cancelAlarm(context, task)
                }
            }, {
                Log.i(this::class.simpleName, it.message.orEmpty())
            })
//        viewState.setButtonsColor(type)
    }

    @SuppressLint("CheckResult")
    fun onItemSwipedRight(task: Task) {
        task.isFinished = true
        taskDao.update(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (task.needNotification) {
                    AlarmHelper.cancelAlarm(context, task)
                }
            }, {
                Log.i(this::class.simpleName, it.message.orEmpty())
            })
    }

}