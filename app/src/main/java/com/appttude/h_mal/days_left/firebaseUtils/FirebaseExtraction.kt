package com.appttude.h_mal.days_left.firebaseUtils

import com.google.android.gms.tasks.Task
import java.io.IOException

abstract class FirebaseExtraction {

    fun <T : Any> safeFirebaseResult(
        call: () -> Task<T>
    ): T {
        val task = call.invoke()

        if (!task.isSuccessful) {
            throw task.exception ?: IOException("Failed to complete firebase task")
        } else {
            return task.result!!
        }
    }

}