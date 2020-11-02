package com.voak.android.tasklist.services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.voak.android.tasklist.base.BaseApp
import com.voak.android.tasklist.db.dao.TaskDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StartupService() : IntentService(TAG) {

    @Inject
    lateinit var taskDao: TaskDao

    init {
        BaseApp.instance.component.inject(this)
    }

    @SuppressLint("CheckResult")
    override fun onHandleIntent(intent: Intent?) {
        taskDao.getNeedNotificationActiveTasks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ tasks ->
                val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                tasks.forEach { task ->
                    if (task.date >= System.currentTimeMillis()) {
                        val i = AlarmReceiver.newIntent(this, task.id, task.title)
                        val pi = PendingIntent.getBroadcast(this, task.id, i, 0)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.date, pi)
                    }
                }
            }, {

            })
    }

    companion object {
        private const val TAG = "StartupService"

        fun start(context: Context?) {
            val intent = Intent(context, StartupService::class.java)
            context?.startService(intent)
        }

    }
}