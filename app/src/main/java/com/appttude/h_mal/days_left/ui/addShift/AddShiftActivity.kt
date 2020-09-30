package com.appttude.h_mal.days_left.ui.addShift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModel
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AddShiftActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<ShiftsViewModelFactory>()
    private val viewModel by viewModels<ShiftsViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shift)
        viewModel

        //set toolbar
        setSupportActionBar(toolbar)

        intent.extras?.takeIf { !it.isEmpty }?.let {

        }
    }

    private fun getStackCount(): Int {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment?
        return navHostFragment!!.childFragmentManager.backStackEntryCount
    }

    override fun onBackPressed() {
        if (getStackCount() == 0){
            AlertDialog.Builder(this)
                .setTitle("Leave?")
                .setMessage("Discard current shift? 1122")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes) { _, _ -> finish() }
                .create().show()
        }else{
            super.onBackPressed()
        }
    }
}