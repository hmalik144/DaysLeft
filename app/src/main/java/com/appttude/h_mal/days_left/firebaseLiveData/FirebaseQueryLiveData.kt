package com.appttude.h_mal.days_left.firebaseLiveData

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener


class FirebaseQueryLiveData(
    var query: Query
): LiveData<DataSnapshot>(),ValueEventListener {

    override fun onActive() {
        query.addValueEventListener(this)
    }

    override fun onInactive() {
        query.removeEventListener(this)
    }

    override fun onDataChange(p0: DataSnapshot) {
        postValue(p0)
    }

    override fun onCancelled(p0: DatabaseError) {
        Log.e(this.toString(),p0.message)
    }


}