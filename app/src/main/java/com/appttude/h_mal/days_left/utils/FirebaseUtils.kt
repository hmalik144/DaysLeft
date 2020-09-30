package com.appttude.h_mal.days_left.utils

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.io.IOException

fun <T : Any> Task<T?>.safeFirebaseResult(): T? {
    if (!isSuccessful) {
        throw exception ?: IOException("Failed to complete firebase task")
    } else {
        return result!!
    }
}

fun <T : Any> Task<T>.safeFirebaseResult(
    success: ((T?) -> Unit),
    failure: ((String) -> Unit),
    complete: (() -> Unit)
){
    this.addOnSuccessListener {
        success(it)
        complete()
    }.addOnFailureListener {
        it.printStackTrace()
        failure(it.message!!)
        complete()
    }
}

fun Exception.firebaseError(): String?{
    cause?.printStackTrace()
    return message
}

fun DatabaseReference.getResults(
    result: ((DataSnapshot?) -> Unit)
){
    addListenerForSingleValueEvent(object : ValueEventListener{
        override fun onCancelled(error: DatabaseError) {
            result.invoke(null)
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            result.invoke(snapshot)
        }
    })
}