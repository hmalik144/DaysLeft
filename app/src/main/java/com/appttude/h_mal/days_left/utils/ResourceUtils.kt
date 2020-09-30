package com.appttude.h_mal.days_left.utils

import android.content.Context
import androidx.core.content.res.ResourcesCompat

const val PIECE_CONST = "Piece Rate"
const val HOURLY_CONST = "Hourly"
const val UID = "uid"

const val USER_FIREBASE = "users"
const val EMPLOYER_FIREBASE = "employers"
const val SHIFT_FIREBASE = "shifts"
const val ABN_FIREBASE = "abn"
const val RECENT_EMPLOYERS = "recentemployers"
const val TASK_LIST = "taskList"

fun Context.loadColour(id: Int): Int {
    return ResourcesCompat.getColor(resources, id,null)
}