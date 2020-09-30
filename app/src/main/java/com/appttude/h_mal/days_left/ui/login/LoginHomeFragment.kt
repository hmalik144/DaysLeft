package com.appttude.h_mal.days_left.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.utils.navigateTo
import kotlinx.android.synthetic.main.fragment_login_home.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class LoginHomeFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_up.setOnClickListener(this)
        submission_button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.sign_up -> R.id.to_registrationNicknameFragment
            R.id.submission_button -> R.id.splash_to_loginFragment
            else ->{
                null
            }
        }?.let {
            view?.navigateTo(it)
        }
    }
}