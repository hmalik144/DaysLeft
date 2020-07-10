package com.appttude.h_mal.days_left.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.login.FullscreenActivity.Companion.fragmentManagerLogin
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPassword : Fragment(), ForgotPasswordCallback {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onStarted() {
        pb.visibility = View.VISIBLE
    }

    override fun onEmailInvalid() {
        pb.visibility = View.GONE
        reset_pw.error = getString(R.string.error_invalid_password)
    }

    override fun onSuccess() {
        pb.visibility = View.GONE
        fragmentManagerLogin.popBackStack()

    }

    override fun onFailure(message: String) {
        pb.visibility = View.GONE
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

}
