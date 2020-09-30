package com.appttude.h_mal.days_left.firebaseUtils

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.gson.reflect.TypeToken


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

@Suppress("UNCHECKED_CAST")
class FirebaseQueryLiveDataTyped<T: Any>(
    var query: Query
): LiveData<T>(),ValueEventListener {


    override fun onActive() {
        query.addValueEventListener(this)
    }

    override fun onInactive() {
        query.removeEventListener(this)
    }

    override fun onDataChange(p0: DataSnapshot) {
        val cls = p0.getValue(object: TypeToken<T>() {}.rawType)
        postValue(cls as T)
    }

    override fun onCancelled(p0: DatabaseError) {
        Log.e(this.toString(),p0.message)
    }

}

@Suppress("UNCHECKED_CAST")
class FirebaseQueryLiveDataTyped2<T: Any>(
    var query: Query,
    val clazz: Class<T>
): LiveData<T>(),ValueEventListener {

    override fun onActive() {
        query.addValueEventListener(this)
    }

    override fun onInactive() {
        query.removeEventListener(this)
    }

    override fun onDataChange(p0: DataSnapshot) {
        val cls = p0.getValue(clazz)
        postValue(cls as T)
    }

    override fun onCancelled(p0: DatabaseError) {
        Log.e(this.toString(), p0.message)
    }

}

class FirebaseQueryLiveDataList<T: Any>(
    var query: Query,
    val type: Class<T>
): LiveData<List<T>>(),ValueEventListener {

    fun updateQuery(query: Query){
        this.query = query
        onActive()
    }

    override fun onActive() {
        query.addValueEventListener(this)
    }

    override fun onInactive() {
        query.removeEventListener(this)
    }

    override fun onDataChange(p0: DataSnapshot) {
        val cls = p0.children.map { it.getValue(type)!! }
        postValue(cls)
    }

    override fun onCancelled(p0: DatabaseError) {
        Log.e(this.toString(),p0.message)
    }

}

@Suppress("UNCHECKED_CAST")
class FirebaseQueryLiveDataMap<T: Any>(
    var query: Query,
    val type: Class<T>
): LiveData<List<Pair<String, T>>>(),ValueEventListener {

    fun updateQuery(query: Query){
        this.query = query
        onActive()
    }

    override fun onActive() {
        query.addValueEventListener(this)
    }

    override fun onInactive() {
        query.removeEventListener(this)
    }

    override fun onDataChange(p0: DataSnapshot) {
        val cls = p0.children.map { item -> Pair(item.key!!, item.getValue(type)!!) }
        postValue(cls)
    }

    override fun onCancelled(p0: DatabaseError) {
        Log.e(this.toString(),p0.message)
    }

}