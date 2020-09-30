package com.appttude.h_mal.days_left.utils

fun Double.formatToTwoDp() = String.format("%.2f", this)
fun Float.formatToTwoDp() = String.format("%.2f", this)

fun Int.formatDateInt(): String =  String.format("%02d", this)

fun getTimeString(hourOfDay: Int, minute: Int): String{
    return String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)
}