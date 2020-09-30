package com.appttude.h_mal.days_left.utils

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.appttude.h_mal.days_left.R
import com.google.android.material.textfield.TextInputLayout

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.setVis(b: Boolean) {
    if (b){ show() }else{ hide() }
}

fun View.toggleVisibility() {
    if (isVisible){ hide() }else{ show() }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showToast(@StringRes resourceId: Int) {
    Toast.makeText(this, resourceId, Toast.LENGTH_LONG).show()
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.navigateTo(navigationId: Int) {
    findNavController().navigate(navigationId)
}

fun View.navigateTo(navDirections: NavDirections) {
    Navigation.findNavController(this).navigate(navDirections)
}

fun View.popBack(){
    Navigation.findNavController(this).popBackStack()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}


fun View.layoutListener(work: (() -> Unit)) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            work()
            this@layoutListener.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun Context.inflateLayout(id: Int): View {
    return LayoutInflater
        .from(this)
        .inflate(id, null)
}

fun Context.inflateLayout(id: Int, parent: ViewGroup): View {
    return LayoutInflater
        .from(this)
        .inflate(id, parent, false)
}

fun TextInputLayout.clearError(){
    error = null
}


fun FragmentActivity.navigateTo(f: Fragment){
    supportFragmentManager
        .beginTransaction()
        .replace(
            R.id.container,
            f
        ).commit()
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