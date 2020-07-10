package com.appttude.h_mal.days_left.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.login.FullscreenActivity.Companion.fragmentManagerLogin
import com.appttude.h_mal.days_left.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class LoginFragment : Fragment(), LoginCallback, KodeinAware {
    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()
    private val viewModel: AuthViewModel by viewModels{ factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                viewModel.onClickLogin(textView)
                return@OnEditorActionListener true
            }
            false
        })

        forgot.setOnClickListener{
            fragmentManagerLogin.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_in)
                .replace(R.id.container, ForgotPassword())
                .addToBackStack("forgot_pw").commit()
        }

        register_button.setOnClickListener {
            fragmentManagerLogin.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_in)
                .replace(R.id.container, Register())
                .addToBackStack("register")
                .commit()
        }
    }

    override fun onStarted() {
        login_progress.visibility = View.VISIBLE
    }

    override fun onEmailInvalid() {
        email.error = getString(R.string.error_invalid_email)
        email.requestFocus()
        login_progress.visibility = View.GONE
    }

    override fun onPasswordInvalid() {
        password.error = getString(R.string.error_invalid_password)
        password.requestFocus()
        login_progress.visibility = View.GONE
    }

    override fun onSuccess() {
        login_progress.visibility = View.GONE
        Intent(context, MainActivity::class.java).apply {
            startActivity(this)
            activity?.finish()
        }
    }

    override fun onFailure(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        login_progress.visibility = View.GONE
    }

}
