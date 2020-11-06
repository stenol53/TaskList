package com.voak.android.tasklist.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.voak.android.tasklist.db.entities.Task
import com.voak.android.tasklist.services.AlarmReceiver

object AlarmHelper {

    fun startAlarm(context: Context, task: Task) {
        val intent = AlarmReceiver.newIntent(context, task.id, task.title)
        val pi = PendingIntent.getBroadcast(context, task.id, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.date, pi)
    }

    fun cancelAlarm(context: Context, task: Task) {
        val intent = AlarmReceiver.newIntent(context, task.id, task.title)
        val pi = PendingIntent.getBroadcast(context, task.id, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pi)
        pi.cancel()
    }
}