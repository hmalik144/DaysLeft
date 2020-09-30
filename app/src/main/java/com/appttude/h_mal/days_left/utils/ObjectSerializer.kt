package com.appttude.h_mal.days_left.utils

import androidx.lifecycle.ViewModel
import com.google.gson.Gson

abstract class ObjectSerializer {
    val gson by lazy { Gson() }

    fun Any.serialiseToString(): String? {
        return gson.toJson(this)
    }
}