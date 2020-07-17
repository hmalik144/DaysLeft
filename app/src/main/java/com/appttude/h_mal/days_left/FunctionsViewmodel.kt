package com.appttude.h_mal.days_left

import android.util.Log
import androidx.lifecycle.ViewModel
import com.appttude.h_mal.days_left.data.firebase.FirebaseFunctionsSource
import com.appttude.h_mal.days_left.models.AbnObject
import com.appttude.h_mal.days_left.utils.genericType
import com.google.gson.Gson
import java.io.IOException
import java.lang.Exception

class FunctionsViewmodel(
    val functions: FirebaseFunctionsSource
) : ViewModel(){

    fun getAbnList(input: String){
        try {
            functions.getAbnList(input).continueWith{ task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.
                val result= task.result?.data

                result
            }.continueWith {
                if (!it.isSuccessful){
                    throw it.exception ?: IOException("Unable to retrieve Abn details")
                }

                val json = Gson().toJson(it)
                val type = genericType<List<AbnObject>>()
                Gson().fromJson<List<AbnObject>>(json, type)
            }.addOnCompleteListener {
                if (!it.isSuccessful){
                    throw it.exception ?: IOException("Unable to retrieve Abn details")
                }
                val list = it.result
            }
        }catch (e: Exception){
            e.cause?.printStackTrace()
            e.message?.let {

            }
        }

    }
}