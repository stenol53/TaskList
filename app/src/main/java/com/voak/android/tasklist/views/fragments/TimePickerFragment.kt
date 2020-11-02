package com.voak.android.tasklist.views.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.voak.android.tasklist.R

class TimePickerFragment : DialogFragment() {

    private lateinit var timePicker: TimePicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.dialog_time_picker, null)

        timePicker = view.findViewById(R.id.dialog_time_picker)
        timePicker.setIs24HourView(true)

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    timePicker.hour else timePicker.currentHour
                val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    timePicker.minute else timePicker.currentMinute

                sendResult(Activity.RESULT_OK, hour, minute)
            }
            .create()
    }

    private fun sendResult(resultCode: Int, hour: Int, minute: Int) {
        val intent = Intent()
        intent.putExtra(EXTRA_HOUR, hour)
        intent.putExtra(EXTRA_MINUTE, minute)
        targetFragment?.onActivityResult(targetRequestCode, resultCode, intent)
    }

    companion object {
        const val EXTRA_HOUR = "com.voak.android.tasklist.hour"
        const val EXTRA_MINUTE = "com.voak.android.tasklist.minute"
    }
}