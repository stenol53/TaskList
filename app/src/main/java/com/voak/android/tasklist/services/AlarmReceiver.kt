package com.voak.android.tasklist.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.voak.android.tasklist.R
import com.voak.android.tasklist.views.activities.TaskActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(this::class.simpleName, "onReceive")

        context?.let {
            val taskId = intent?.getIntExtra(TASK_ID, -1)
            val taskTitle = intent?.getStringExtra(TASK_TITLE)
            showNotification(it, taskId, taskTitle)
        }
    }

    private fun showNotification(context: Context, taskId: Int?, taskTitle: String?) {
        Log.i(this::class.simpleName, "showNotification")
        if (taskId != -1) {
            taskId?.let {
                val intent = TaskActivity.newIntentById(context, taskId.toString())
                val pi = PendingIntent.getActivity(context, it, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                val resources = context.resources

                val notification = NotificationCompat.Builder(context, "")
                    .setContentTitle(taskTitle)
                    .setContentText(resources.getString(R.string.notification_text))
                    .setContentIntent(pi)
                    .setSmallIcon(R.drawable.ic_baseline_android_24)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .build()

                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(it, notification)
            }
        }
    }

    companion object {
        private const val TASK_ID = "com.voak.android.tasklist.services.alarmreceiver.task_id"
        private const val TASK_TITLE = "com.voak.adndroid.tasklist.services.alarmreceiver.task_title"

        fun newIntent(context: Context, taskId: Int, taskTitle: String): Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(TASK_ID, taskId)
            intent.putExtra(TASK_TITLE, taskTitle)
            return intent
        }
    }
}