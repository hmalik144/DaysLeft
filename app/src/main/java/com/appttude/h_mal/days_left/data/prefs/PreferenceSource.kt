package com.appttude.h_mal.days_left.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager

const val SORT_ORDER_CONST = "sort_order"
const val DIR_ORDER_CONST = "direction_order"
class PreferenceSource(
    context: Context
) {

    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveCurrentSortation(sort: String, direction: Int){
        preferences.edit()
            .putString(SORT_ORDER_CONST, sort)
            .putInt(DIR_ORDER_CONST, direction)
            .apply()
    }

    fun loadCurrentSortation(): Pair<String?, Int>{
        return Pair(
            preferences.getString(SORT_ORDER_CONST, null),
            preferences.getInt(DIR_ORDER_CONST, -1)
        )
    }

    fun livePair(): LiveData<Pair<String?, Int>> {
        return object: LiveData<Pair<String?, Int>>() {
            val listener =
                SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    if (key == SORT_ORDER_CONST || key == DIR_ORDER_CONST)
                        postValue(loadCurrentSortation())
                }
            override fun onActive() {
                super.onActive()
                preferences.registerOnSharedPreferenceChangeListener(listener)
            }

            override fun onInactive() {
                preferences.unregisterOnSharedPreferenceChangeListener(listener)
                super.onInactive()
            }
        }
    }
}
