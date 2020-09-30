package com.appttude.h_mal.days_left.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.login.AuthViewModel
import com.appttude.h_mal.days_left.ui.login.AuthViewModelFactory
import com.appttude.h_mal.days_left.utils.setVis
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()
    private val factory2 by instance<ShiftsViewModelFactory>()
    val viewModel: AuthViewModel by viewModels{ factory }

    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewModelProvider(this, factory2).get(ShiftsViewModel::class.java)
        //set toolbar
        setSupportActionBar(toolbar)
        // initiate nav host for android navigation
        val navHost = supportFragmentManager
            .findFragmentById(R.id.container) as NavHostFragment
        // instantiate controller for nav host
        navController = navHost.navController
        // setup bottom bar with tabs
        setupBottomBar()
        // progress view for async operations
        viewModel.operationState.observe(this, Observer {
            progressBar2.setVis(it)
        })
    }

    private fun setupBottomBar(){
        val tabs = setOf(R.id.navigation_home, R.id.navigation_list, R.id.navigation_tools)
        val appBarConfiguration = AppBarConfiguration(tabs)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigation.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.nav_settings ->{
                NavigationUI.onNavDestinationSelected(item, navController)
                true
            }
            else -> {
                false
            }
        }
    }

}
