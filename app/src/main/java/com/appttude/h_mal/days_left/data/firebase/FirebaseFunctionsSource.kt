package com.appttude.h_mal.days_left.data.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import java.util.HashMap

class FirebaseFunctionsSource {

    private val functions: FirebaseFunctions by lazy {
        FirebaseFunctions.getInstance()
    }

    fun getAbnList(input: String): Task<HttpsCallableResult> {
        val data = HashMap<String, Any>()
        data["input"] = input
        data["push"] = true

        return functions
            .getHttpsCallable("abnLooKUp")
            .call(data)
    }

    fun getExcel(): Task<HttpsCallableResult> {
        // Create the arguments to the callable function.
        val data = HashMap<String, Any>()
        data["push"] = true

        return functions
            .getHttpsCallable("writeFireToExcelVisa")
            .call(data)
    }
}