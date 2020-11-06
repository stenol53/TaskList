package com.voak.android.tasklist.views.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.voak.android.tasklist.R
import com.voak.android.tasklist.db.entities.Task
import com.voak.android.tasklist.presenters.TaskPresenter
import com.voak.android.tasklist.utils.Constants
import com.voak.android.tasklist.views.interfaces.ITaskView
import java.util.*

class TaskFragment : MvpAppCompatFragment(), ITaskView {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var dateBtn: TextView
    private lateinit var titleEditText: TextInputEditText
    private lateinit var detailsEditText: TextInputEditText
    private lateinit var titleTextInputLayout: TextInputLayout
    private lateinit var detailsTextInputLayout: TextInputLayout
    private lateinit var notificationSwitch: SwitchMaterial

    @InjectPresenter lateinit var presenter: TaskPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        toolbar = view.findViewById(R.id.toolbar)
        dateBtn = view.findViewById(R.id.date_button)
        titleEditText = view.findViewById(R.id.task_title)
        detailsEditText = view.findViewById(R.id.task_details)
        titleTextInputLayout = view.findViewById(R.id.textInputLayout)
        detailsTextInputLayout = view.findViewById(R.id.textInputLayout2)
        notificationSwitch = view.findViewById(R.id.notification_switch)

        toolbar.setNavigationOnClickListener {
            presenter.onNavigationBtnClicked()
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.accept_task -> {
                    presenter.onAcceptBtnClicked(
                        titleEditText.text.toString(),
                        detailsEditText.text.toString(),
                        notificationSwitch.isChecked
                    )
                    true
                }
                else -> false
            }
        }

        dateBtn.setOnClickListener {
            presenter.onDateBtnClicked()
        }

        if (savedInstanceState == null) {

            arguments?.getString(ARG_TASK_ID).let { taskId ->
                if (!taskId.isNullOrEmpty()) {
                    Log.i(this::class.simpleName, "taskId: $taskId")
                    presenter.onCreateView(requireArguments().getString((ARG_TASK_ID))!!)
                } else {
                    arguments?.getInt(ARG_TYPE).let { taskType ->
                        if (taskType != null) {
                            presenter.onCreateView(taskType)
                        }
                    }
                }
            }

            Log.i(TaskFragment::class.simpleName, "savedInstanceState")
        } else {
            titleEditText.setText(savedInstanceState.getString(TITLE_STATE).orEmpty())
            detailsEditText.setText(savedInstanceState.getString(DETAILS_STATE).orEmpty())
        }

        Log.i(TaskFragment::class.simpleName, "onCreateView")

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_DATE -> {
                val date = data?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
                presenter.onDateResult(date)
            }
            REQUEST_TIME -> {
                val hour = data?.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0)
                val minute = data?.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 0)
                presenter.onTimeResult(hour!!, minute!!)        // TODO Исправить !! знаки
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TITLE_STATE, titleEditText.text.toString())
        outState.putString(DETAILS_STATE, detailsEditText.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun showToast() {
        Toast.makeText(requireContext(), "Введите название!", Toast.LENGTH_LONG).show()
    }

    override fun goBack() {
        requireActivity().finish()
    }

    override fun openDatePicker(date: Date) {
        val fm = requireFragmentManager()
        val dialog = DatePickerFragment.newInstance(date)
        dialog.setTargetFragment(this, REQUEST_DATE)
        dialog.show(fm, DIALOG_DATE)
    }

    override fun openTimePicker() {
        val fm = requireFragmentManager()
        val dialog = TimePickerFragment()
        dialog.setTargetFragment(this, REQUEST_TIME)
        dialog.show(fm, DIALOG_TIME)
    }

    override fun setDate(dateString: String) {
        Log.i(TaskFragment::class.simpleName, "setDate")
        dateBtn.text = dateString
    }

    override fun setTitle(title: String) {
        titleEditText.setText(title)
    }

    override fun setDetails(details: String) {
        detailsEditText.setText(details)
    }

    override fun setViewsColor(type: Int) {
        when (type) {
            Constants.IMPORTANT_URGENT -> {
                toolbar.title = context?.getString(R.string.important_urgent)
                toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
                dateBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
                dateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_red)
                titleTextInputLayout.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorRed)
                titleTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.colorRed)
                detailsTextInputLayout.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorRed)
                detailsTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.colorRed)
            }
            Constants.IMPORTANT_NOT_URGENT -> {
                toolbar.title = context?.getString(R.string.important_not_urgent)
                toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorGreen))
                dateBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorGreen))
                dateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_green)
                titleTextInputLayout.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorGreen)
                titleTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.colorGreen)
                detailsTextInputLayout.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorGreen)
                detailsTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.colorGreen)
            }
            Constants.NOT_IMPORTANT_URGENT -> {
                toolbar.title = context?.getString(R.string.not_important_urgent)
                toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                dateBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                dateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_orange)
                titleTextInputLayout.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorOrange)
                titleTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.colorOrange)
                detailsTextInputLayout.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorOrange)
                detailsTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.colorOrange)
            }
            Constants.NOT_IMPORTANT_NOT_URGENT -> {
                toolbar.title = context?.getString(R.string.not_important_not_urgent)
                toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBlue))
                dateBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlue))
                dateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.text_view_border_blue)
                titleTextInputLayout.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorBlue)
                titleTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.colorBlue)
                detailsTextInputLayout.hintTextColor = ContextCompat.getColorStateList(requireContext(), R.color.colorBlue)
                detailsTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.colorBlue)
            }
        }
    }

    override fun setNotificationSwitchChecked(isChecked: Boolean) {
        notificationSwitch.isChecked = isChecked
    }

    companion object {
        private const val TITLE_STATE = "title_state"
        private const val DETAILS_STATE = "details_state"

        private const val ARG_TYPE = "com.voak.android.tasklist.views.fragments.taskfragment.task_type"
        private const val ARG_TASK_ID = "com.voak.android.tasklist.views.fragments.taskfragment.task_id"
        private const val REQUEST_DATE = 0
        private const val REQUEST_TIME = 1

        private const val DIALOG_DATE = "dialog_date"
        private const val DIALOG_TIME = "dialog_time"

        fun newInstance(type: Int): TaskFragment {
            val args = Bundle()
            args.putInt(ARG_TYPE, type)
            return TaskFragment().apply { arguments = args }
        }

        fun newInstance(taskId: String): TaskFragment {
            val args = Bundle()
            args.putString(ARG_TASK_ID, taskId)

            return TaskFragment().apply { arguments = args }
        }
    }
}