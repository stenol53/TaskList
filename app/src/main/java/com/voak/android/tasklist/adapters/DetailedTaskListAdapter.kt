package com.voak.android.tasklist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.voak.android.tasklist.R
import com.voak.android.tasklist.adapters.DetailedTaskListAdapter.DetailedTaskViewHolder
import com.voak.android.tasklist.db.entities.Task
import com.voak.android.tasklist.utils.Constants
import java.util.*

class DetailedTaskListAdapter(
    private val clickCallback: IRecyclerViewClickCallback
) : RecyclerView.Adapter<DetailedTaskViewHolder>(), ItemTouchHelperAdapter {

    private var tasks = Collections.emptyList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailedTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.detailed_task_item, parent, false)

        return DetailedTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailedTaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun getSwipedItem(position: Int): Task {
        val task = tasks.removeAt(position)
        notifyItemRemoved(position)

        return task
    }

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    inner class DetailedTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var task: Task
        private val title: TextView = itemView.findViewById(R.id.task_title)
        private val date: TextView = itemView.findViewById(R.id.task_date)
        private val dateImg: ImageView = itemView.findViewById(R.id.calendar_img)
        private val notificationImg: ImageView = itemView.findViewById(R.id.notification_img)

        init {
            itemView.setOnClickListener {
                clickCallback.onItemClick(task.id.toString())
            }
        }

        fun bind(task: Task) {
            this.task = task

            title.text = task.title
            if (task.date != -1L) {
                date.text = getStringDate(Date(task.date))
            }

            if (task.needNotification) {
                notificationImg.visibility = View.VISIBLE
            } else {
                notificationImg.visibility = View.GONE
            }

            when (task.taskType) {
                Constants.IMPORTANT_URGENT -> {
                    dateImg.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.colorRed)
                    notificationImg.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.colorRed)
                }
                Constants.IMPORTANT_NOT_URGENT -> {
                    dateImg.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.colorGreen)
                    notificationImg.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.colorGreen)
                }
                Constants.NOT_IMPORTANT_URGENT -> {
                    dateImg.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.colorOrange)
                    notificationImg.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.colorOrange)
                }
                Constants.NOT_IMPORTANT_NOT_URGENT -> {
                    dateImg.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.colorBlue)
                    notificationImg.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.colorBlue)
                }
            }

        }

        private fun getStringDate(date: Date): String {
            val calendar = Calendar.getInstance().apply { time = date }
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

            return "$dayOfWeek $dayStr.$monthStr.$year, $hourStr:$minuteStr"
        }
    }

}