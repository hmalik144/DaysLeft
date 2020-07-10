package com.appttude.h_mal.days_left

import com.appttude.h_mal.days_left.FirebaseClass.Companion.auth
import com.appttude.h_mal.days_left.FirebaseClass.Companion.mDatabase

const val USER_FIREBASE = "users"
val EMPLOYER_FIREBASE = "employers"
const val  SHIFT_FIREBASE = "shifts"
val TASK_FIREBASE = "taskList"

val SHIFT_ID = "shift_id"

val PIECE = "Piece Rate"
val HOURLY = "Hourly"
class FirebaseDatabase{

    fun allShifts() = mDatabase.child(USER_FIREBASE).child(auth.uid!!).child(SHIFT_FIREBASE)
    fun child(shiftId: String) = mDatabase.child(USER_FIREBASE).child(auth.uid!!).child(SHIFT_FIREBASE).child(shiftId)

    fun taskObject(abn: String) = mDatabase.child(FirebaseClass.EMPLOYER_FIREBASE).child(abn).child(
        FirebaseClass.TASK_FIREBASE
    )
}