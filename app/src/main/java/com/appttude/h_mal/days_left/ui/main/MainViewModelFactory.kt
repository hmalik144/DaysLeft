package com.appttude.h_mal.days_left.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appttude.h_mal.days_left.data.repository.FirebaseRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class MainViewModelFactory(
    private val firebaseRepository: FirebaseRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return (
                    MainViewModel(
                        firebaseRepository
                    )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
