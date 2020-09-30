package com.appttude.h_mal.days_left.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.utils.hide
import com.appttude.h_mal.days_left.utils.show
import com.appttude.h_mal.days_left.utils.showToast
import kotlinx.android.synthetic.main.activity_fullscreen.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class FullscreenActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()
    private val viewModel: AuthViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        viewModel.operationState.observe(this, Observer {
            if (it){
                progress_circular.show()
            }else{
                progress_circular.hide()
            }
        })

        viewModel.operationError.observe(this, Observer {
            it.getContentIfNotHandled()?.let { message ->
                showToast(message)
            }
        })
    }

    // If the there is more than 1 fragment in the backstack
    // Go back to previous fragment
    // Or exit
    override fun onBackPressed() {
        container.childFragmentManager.backStackEntryCount.let {
            if (it > 0) {
                super.onBackPressed()
            } else {
                finish()
            }
        }
    }

}
