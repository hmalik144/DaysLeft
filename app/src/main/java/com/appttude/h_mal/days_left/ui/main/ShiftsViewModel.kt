package com.appttude.h_mal.days_left.ui.main

import androidx.lifecycle.ViewModel
import com.appttude.h_mal.days_left.FirebaseDatabase
import com.appttude.h_mal.days_left.firebaseLiveData.FirebaseQueryLiveData

class ShiftsViewModel(
    val database: FirebaseDatabase
): ViewModel() {

    private val shifts = database.allShifts()
    val shiftData = FirebaseQueryLiveData(shifts)

}