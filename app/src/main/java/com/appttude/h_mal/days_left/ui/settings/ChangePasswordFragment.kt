package com.appttude.h_mal.days_left.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.login.AuthViewModel
import com.appttude.h_mal.days_left.utils.clearError
import com.appttude.h_mal.days_left.utils.showToast
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : Fragment() {

    val viewModel by activityViewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submission_button.setOnClickListener {
            viewModel.updateEmailAddress(
                submission_et.text.toString(),
                submission_password_et.text.toString(),
                submission_password_again_et.text.toString()
            )
        }

        viewModel.operationError.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { res ->
                context?.showToast(res)
//                ResultDialog(requireContext(), res) {}
            }
        })

        viewModel.updateForm.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { form ->
                form.emailError?.let { emailError ->
                    til_submission.error = getString(emailError)
                }
                form.passwordError?.let { passwordError ->
                    til_submission_password.error = getString(passwordError)
                }
                form.newPasswordError?.let { passwordError ->
                    til_submission_password_again.error = getString(passwordError)
                }
            }
        })

        submission_et.setOnClickListener(listener)
        submission_password_et.setOnClickListener(listener)
        submission_password_again_et.setOnClickListener(listener)
    }

    private val listener = View.OnClickListener {
        til_submission.clearError()
        til_submission_password.clearError()
        til_submission_password_again.clearError()
    }
}