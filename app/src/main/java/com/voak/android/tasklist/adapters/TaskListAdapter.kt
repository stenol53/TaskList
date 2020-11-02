package com.voak.android.tasklist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.voak.android.tasklist.R
import com.voak.android.tasklist.db.entities.Task
import java.util.*

class TaskListAdapter(private val holderOnClick: (String) -> Unit) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private var tasks = Collections.emptyList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.simple_task_item, parent, false)

        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTitle: TextView = itemView.findViewById(R.id.task_title)
        private lateinit var task: Task

        init {
            itemView.setOnClickListener {
                holderOnClick(task.id.toString())
            }
        }

        fun bind(task: Task) {
            this.task = task
            taskTitle.text = task.title
        }
    }

}