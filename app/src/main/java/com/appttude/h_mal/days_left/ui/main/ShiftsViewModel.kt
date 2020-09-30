package com.appttude.h_mal.days_left.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appttude.h_mal.days_left.data.repository.FirebaseRepository
import com.appttude.h_mal.days_left.firebaseUtils.FirebaseQueryLiveDataList
import com.appttude.h_mal.days_left.firebaseUtils.FirebaseQueryLiveDataMap
import com.appttude.h_mal.days_left.firebaseUtils.FirebaseQueryLiveDataTyped2
import com.appttude.h_mal.days_left.models.AbnObject
import com.appttude.h_mal.days_left.models.ShiftObject
import com.appttude.h_mal.days_left.models.TaskObject
import com.appttude.h_mal.days_left.utils.Event
import com.appttude.h_mal.days_left.utils.getResults
import com.appttude.h_mal.days_left.utils.safeFirebaseResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShiftsViewModel(
    val repository: FirebaseRepository,
    val gson: Gson
) : ViewModel() {

    // Live date of operation success
    val operationState = MutableLiveData<Boolean>()
    val operationResult = MutableLiveData<Event<Pair<Boolean, String?>>>()

    val shifts = repository.getShiftList()

    val shiftData = FirebaseQueryLiveDataMap(shifts, ShiftObject::class.java)
    val liveData = MutableLiveData<Triple<List<ShiftObject?>, Int, IntArray>>()

    val currentShift = MutableLiveData<ShiftObject>()

    init {
        shiftData.observeForever {
            val shifts = it.map { item -> item.second }
            val uniqueEntries = countDistinct(shifts)
            val shiftTypeCount = countShiftType(shifts)

            liveData.value = Triple(
                shifts,
                uniqueEntries,
                shiftTypeCount
            )
        }
    }

    fun retrieveSingleShift(id: String){
        repository.getSingleShift(id).getResults { dataSnapshot ->
            dataSnapshot?.getValue(ShiftObject::class.java)?.let {
                currentShift.postValue(it)
            }
        }
    }

    fun getSingleShift(id: String): FirebaseQueryLiveDataTyped2<ShiftObject> {
        return FirebaseQueryLiveDataTyped2<ShiftObject>(
            repository.getSingleShift(id),
            ShiftObject::class.java
        )
    }

    fun getTasks(abn: String): FirebaseQueryLiveDataList<TaskObject> {
        return FirebaseQueryLiveDataList(repository.tasks(abn), TaskObject::class.java)
    }

    fun deleteShift(id: String) {
        repository.deleteSingleShift(id)
    }

    fun updateShift(id: String, shiftObject: ShiftObject) {
        operationState.postValue(true)
        repository.updateShift(id, shiftObject).safeFirebaseResult({
            operationResult.postValue(Event(Pair(true, "Update Successful")))
        }, {
            operationResult.postValue(Event(Pair(false, it)))
        }, {
            operationState.postValue(false)
        })
    }

    fun postNewShift(shiftObject: ShiftObject) {
        operationState.postValue(true)
        repository.postShift(shiftObject).safeFirebaseResult({
            operationResult.postValue(Event(Pair(true, "Post Successful")))
        }, {
            operationResult.postValue(Event(Pair(false, it)))
        }, {
            operationState.postValue(false)
        })
    }

    fun getMyEmployers(): LiveData<MutableList<AbnObject>> {
        val query = repository.getRecentEmployers()

        return object : LiveData<MutableList<AbnObject>>(), ValueEventListener {
            override fun onActive() {
                query.addValueEventListener(this)
            }

            override fun onInactive() {
                query.removeEventListener(this)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.mapNotNull { it.key }
                Log.i("GetEmployers", "size = ${data.size}")
                clearData()
                if (data.isEmpty()) return

                data.forEach { item ->
                    repository.getEmployer(item).getResults { ds ->
                        ds?.getValue(AbnObject::class.java)?.let { abn ->
                            Log.i("GetEmployers", "abn = ${abn.abn}")
                            addItems(abn)
                        }
                    }
                }
            }

            fun <T> LiveData<MutableList<T>>.addItems(item: T) {
                val updatedItems = this.value ?: mutableListOf()
                updatedItems.add(item)
                this.value = updatedItems
            }

            fun <T> LiveData<MutableList<T>>.clearData() {
                this.value = mutableListOf()
            }

            override fun postValue(value: MutableList<AbnObject>?) {
                Log.i("GetEmployers", "posted size  = ${value?.size}")
                super.postValue(value)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
    }

    fun searchForAbn(
        input: String,
        output: ((List<AbnObject>) -> Unit)
    ){
        operationState.postValue(true)
        repository.getAbnList(input).safeFirebaseResult({
            val list = it?.data as ArrayList<*>
            val abnList = list.map { item ->
                convertAny<AbnObject>(item)
            }
            output.invoke(abnList)
        },{
            operationResult.postValue(Event(Pair(false, it)))
        },{
            operationState.postValue(false)
        })
    }

    private inline fun <reified T> convertAny(item: Any): T {
        val type = object: TypeToken<T>() {}.type
        val itemAsString = gson.toJson(item)
        return gson.fromJson<T>(itemAsString, type)
    }

    fun setAddShift(shiftObject: ShiftObject){
        val updatedItems = currentShift.value ?: ShiftObject()

        shiftObject.apply {
            abnObject?.let {
                updatedItems.abnObject = it
            }
            taskObject?.let {
                updatedItems.taskObject = it
            }
            timeObject?.let {
                updatedItems.timeObject = it
            }
            unitsCount?.let {
                updatedItems.unitsCount = it
            }
            shiftDate?.let {
                updatedItems.shiftDate = it
            }
        }
        currentShift.postValue(updatedItems)
    }

    private fun countDistinct(list: List<ShiftObject?>): Int {
        return list.distinctBy { it?.shiftDate }.size
    }

    private fun countShiftType(list: List<ShiftObject?>): IntArray {
        val i = list.filter { it?.taskObject?.workType.equals("Hourly") }.size
        val j = list.size - i

        return intArrayOf(i, j)
    }


}