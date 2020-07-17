package com.appttude.h_mal.days_left.utils

import com.google.gson.reflect.TypeToken

inline fun <reified T> genericType() = object: TypeToken<T>() {}.type
