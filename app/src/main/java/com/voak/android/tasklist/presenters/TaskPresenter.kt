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
import com.voak.android.tasklist.views.interfaces.ITaskView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@InjectViewState
class TaskPresenter : MvpPresenter<ITaskView>() {

    @Inject
    lateinit var taskDao: TaskDao
    @Inject lateinit var context: Context
    private lateinit var task: Task
    private var isNewTask = false

    private var date = Date()

    init {
        BaseApp.instance.component.inject(this)
    }

    fun onDateBtnClicked() {
        if (task.date != -1L) {
            viewState.openDatePicker(Date(task.date))
        } else {
            viewState.openDatePicker(date)
        }
    }

    fun onNavigationBtnClicked() {
        viewState.goBack()
    }

    fun onDateResult(date: Date) {
        this.date = date
        Log.i(TaskPresenter::class.simpleName, date.toString())
        viewState.openTimePicker()
    }

    fun onTimeResult(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        date = GregorianCalendar(year, month, day, hour, minute).time
        task.date = date.time
        setDate(date)
        Log.i(TaskPresenter::class.simpleName, date.time.toString())
    }

    fun onSwitchCheckedChange(isChecked: Boolean) {
        viewState.setDateBtnEnabled(isChecked)
    }

    @SuppressLint("CheckResult")
    fun onCreateView(taskId: String) {
        isNewTask = false
        taskDao.getTaskById(taskId.toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                task = it[0]
                viewState.setTitle(task.title)
                viewState.setDetails(task.details)
                if (task.date != -1L) {
                    setDate(Date(task.date))
                }
                viewState.setNotificationSwitchChecked(task.needNotification)
                viewState.setViewsColor(task.taskType)
                viewState.setDateBtnEnabled(task.needNotification)
            },
            {
                Log.e(TaskPresenter::class.simpleName, it.message.orEmpty())
            })
    }

    fun onCreateView(taskType: Int) {
        isNewTask = true
        task = Task(
            title = "",
            details = "",
            taskType = taskType,
            isFinished = false,
            date = -1L,
            needNotification = false
        )
        viewState.setViewsColor(taskType)
    }

    @SuppressLint("CheckResult")
    fun onAcceptBtnClicked(title: String, details: String, needNotification: Boolean) {
        task.title = title
        task.details = details
        task.needNotification = needNotification

        Log.i(this::class.simpleName, task.needNotification.toString())

        if (isNewTask) {
            insertTask()
        } else {
            updateTask()
        }

    }

    @SuppressLint("CheckResult")
    private fun insertTask() {
        taskDao.insert(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                task.id = it.toInt()
                startAlarm()
                viewState.goBack()
            },{
                Log.e(TaskPresenter::class.simpleName, it.message.orEmpty())
            })
    }

    @SuppressLint("CheckResult")
    private fun updateTask() {
        taskDao.update(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                startAlarm()
                viewState.goBack()
                SimpleDateFormat.getDateInstance().calendar.time.time
            },{
                Log.e(TaskPresenter::class.simpleName, it.message.orEmpty())
            })
    }

    private fun startAlarm() {
        val intent = AlarmReceiver.newIntent(context, task.id, task.title)
        val pi = PendingIntent.getBroadcast(context, task.id, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (task.needNotification) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.date, pi)
        } else {
            alarmManager.cancel(pi)
            pi.cancel()
        }
    }

    private fun setDate(date: Date) {
        val calendar = Calendar.getInstance().apply {
            time = date
        }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Пн"
            Calendar.TUESDAY -> "Вт"
            Calendar.WEDNESDAY -> "Ср"
            Calendar.THURSDAY -> "Чт"
            Calendar.FRIDAY -> "Пт"
            Calendar.SATURDAY -> "Сб"
            Calendar.SUNDAY -> "Вс"
            else -> "Пн"
        }
        val dayStr = if (day < 10) "0$day" else day.toString()
        val monthStr = if (month < 10) "0$month" else month.toString()
        val hourStr = if (hour < 10) "0$hour" else hour.toString()
        val minuteStr = if (minute < 10) "0$minute" else minute.toString()

        val dateString = "$dayOfWeek $dayStr.$monthStr.$year $hourStr:$minuteStr"
        viewState.setDate(dateString)
    }
}