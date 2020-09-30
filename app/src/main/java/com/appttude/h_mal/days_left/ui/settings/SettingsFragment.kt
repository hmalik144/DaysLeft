package com.appttude.h_mal.days_left.ui.settings

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.login.AuthViewModel
import com.appttude.h_mal.days_left.ui.main.MainViewModel
import com.appttude.h_mal.days_left.utils.navigateTo

class SettingsFragment : PreferenceFragmentCompat() {

    val viewModel by activityViewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("email")?.setOnPreferenceClickListener {
            view?.navigateTo(R.id.to_changeEmailFragment)
            true
        }
        findPreference<Preference>("password")?.setOnPreferenceClickListener {
            view?.navigateTo(R.id.to_changePasswordFragment)
            true
        }
        findPreference<Preference>("name")?.setOnPreferenceClickListener {
            view?.navigateTo(R.id.to_changeNameFragment)
            true
        }
        findPreference<Preference>("log_out")?.setOnPreferenceClickListener {
            viewModel.logout()
            view?.navigateTo(R.id.mainTo_loginHomeFragment)
            activity?.finish()
            true
        }
    }
}