package com.appttude.h_mal.days_left.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_register.*


class Register : Fragment(), RegisterCallBack {
    val viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onStarted() {
        pb.visibility = View.VISIBLE
    }

    override fun onNameInvalid() {
        pb.visibility = View.GONE
        name_register.error = getString(R.string.error_invalid_email)
        name_register.requestFocus()
    }

    override fun onEmailInvalid() {
        pb.visibility = View.GONE
        email_register.error = getString(R.string.error_invalid_email)
        email_register.requestFocus()
    }

    override fun onPasswordInvalid(id: Int) {
        pb.visibility = View.GONE
        when(id){
            0 -> password_top.error = getString(R.string.error_invalid_password)
            1 -> password_top.error = getString(R.string.no_match_password)
        }
        password_top.requestFocus()
    }

    override fun onPasswordTwoInvalid() {
        pb.visibility = View.GONE
        password_bottom.error = getString(R.string.error_invalid_password)
        password_bottom.requestFocus()
    }

    override fun onSuccess() {
        pb.visibility = View.GONE

        Intent(context, MainActivity::class.java).apply {
            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
            activity?.finish()
        }
    }

    override fun onFailure(message: String) {
        pb.visibility = View.GONE
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}
