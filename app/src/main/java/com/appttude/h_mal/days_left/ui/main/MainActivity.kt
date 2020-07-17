package com.appttude.h_mal.days_left.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.appttude.h_mal.days_left.*
import com.appttude.h_mal.days_left.ui.login.FullscreenActivity
import com.appttude.h_mal.days_left.FirebaseClass.Companion.SHIFT_FIREBASE
import com.appttude.h_mal.days_left.FirebaseClass.Companion.USER_FIREBASE
import com.appttude.h_mal.days_left.FirebaseClass.Companion.auth
import com.appttude.h_mal.days_left.FirebaseClass.Companion.mDatabase
import com.appttude.h_mal.days_left.models.ShiftObject
import com.appttude.h_mal.days_left.ui.main.home.HomeFragment
import com.appttude.h_mal.days_left.ui.main.list.FragmentList
import com.appttude.h_mal.days_left.ui.main.tools.FragmentTools
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_drawer_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<MainViewModelFactory>()
    private val factory2 by instance<ShiftsViewModelFactory>()
    val viewModel: MainViewModel by viewModels{ factory }

    companion object{
        var shiftList = ArrayList<ShiftObject>()
        val ref = mDatabase.child(USER_FIREBASE).child(auth.uid as String).child(SHIFT_FIREBASE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_main)

        ViewModelProvider(this, factory2).get(ShiftsViewModel::class.java)

        //setup backstack change listener
        supportFragmentManager.addOnBackStackChangedListener(backStackChangedListener)
        //set toolbar
        setSupportActionBar(toolbar)
        //setup fab
        fab.setOnClickListener{
            val intent = Intent(this, AddShiftActivity::class.java)
            startActivity(intent)
        }

        //setup drawer layout
        val toggle = ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //apply navigation listener on bottom bar navigation view
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //setup naviation view
        nav_view.setNavigationItemSelectedListener { menuItem ->
            menuItem.itemId.let { id ->
                if (id == R.id.nav_camera){
                    val intent = Intent(this, ChangeUserDetailsActivity::class.java)
                    startActivity(intent)
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

        //Setup drawer
        setupDrawer(nav_view)

        //initialise data for fragments
        supportFragmentManager.beginTransaction().replace(R.id.container,
            HomeFragment()
        ).commit()
        navigation.id = R.id.navigation_home
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.container,
                    HomeFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_list -> {
                supportFragmentManager.beginTransaction().replace(R.id.container,
                    FragmentList()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tools -> {
                supportFragmentManager.beginTransaction().replace(R.id.container,
                    FragmentTools()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val backStackChangedListener = FragmentManager.OnBackStackChangedListener {
        supportFragmentManager.fragments[0].javaClass.simpleName.let { fragmentName ->

            when (fragmentName){
                "HomeFragment" -> {
                    title = "Home"
                }
                "ListFragment" -> {
                    title = "List"
                }
                "ToolsFragment" -> {
                    title = "Tools"
                }
                else -> {
                    title = getString(R.string.app_name)
                }
            }
        }
    }

    private fun setupDrawer(navigationView: NavigationView){
        val header: View = navigationView.getHeaderView(0)

        viewModel.getName()?.let {
            header.driver_name.text = it

        }

        viewModel.getEmail()?.let {
            header.driver_email.text = it
        }

        viewModel.getPhotoUrl()?.let {
            Picasso.get().load(it).placeholder(R.mipmap.ic_launcher_round)
                .into(header.profileImage)
        }

        logout.setOnClickListener{
            viewModel.logout()
            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



//    fun initiateFragment(){
//        progressBar2.visibility = View.VISIBLE
//
//        ref.keepSynced(true)
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                progressBar2.visibility = View.GONE
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                progressBar2.visibility = View.GONE
//
//                if(shiftList.isNotEmpty()){
//                    shiftList.clear()
//                }
//
//                for(postSnapshot in p0.children){
//                    shiftList.add(postSnapshot.getValue(ShiftObject::class.java)!!)
//                }
//
//                Log.i("firebase", "shiftlist count = " + shiftList.size)
//
//                if (shiftList.size > 0){
//                    if(fragmentManager.fragments.size == 0){
//                        //apply navigation listener on bottom bar navigation view
//                        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
//                        //add first fragment
//                        fragmentManager.beginTransaction().replace(R.id.container,HomeFragment()).commit()
//                        navigation.setSelectedItemId(R.id.navigation_home)
//                    }
//                }else{
//                    Toast.makeText(getBaseContext() , "Cannot load data", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        })
//    }

}
