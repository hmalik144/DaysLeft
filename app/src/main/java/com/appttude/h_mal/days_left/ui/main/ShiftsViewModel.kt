package com.appttude.h_mal.days_left.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.appttude.h_mal.days_left.data.firebase.FirebaseDataSource
import com.appttude.h_mal.days_left.firebaseUtils.FirebaseQueryLiveData
import com.appttude.h_mal.days_left.models.ShiftObject
import com.google.firebase.database.DataSnapshot
import java.util.HashSet

class ShiftsViewModel(
    val database: FirebaseDataSource
): ViewModel() {

    val shifts = database.allShifts()
    val shiftData = FirebaseQueryLiveData(shifts)

    val liveData = MutableLiveData<Triple<List<ShiftObject?>, Int, IntArray>>()

    init {
        shiftData.observeForever {
            val list = it.mapSnapToShiftList()
            val uniqueEntries = countDistinct(list)
            val shiftTypeCount = countShiftType(list)

            liveData.value = Triple(
                list,
                uniqueEntries,
                shiftTypeCount
            )
        }
    }

    private fun countDistinct(list: List<ShiftObject?>): Int {
        return  list.distinctBy { it?.shiftDate }.size
    }

    private fun countShiftType(list: List<ShiftObject?>): IntArray {
        val i = list.filter { it?.taskObject?.workType.equals("Hourly")  }.size
        val j = list.size - i

        return intArrayOf(i, j)
    }

    private fun DataSnapshot.mapSnapToShiftList(): List<ShiftObject?> = this.children.map {
        it.getValue(ShiftObject::class.java)
    }

//    private fun calculateAccumulatedPay(type: Int): Float {
//        var pay = 0f
//
//        for (shiftObject in shiftList) {
//            shiftObject?.let {
//                when (type){
//                    0 -> {
//                        if (it.taskObject?.workType == "Hourly") {
//                            pay += it.taskObject?.rate?.times((it.timeObject!!.hours - it.timeObject!!.breakEpoch))
//                                ?: pay
//                        }
//                    }
//                    1 -> {
//                        if (it.taskObject?.workType == "Piece Rate") {
//                            pay += it.taskObject?.rate?.times(it.unitsCount!!) ?: pay
//                        }
//                    }
//                    else -> {
//                        if (it.taskObject?.workType == "Hourly") {
//                            pay += it.taskObject?.rate?.times((it.timeObject!!.hours - it.timeObject!!.breakEpoch))
//                                ?: pay
//                        } else {
//                            pay += it.taskObject?.rate?.times(it.unitsCount!!) ?: pay
//                        }
//                    }
//                }
//            }
//        }
//
//        return pay
//    }

    private fun totalPay(): Float{


        return 0.0f
    }

}