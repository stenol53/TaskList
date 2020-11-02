package com.voak.android.tasklist.views.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.voak.android.tasklist.R
import java.util.*

class DatePickerFragment : DialogFragment() {

    private lateinit var datePicker: DatePicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = requireArguments().getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val view = LayoutInflater.from(activity)
            .inflate(R.layout.dialog_date_picker, null)

        datePicker = view.findViewById(R.id.dialog_date_picker)
        datePicker.init(year, month, day, null)
        datePicker.minDate = Date().time
        datePicker.firstDayOfWeek = Calendar.MONDAY

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                val newYear = datePicker.year
                val newMonth = datePicker.month
                val newDay = datePicker.dayOfMonth

                val newDate = GregorianCalendar(newYear, newMonth, newDay).time
                sendResult(Activity.RESULT_OK, newDate)
            }
            .create()
    }

    private fun sendResult(resultCode: Int, date: Date) {
        val intent = Intent()
        intent.putExtra(EXTRA_DATE, date)
        targetFragment?.onActivityResult(targetRequestCode, resultCode, intent)
    }

    companion object {
        const val EXTRA_DATE = "com.voak.android.tasklist.date"
        private const val ARG_DATE = "date"

        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            val fragment = DatePickerFragment()

            return fragment.apply {
                arguments = args
            }
        }
    }
}