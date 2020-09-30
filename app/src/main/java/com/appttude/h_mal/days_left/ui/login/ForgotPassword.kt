package com.appttude.h_mal.days_left.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.utils.afterTextChanged
import com.appttude.h_mal.days_left.utils.isEmailValid
import com.appttude.h_mal.days_left.utils.showToast
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPassword : Fragment() {

    val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submission_et.apply{
            afterTextChanged {
                til_submission.error = null
            }

            setOnEditorActionListener { _, id, _ ->
                if (id == EditorInfo.IME_ACTION_DONE) {
                    resetPassword()
                    return@setOnEditorActionListener true
                }
                false
            }
        }

        viewModel.operationResetPassword.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                context?.let {ctx ->
                    ctx.showToast(ctx.resources.getString(R.string.password_reset_success))
                    activity?.onBackPressed()
                }
            }
        })
    }

    private fun resetPassword(){
        val email = submission_et.text.toString()
        if (!isEmailValid(email)){
            til_submission.error = "Not a valid email address"
            return
        }
        viewModel.resetPassword(email)
    }
}
