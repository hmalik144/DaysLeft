package com.appttude.h_mal.days_left.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.FirebaseClass.Companion.auth
import com.google.firebase.auth.FirebaseAuth

class FullscreenActivity : AppCompatActivity() {
    companion object {
        lateinit var fragmentManagerLogin : FragmentManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        auth = FirebaseAuth.getInstance()

        fragmentManagerLogin = supportFragmentManager

        fragmentManagerLogin.beginTransaction().replace(
            R.id.container,
            SplashFragment()
        ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragmentManagerLogin.fragments.size > 1) {
            fragmentManagerLogin.popBackStack()
        }
    }


}
