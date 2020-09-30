package com.appttude.h_mal.days_left.ui.login.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.login.AuthViewModel
import com.appttude.h_mal.days_left.utils.afterTextChanged
import com.appttude.h_mal.days_left.utils.navigateTo
import kotlinx.android.synthetic.main.fragment_registration_four.*


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFourFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationConfirmationFragment : Fragment() {
    val viewmodel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_four, container, false)
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

        viewmodel.operationRegister.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                view.navigateTo(R.id.registration_to_mainActivity)
            }
        })
    }

    fun next(){
        val password = submission_et.text.toString()
        val regArgs =
            RegistrationConfirmationFragmentArgs.fromBundle(requireArguments()).regThreeArgs
        val previousPassword = regArgs.password!!
        if (password != previousPassword){
            til_submission.error = "Passwords do not match"
            return
        }

        regArgs.run {
            viewmodel.register(
                email!!, previousPassword, name!!
            )
        }
    }

}