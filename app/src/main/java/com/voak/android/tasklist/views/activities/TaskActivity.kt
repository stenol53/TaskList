package com.voak.android.tasklist.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.voak.android.tasklist.R
import com.voak.android.tasklist.base.BaseActivity

class TaskActivity : BaseActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_task
    }

    companion object {
        private const val EXTRA_TYPE = "com.voak.android.tasklist.views.activities.taskactivity.task_type"
        private const val EXTRA_TASK_ID = "com.voak.android.tasklist.views.activities.taskactivity.task_id"
        private const val EXTRA_TASK_ID_AS_BYTES = "com.voak.android.tasklist.views.activities.taskactivity.task_id_bytes"

        fun newIntent(context: Context, type: Int): Intent {
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra(EXTRA_TYPE, type)

            return intent
        }

        fun newIntentById(context: Context, taskId: String): Intent {
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra(EXTRA_TASK_ID, taskId)

            Log.i(this::class.simpleName, "newIntentById $taskId")

            return intent
        }

        fun newIntent(context: Context, taskId: ByteArray): Intent {
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra(EXTRA_TASK_ID_AS_BYTES, taskId)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())

        if (savedInstanceState == null) {
            val type = intent.getIntExtra(EXTRA_TYPE, -1)
            val id = intent.getStringExtra(EXTRA_TASK_ID)
            val idByteArray = intent.getByteArrayExtra(EXTRA_TASK_ID_AS_BYTES)
            if (type != -1) {
                viewRouter.navigateToTaskFragment(type)
            } else if (!id.isNullOrEmpty()) {
                viewRouter.navigateToTaskFragment(id)
            } else {
                Log.i(this::class.simpleName, "taskId ${String(idByteArray)}")
                idByteArray?.let {
                    val idStr = String(it)
                    viewRouter.navigateToTaskFragment(idStr)
                }
            }
        }
    }
}