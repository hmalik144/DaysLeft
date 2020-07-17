package com.appttude.h_mal.days_left.application

import android.app.Application
import com.appttude.h_mal.days_left.ui.login.AuthViewModelFactory
import com.appttude.h_mal.days_left.data.firebase.FirebaseAuthSource
import com.appttude.h_mal.days_left.data.firebase.FirebaseDataSource
import com.appttude.h_mal.days_left.data.repository.UserRepository
import com.appttude.h_mal.days_left.ui.main.MainViewModelFactory
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppClass : Application(), KodeinAware{

    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppClass))

        bind() from singleton { FirebaseAuthSource() }
        bind() from singleton { FirebaseDataSource() }
        bind() from singleton {
            UserRepository(
                instance()
            )
        }
        bind() from provider { MainViewModelFactory( instance() ) }
        bind() from provider {
            ShiftsViewModelFactory( instance() )
        }
        bind() from provider {
            AuthViewModelFactory( instance() )
        }
    }
}