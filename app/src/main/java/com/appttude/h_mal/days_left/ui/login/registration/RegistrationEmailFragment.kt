package com.appttude.h_mal.days_left.ui.login.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.models.registration.RegistrationArgs
import com.appttude.h_mal.days_left.utils.afterTextChanged
import com.appttude.h_mal.days_left.utils.isEmailValid
import com.appttude.h_mal.days_left.utils.isNameValid
import com.appttude.h_mal.days_left.utils.navigateTo
import kotlinx.android.synthetic.main.fragment_registration_two.*


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationEmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationEmailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_two, container, false)
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
        val email = submission_et.text.toString()
        if (!isEmailValid(email)){
            til_submission.error = "Enter a valid Email"
            return
        }

        val regArgs =
            RegistrationEmailFragmentArgs.fromBundle(requireArguments()).regOneArgs
        regArgs.email = email

        val action =
            RegistrationEmailFragmentDirections.toRegistrationPasswordFragment(regArgs)

        view?.navigateTo(action)
    }


}