package com.appttude.h_mal.days_left.application

import android.app.Application
import com.appttude.h_mal.days_left.data.firebase.FirebaseAuthSource
import com.appttude.h_mal.days_left.data.firebase.FirebaseDataSource
import com.appttude.h_mal.days_left.data.firebase.FirebaseFunctionsSource
import com.appttude.h_mal.days_left.data.prefs.PreferenceSource
import com.appttude.h_mal.days_left.data.repository.FirebaseRepository
import com.appttude.h_mal.days_left.helper.ShiftHelper
import com.appttude.h_mal.days_left.ui.login.AuthViewModelFactory
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModelFactory
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppClass : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppClass))

        bind() from singleton { Gson() }
        bind() from singleton { PreferenceSource(instance()) }
        bind() from singleton { FirebaseAuthSource() }
        bind() from singleton { FirebaseDataSource() }
        bind() from singleton { FirebaseFunctionsSource() }
        bind() from singleton {
            FirebaseRepository(
                instance(),
                instance(),
                instance()
            )
        }
        bind() from singleton { ShiftHelper() }
//        bind() from provider { MainViewModelFactory( instance() ) }
        bind() from provider {
            ShiftsViewModelFactory(instance(), instance())
        }
        bind() from provider {
            AuthViewModelFactory(instance())
        }
    }
}