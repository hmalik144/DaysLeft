package com.appttude.h_mal.days_left.ui.resultDialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.appttude.h_mal.days_left.R
import kotlinx.android.synthetic.main.dialog_test_result.view.*

class ResultDialog(
    context: Context,
    result: Pair<Boolean, String?>,
    callback: (() -> Unit)
): AlertDialog(context) {

    init {
        val layoutView = getView()
        setView(layoutView)
        setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, _ ->
            callback()
        }
        setOnDismissListener {
            callback()
        }
        create()
        setViewDetails(layoutView, result)
        show()
    }

    private fun getView(): View = View.inflate(context, R.layout.dialog_test_result, null)

    private fun setViewDetails(dialogView: View, result: Pair<Boolean, String?>) {
        val imageRes = if (result.first) {
            R.mipmap.ic_success } else { R.mipmap.ic_failed }

        dialogView.result_dialog_icon?.setImageResource(imageRes)
        dialogView.result_dialog_text?.text = result.second
    }
}