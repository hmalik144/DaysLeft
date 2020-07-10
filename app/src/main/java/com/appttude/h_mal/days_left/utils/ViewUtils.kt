package com.appttude.h_mal.days_left.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import com.squareup.picasso.Picasso
import java.lang.Exception

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showToast(@StringRes resourceId: Int) {
    Toast.makeText(this, resourceId, Toast.LENGTH_LONG).show()
}

fun Context.hideKeyboard(view: View?) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}

//fun View.navigateTo(navigationId: Int) {
//    try {
//        Navigation.findNavController(this).navigate(navigationId)
//    }catch (e: IllegalArgumentException){
//        e.printStackTrace()
//    }
//
//}
//
//fun View.navigateTo(navDirections: NavDirections) {
//    Navigation.findNavController(this).navigate(navDirections)
//}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

//fun ImageView.setPicassoIcon(profileUrl: String?) {
//    val width = layoutParams?.width?.takeIf { it > 0 }
//    val height = layoutParams?.height?.takeIf { it > 0 }
//
//    val builder = Picasso.get()
//        .load(profileUrl)
//        .placeholder(R.drawable.ic_prova_id_icon)
//    if (width != null && height != null){
//        builder
//            .resize(width, height)
//            .centerCrop()
//    }
//    builder.into(this)
//}
//
//fun ImageView.setPicassoIcon(profileUri: Uri?) {
//    val width = layoutParams?.width?.takeIf { it > 0 }
//    val height = layoutParams?.height?.takeIf { it > 0 }
//
//    val builder = Picasso.get()
//        .load(profileUri)
//        .placeholder(R.drawable.ic_prova_id_icon)
//    if (width != null && height != null){
//        builder
//            .resize(width, height)
//            .centerCrop()
//    }
//    builder.into(this)
//}