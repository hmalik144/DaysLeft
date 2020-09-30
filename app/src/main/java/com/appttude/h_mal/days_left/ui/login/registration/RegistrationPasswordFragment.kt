package com.appttude.h_mal.days_left.ui.login.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.utils.afterTextChanged
import com.appttude.h_mal.days_left.utils.isPasswordValid
import com.appttude.h_mal.days_left.utils.navigateTo
import kotlinx.android.synthetic.main.fragment_registration_password.*


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationPasswordFragment] factory method to
 * create an instance of this fragment.
 */
class RegistrationPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submission_et.apply {
            afterTextChanged {
                til_submission.error = null
            }

            setOnEditorActionListener { textView, id, _ ->
                if (id == EditorInfo.IME_ACTION_DONE) {
                    next()
                    return@setOnEditorActionListener true
                }
                false
            }
        }

        submission_button.setOnClickListener { next() }
    }

    fun next(){
        val password = submission_et.text.toString()
        if (!isPasswordValid(password)){
            til_submission.error = "Enter a valid Email"
            return
        }

        val regArgs =
            RegistrationPasswordFragmentArgs.fromBundle(requireArguments()).regTwoArgs
        regArgs.password = password

        val action =
            RegistrationPasswordFragmentDirections.toRegistrationConfirmationFragment(regArgs)

        view?.navigateTo(action)
    }

}