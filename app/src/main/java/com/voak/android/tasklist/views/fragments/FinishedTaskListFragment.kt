package com.voak.android.tasklist.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.button.MaterialButton
import com.voak.android.tasklist.R
import com.voak.android.tasklist.adapters.*
import com.voak.android.tasklist.db.entities.Task
import com.voak.android.tasklist.presenters.FinishedTaskListPresenter
import com.voak.android.tasklist.utils.Constants
import com.voak.android.tasklist.views.activities.TaskActivity
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IActivityCallback
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IFinishedTaskListFragmentView
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IFragmentCallback

class FinishedTaskListFragment : MvpAppCompatFragment(), IFinishedTaskListFragmentView,
    IFragmentCallback, IRecyclerViewClickCallback, ISwipeCallback {

    private lateinit var taskList: RecyclerView
    private lateinit var removeTasksBtn: TextView

    @InjectPresenter
    lateinit var presenter: FinishedTaskListPresenter

    private var activityCallback: IActivityCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_finished_task_list, container, false)

        taskList = view.findViewById(R.id.recycler_view)
        taskList.layoutManager = LinearLayoutManager(requireContext())
        taskList.adapter = DetailedTaskListAdapter(this)

        val taskListCallback = SwipeCallback(taskList.adapter as ItemTouchHelperAdapter, this)

        val touchHelper = ItemTouchHelper(taskListCallback)
        touchHelper.attachToRecyclerView(taskList)

        removeTasksBtn = view.findViewById(R.id.remove_tasks_btn)
        removeTasksBtn.setOnClickListener {
            presenter.onRemoveTasksBtnClicked()
        }

        val type = requireArguments().getInt(ARG_TYPE)

        presenter.onCreateView(type)

        return view
    }

    override fun setButtonsColor(type: Int) {
        when (type) {
            Constants.IMPORTANT_URGENT -> {
                removeTasksBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_red)
                removeTasksBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
            }
            Constants.IMPORTANT_NOT_URGENT -> {
                removeTasksBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_green)
                removeTasksBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGreen))
            }
            Constants.NOT_IMPORTANT_URGENT -> {
                removeTasksBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_orange)
                removeTasksBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
            }
            Constants.NOT_IMPORTANT_NOT_URGENT -> {
                removeTasksBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_blue)
                removeTasksBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlue))
            }
        }
    }

    override fun setTasks(tasks: List<Task>) {
        (taskList.adapter as DetailedTaskListAdapter).setTasks(tasks)
        activityCallback?.onFinishedTasksLoaded(tasks.size)
    }

    override fun updateType(type: Int) {
        presenter.onCreateView(type)
    }

    override fun openTaskActivity(id: String) {
        startActivity(TaskActivity.newIntentById(requireContext(), id))
    }

    override fun onItemClick(taskId: String) {
        presenter.onItemClicked(taskId)
    }

    override fun onSwipeLeft(task: Task) {
        presenter.onItemSwipedLeft(task)
    }

    override fun onSwipeRight(task: Task) {
        presenter.onItemSwipedRight(task)
    }

    fun setActivityCallback(callback: IActivityCallback) {
        activityCallback = callback
    }

    override fun getTasksCount(): Int {
        return (taskList.adapter as DetailedTaskListAdapter).itemCount
    }

    companion object {
        private const val ARG_TYPE =
            "com.voak.android.tasklist.views.fragments.finishedtasklistfragment.type"

        fun newInstance(type: Int): FinishedTaskListFragment {
            val args = Bundle()
            args.putInt(ARG_TYPE, type)

            return FinishedTaskListFragment().apply { arguments = args }
        }
    }

}