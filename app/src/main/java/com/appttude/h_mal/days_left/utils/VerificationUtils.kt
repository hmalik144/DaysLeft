package com.appttude.h_mal.days_left.utils

import android.text.TextUtils
import java.util.regex.Pattern

// Password must contain 8-16 characters,
// including at least 1 upper case letter and at least 1 numerical digit.
fun isPasswordValid(password: String?): Boolean {
    return password?.let {
        Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}\$").matcher(it).find()
    } ?: false

}

fun isNameValid(name: String?): Boolean {
    return name?.let {
        Pattern.compile("[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}\$").matcher(it).find()
    } ?: false
}

fun isEmailValid(email: String?): Boolean {
    return email?.let {
         android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    } ?: false
}