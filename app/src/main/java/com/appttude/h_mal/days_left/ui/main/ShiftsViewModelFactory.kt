package com.appttude.h_mal.days_left.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appttude.h_mal.days_left.FirebaseDatabase

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ShiftsViewModelFactory(
    private val firebaseDatabase: FirebaseDatabase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftsViewModel::class.java)) {
            return (
                    ShiftsViewModel(
                        firebaseDatabase
                    )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
