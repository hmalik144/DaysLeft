package com.appttude.h_mal.days_left.helper

import com.appttude.h_mal.days_left.models.TaskObject
import com.appttude.h_mal.days_left.models.TimeObject
import com.appttude.h_mal.days_left.utils.HOURLY_CONST
import com.appttude.h_mal.days_left.utils.PIECE_CONST
import com.appttude.h_mal.days_left.utils.formatToTwoDp

class ShiftHelper {
    companion object{
        fun String?.ifPieceRate(
            piece: (() -> Unit),
            hourly: (() -> Unit)
        ){
            if (equals(PIECE_CONST)){
                piece.invoke()
            }else if (equals(HOURLY_CONST)){
                hourly.invoke()
            }
        }
    }

    // converts hours float (eg 6.75 hrs into 6 Hours 45 Mins)
    fun convertTimeFloat(timeIn: Float?): String? {
        if (timeIn == null) return null
        if (timeIn < 0) return null
        val stringBuilder = StringBuilder()
        val hour = timeIn.toInt().takeIf { it > 0 }?.let {
            stringBuilder.append(it).append(" hours ")
            it
        } ?: 0

        ((timeIn - hour).toInt() * 60).takeIf { it > 0 }?.let{
            stringBuilder.append(it).append(" mins")
        }

        return stringBuilder.toString()
    }

    fun createTaskSummary(taskObject: TaskObject?): String? {
        taskObject ?: return null
        val summary = StringBuilder()
        summary.append(taskObject.workType)
            .append(" - ")
            .append(createRateSummary(taskObject))

        return summary.toString()
    }

    // return "$##.## per type"
    // type being unit or hour
    fun createRateSummary(taskObject: TaskObject?): String {
        val summary = StringBuilder()
        summary.append("$")
            .append(taskObject?.rate?.formatToTwoDp())
            .append(" per ")
        if (taskObject?.workType.equals(PIECE_CONST)) {
            summary.append("unit")
        } else {
            summary.append("hour")
        }
        return summary.toString()
    }

    // returns "HH:mm - HH:mm"
    fun getTimeInOutString(timeObject: TimeObject?): String? {
        timeObject ?: return null
        return timeObject.timeIn + " - " + timeObject.timeOut
    }


    fun getBreakTimeString(breakMins: Int?): String? {
        breakMins ?: return null
        val hoursFloat = (breakMins / 60).toFloat()
        return convertTimeFloat(hoursFloat)
    }
}