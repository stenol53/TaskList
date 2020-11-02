package com.voak.android.tasklist.navigation

import com.voak.android.tasklist.R
import com.voak.android.tasklist.base.BaseActivity
import com.voak.android.tasklist.views.fragments.TaskFragment
import com.voak.android.tasklist.views.fragments.TaskListFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewRouter @Inject constructor() {

    var currentActivity: BaseActivity? = null

    fun setUpMainActivityFragments() {
        currentActivity?.supportFragmentManager?.beginTransaction()?.apply {
            add(R.id.frame_1, TaskListFragment.newInstance(TaskListFragment.IMPORTANT_URGENT))
            add(R.id.frame_2, TaskListFragment.newInstance(TaskListFragment.IMPORTANT_NOT_URGENT))
            add(R.id.frame_3, TaskListFragment.newInstance(TaskListFragment.NOT_IMPORTANT_URGENT))
            add(R.id.frame_4, TaskListFragment.newInstance(TaskListFragment.NOT_IMPORTANT_NOT_URGENT))
            commit()
        }
    }

    fun navigateToTaskFragment(type: Int) {
        currentActivity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragment_container, TaskFragment.newInstance(type))
            commit()
        }
    }

    fun navigateToTaskFragment(taskId: String) {
        currentActivity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragment_container, TaskFragment.newInstance(taskId))
            commit()
        }
    }
}