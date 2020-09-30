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
import com.appttude.h_mal.days_left.utils.navigateTo
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein


class LoginFragment : Fragment(), KodeinAware {
    override val kodein by kodein()

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        password.apply {
            setOnEditorActionListener { textView, id, _ ->
                if (id == EditorInfo.IME_ACTION_DONE) {
                    viewModel.login(
                        submission_et.text.toString(),
                        textView.text.toString()
                    )
                    return@setOnEditorActionListener true
                }
                false
            }

            afterTextChanged {
                til_password.error = null
            }
        }

        submission_et.apply {
            afterTextChanged {
                til_submission.error = null
            }
        }

        submission_button.setOnClickListener {
            viewModel.login(
                submission_et.text.toString(),
                password.text.toString()
            )
        }

        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            if (loginState.usernameError != null) {
                til_submission.error = getString(loginState.usernameError)

            }
            if (loginState.passwordError != null) {
                til_password.error = getString(loginState.passwordError)
            }
        })

        viewModel.operationLogin.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                view.navigateTo(R.id.LoginTo_mainActivity)
            }
        })
    }

}
