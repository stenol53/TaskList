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
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.card.MaterialCardView
import com.voak.android.tasklist.R
import com.voak.android.tasklist.adapters.*
import com.voak.android.tasklist.db.entities.Task
import com.voak.android.tasklist.presenters.TaskListPresenter
import com.voak.android.tasklist.views.activities.DetailedTaskListActivity
import com.voak.android.tasklist.views.activities.TaskActivity
import com.voak.android.tasklist.views.interfaces.ITaskListVIew

class TaskListFragment : MvpAppCompatFragment(), ITaskListVIew, IRecyclerViewClickCallback, ISwipeCallback {

    private lateinit var title: TextView
    private lateinit var line: View
    private lateinit var cardView: MaterialCardView
    private lateinit var recycler: RecyclerView

    @InjectPresenter lateinit var presenter: TaskListPresenter

    @ProvidePresenter
    fun providePresenter(): TaskListPresenter {
        return TaskListPresenter(requireArguments().getInt(ARG_TASK_TYPE))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        title = view.findViewById(R.id.task_title)
        line = view.findViewById(R.id.line_view)
        cardView = view.findViewById(R.id.cardView)
        recycler = view.findViewById(R.id.task_list)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = TaskListAdapter(this)

        val taskListCallback = SwipeCallback(recycler.adapter as ItemTouchHelperAdapter, this)

        val touchHelper = ItemTouchHelper(taskListCallback)
        touchHelper.attachToRecyclerView(recycler)

        title.setOnClickListener {
            presenter.onTitleClicked()
        }

        setViews()
        presenter.onCreateView()

        Log.i(TaskListFragment::class.simpleName, "onCreateView: $presenter")

        return view
    }

    private fun setViews() {
        when (requireArguments()[ARG_TASK_TYPE]) {
            0 -> {
                title.text = getString(R.string.important_urgent)
                title.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
                line.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
                cardView.strokeColor = ContextCompat.getColor(requireContext(), R.color.colorRed)
            }
            1 -> {
                title.text = getString(R.string.important_not_urgent)
                title.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGreen))
                line.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorGreen))
                cardView.strokeColor = ContextCompat.getColor(requireContext(), R.color.colorGreen)
            }
            2 -> {
                title.text = getString(R.string.not_important_urgent)
                title.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                line.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorOrange
                ))
                cardView.strokeColor = ContextCompat.getColor(requireContext(), R.color.colorOrange)
            }
            3 -> {
                title.text = getString(R.string.not_important_not_urgent)
                title.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlue))
                line.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBlue))
                cardView.strokeColor = ContextCompat.getColor(requireContext(), R.color.colorBlue)
            }
        }
    }

    override fun setTasks(tasks: List<Task>) {
        Log.i(TaskListFragment::class.simpleName, tasks.size.toString() + " $this")
        (recycler.adapter as TaskListAdapter).setTasks(tasks)
    }

    override fun openTaskActivity(taskId: String) {
        startActivity(TaskActivity.newIntentById(requireContext(), taskId))
    }

    override fun openDetailedTaskListActivity() {
        startActivity(DetailedTaskListActivity.newIntent(requireContext(),
            requireArguments().getInt(ARG_TASK_TYPE)))
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

    companion object {
        const val IMPORTANT_URGENT = 0
        const val IMPORTANT_NOT_URGENT = 1
        const val NOT_IMPORTANT_URGENT = 2
        const val NOT_IMPORTANT_NOT_URGENT = 3

        private const val ARG_TASK_TYPE = "com.voak.android.tasklist.views.fragments.tasklistfragment.arg_task_type"

        fun newInstance(type: Int): TaskListFragment {
            val args = Bundle()
            args.putInt(ARG_TASK_TYPE, type)
            val fragment = TaskListFragment()
            fragment.arguments = args

            return fragment
        }
    }



}