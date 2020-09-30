package com.appttude.h_mal.days_left.ui.main.shiftInfo

import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Build
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.models.TimeObject
import kotlinx.android.synthetic.main.dialog_add_times.view.*
import java.util.*

class TimeDialog(
    context: Context,
    timeObject: TimeObject?,
    val timeSelected: (TimeObject) -> Unit
): AlertDialog(context) {

    val currentTimeObject = timeObject?.let {
        if (it.timeIn.isNullOrEmpty()) return@let null
        if (it.timeOut.isNullOrEmpty()) return@let null
        it
    } ?: TimeObject()

    private var timePickerTimePicker: TimePicker? = null
    private var startTimeTextView: TextView? = null
    private var finishTimeTextView: TextView? = null
    private var breakEditText: EditText? = null

    private var currentTag: String? = null
    private var breakInt: Int = 0


    var timeSelect: View.OnClickListener = View.OnClickListener { v ->

        currentTag = v.tag as String
        val timeString: String?

        if (currentTag == "start") {
            timeString = currentTimeObject.timeIn?.takeIf { it.isNotBlank() }
            toggleTextButtons(true)

        } else {
            timeString = currentTimeObject.timeOut?.takeIf { it.isNotBlank() }
            toggleTextButtons(false)
        }

        if (timeString != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePickerTimePicker!!.hour = getTime(timeString)[0]
                timePickerTimePicker!!.minute = getTime(timeString)[1]
            }
        } else {
            val calendar = Calendar.getInstance()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePickerTimePicker!!.hour = calendar.get(Calendar.HOUR_OF_DAY)
                timePickerTimePicker!!.minute = calendar.get(Calendar.MINUTE)
            }

        }
    }


    init {
        val view = createView()
        setView(view)
        setNegativeButton()
        setPositiveButton()
        setButtonColour()
        create()
        show()
    }

    private fun createView(): View {
        val view = View.inflate(context, R.layout.dialog_add_times, null)
        currentTag = "start"

        // instantiate views
        startTimeTextView = view.from_date
        finishTimeTextView = view.to_date
        timePickerTimePicker = view.time_picker
        breakEditText = view.breaktime

        timePickerTimePicker!!.setIs24HourView(true)
        initialiseTime()

        currentTimeObject.breakInt?.toString()?.let {
            breakEditText!!.setText(it)
        }

        startTimeTextView!!.tag = "start"
        finishTimeTextView!!.tag = "finish"

        startTimeTextView!!.setOnClickListener(timeSelect)
        finishTimeTextView!!.setOnClickListener(timeSelect)

        timePickerTimePicker!!.setOnTimeChangedListener { _, hourOfDay, minute ->
            val ddTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)

            if (currentTag == "start") {
                currentTimeObject!!.timeIn = ddTime
            } else {
                currentTimeObject!!.timeOut = ddTime
            }
        }
        toggleTextButtons(true)

        return view
    }

    private fun setNegativeButton(){
        val text = context.getText(android.R.string.cancel)
        setButton(DialogInterface.BUTTON_NEGATIVE, text) { _, _ ->
            dismiss()
        }
    }

    private fun setPositiveButton(){
        val text = context.getText(android.R.string.ok)
        setButton(DialogInterface.BUTTON_POSITIVE, text) { _, _ ->
            if (currentTimeObject.timeIn != null &&
                currentTimeObject.timeOut != null
            ) {
                currentTimeObject.hours = calculateDuration() ?: return@setButton
                currentTimeObject.breakInt = breakInt

                timeSelected.invoke(currentTimeObject)
            }

            dismiss()
        }

    }

    private fun setButtonColour(){
        setOnShowListener {
            val colour = context.resources.getColor(R.color.one)
            getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(colour)
            getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(colour)
        }
    }

    private fun getTime(s: String): IntArray {

        return intArrayOf(
            Integer.parseInt(s.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0]),
            Integer.parseInt(
                s.split(
                    ":".toRegex()
                ).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            )
        )
    }

    private fun initialiseTime() {
        val timeString = currentTimeObject.timeIn?.takeIf { it.isNotBlank() }

        if (timeString != null) {
            timePickerTimePicker!!.currentHour = getTime(timeString)[0]
            timePickerTimePicker!!.currentMinute = getTime(timeString)[1]
        } else {
            val calendar = Calendar.getInstance()

            timePickerTimePicker!!.currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            timePickerTimePicker!!.currentMinute = calendar.get(Calendar.MINUTE)
        }
    }

    private fun calculateDuration(): Float? {
        val startTime = currentTimeObject.timeIn?.takeIf { it.isNotBlank() } ?: return null
        val finishTime = currentTimeObject.timeOut?.takeIf { it.isNotBlank() } ?: return null

        breakInt = breakEditText?.text?.toString().takeIf { !it.isNullOrBlank() }?.toInt() ?: 0

        val hoursIn = getTime(startTime)[0]
        val hoursOut = getTime(finishTime)[0]
        val minutesIn = getTime(startTime)[1]
        val minutesOut = getTime(finishTime)[1]

        val duration = if (hoursOut > hoursIn) {
            hoursOut.toFloat() + minutesOut.toFloat() / 60 - (hoursIn.toFloat() + minutesIn.toFloat() / 60) - breakInt.toFloat() / 60
        } else {
            hoursOut.toFloat() + minutesOut.toFloat() / 60 - (hoursIn.toFloat() + minutesIn.toFloat() / 60) - breakInt.toFloat() / 60 + 24
        }

        val s = String.format("%.2f", duration)
        return java.lang.Float.parseFloat(s)
    }

    private fun toggleTextButtons(top: Boolean) {
        setFadeAnimation(startTimeTextView!!)
        setFadeAnimation(finishTimeTextView!!)

        if (top) {
            startTimeTextView!!.setTypeface(null, Typeface.BOLD)
            finishTimeTextView!!.setTypeface(null, Typeface.NORMAL)

            startTimeTextView!!.setBackgroundColor(context.resources.getColor(android.R.color.white))
            finishTimeTextView!!.setBackgroundColor(context.resources.getColor(R.color.one))
        } else {
            finishTimeTextView!!.setTypeface(null, Typeface.BOLD)
            startTimeTextView!!.setTypeface(null, Typeface.NORMAL)

            finishTimeTextView!!.setBackgroundColor(
                context.resources.getColor(android.R.color.white)
            )
            startTimeTextView!!.setBackgroundColor(context.resources.getColor(R.color.one))
        }
    }

    private fun setFadeAnimation(view: View) {
        val bottomUp = AnimationUtils.loadAnimation(
            context,
            R.anim.fade_in
        )

        view.animation = bottomUp
    }
}