package com.appttude.h_mal.days_left.data.firebase

import com.appttude.h_mal.days_left.utils.*
import com.google.firebase.database.FirebaseDatabase

class FirebaseDataSource{

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private val mDatabase = firebaseDatabase.reference
    val usersRef = mDatabase.child(USER_FIREBASE)
    val employersRef = mDatabase.child(EMPLOYER_FIREBASE)

    fun allShifts(uid: String) = usersRef.child(uid).child(
        SHIFT_FIREBASE
    )

    fun shiftsRef(uid: String) = usersRef.child(uid).child(SHIFT_FIREBASE)

    fun singleShift(uid: String, shiftId: String) = shiftsRef(uid).child(shiftId)

    fun deleteItem(uid: String, shiftId: String){
        mDatabase.child(USER_FIREBASE).child(uid).child(
            SHIFT_FIREBASE
        ).child(shiftId).removeValue()
    }

    fun taskObject(abn: String) = mDatabase.child(EMPLOYER_FIREBASE)
        .child(abn).child(TASK_LIST)

    fun recentEmployers(uid: String) = usersRef.child(uid).child(RECENT_EMPLOYERS)

    fun getEmployer(abn: String) = employersRef.child(abn)
}