package com.appttude.h_mal.days_left.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appttude.h_mal.days_left.data.repository.FirebaseRepository
import com.google.gson.Gson

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ShiftsViewModelFactory(
    private val firebaseDatabase: FirebaseRepository,
    private val gson: Gson
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftsViewModel::class.java)) {
            return (
                    ShiftsViewModel(
                        firebaseDatabase,
                        gson
                    )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
