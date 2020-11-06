package com.voak.android.tasklist.views.fragments

import android.os.Bundle
import android.util.Log
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
import com.voak.android.tasklist.R
import com.voak.android.tasklist.adapters.*
import com.voak.android.tasklist.db.entities.Task
import com.voak.android.tasklist.presenters.ActiveTaskListPresenter
import com.voak.android.tasklist.utils.Constants
import com.voak.android.tasklist.views.activities.TaskActivity
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IActiveTaskListFragmentView
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IActivityCallback
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IFragmentCallback

class ActiveTaskListFragment : MvpAppCompatFragment(), IActiveTaskListFragmentView,
    IFragmentCallback, IRecyclerViewClickCallback, ISwipeCallback {

    private lateinit var taskList: RecyclerView
    private lateinit var addTaskBtn: TextView

    @InjectPresenter
    lateinit var presenter: ActiveTaskListPresenter

    private var activityCallback: IActivityCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_active_task_list, container, false)

        taskList = view.findViewById(R.id.recycler_view)
        taskList.layoutManager = LinearLayoutManager(requireContext())
        taskList.adapter = DetailedTaskListAdapter(this)

        val taskListCallback = SwipeCallback(taskList.adapter as ItemTouchHelperAdapter, this)

        val touchHelper = ItemTouchHelper(taskListCallback)
        touchHelper.attachToRecyclerView(taskList)

        addTaskBtn = view.findViewById(R.id.add_new_task_btn)

        addTaskBtn.setOnClickListener {
            presenter.onAddTaskBtnClicked()
        }

        val type = requireArguments().getInt(ARG_TYPE)

        presenter.onCreateView(type)

        return view
    }

    override fun onDetach() {
        super.onDetach()
        activityCallback = null
    }

    override fun setButtonsColor(type: Int) {
        when (type) {
            Constants.IMPORTANT_URGENT -> {
                addTaskBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_red)
                addTaskBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
            }
            Constants.IMPORTANT_NOT_URGENT -> {
                addTaskBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_green)
                addTaskBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGreen))
            }
            Constants.NOT_IMPORTANT_URGENT -> {
                addTaskBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_orange)
                addTaskBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
            }
            Constants.NOT_IMPORTANT_NOT_URGENT -> {
                addTaskBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_blue)
                addTaskBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlue))
            }
        }
    }

    override fun setTasks(tasks: List<Task>) {
        (taskList.adapter as DetailedTaskListAdapter).setTasks(tasks)
        activityCallback?.onActiveTasksLoaded(tasks.size)
    }

    override fun openTaskActivity(type: Int) {
        startActivity(TaskActivity.newIntent(requireContext(), type))
    }

    override fun openTaskActivity(id: String) {
        startActivity(TaskActivity.newIntentById(requireContext(), id))
    }

    override fun updateType(type: Int) {
        Log.i(this::class.simpleName, "updateType $type")
        presenter.onCreateView(type)
    }

    fun setActivityCallback(callback: IActivityCallback) {
        activityCallback = callback
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

    override fun getTasksCount(): Int {
        return (taskList.adapter as DetailedTaskListAdapter).itemCount
    }

    companion object {
        private const val ARG_TYPE =
            "com.voak.android.tasklist.views.fragments.activetasklistfragment.type"

        fun newInstance(type: Int): ActiveTaskListFragment {
            Log.i(this::class.simpleName, "newInstance $type")
            val args = Bundle()
            args.putInt(ARG_TYPE, type)

            return ActiveTaskListFragment().apply { arguments = args }
        }
    }

}