package com.appttude.h_mal.days_left.ui.main.list

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.appttude.h_mal.days_left.data.prefs.PreferenceSource
import com.appttude.h_mal.days_left.ui.login.AuthViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class SortationDialog(
    context: Context
): AlertDialog.Builder(context), KodeinAware{
    override val kodein by kodein(context)
    private val prefs by instance<PreferenceSource>()

    val selectionPair = prefs.loadCurrentSortation()

    private val groupName = arrayOf("Name", "Date Added", "Date of shift")
    var checkedItem = selectionPair.second

//    var sortation = ShiftListRecyclerAdapter.Sortation()
//
//    init {
//        setTitle("Sort by:")
//        setSingleChoiceItems(groupName, checkedItem) { _, item ->
//            checkedItem = item
//            when (item) {
//                0 -> {
//                    sortation = ShiftListRecyclerAdapter.Sortation.Name()
//                }
//                1 -> {
//                    sortation = ShiftListRecyclerAdapter.Sortation.DateAdded()
//                    fireAdapter.filterShifts(ShiftListRecyclerAdapter.Sortation.DateAdded())
//                }
//                2 ->{
//                    fireAdapter.filterShifts(ShiftListRecyclerAdapter.Sortation.ShiftDate())
//                }
//            }
//        }
//        setPositiveButton("Ascending") { dialog, _ ->
//
//            dialog.dismiss()
//        }
//        setNegativeButton("Descending") { dialog, _ ->
//
//            dialog.dismiss()
//        }
//
//        create().show()
//    }
//
//    private fun sortData() {
//
//
//        android.app.AlertDialog.Builder(context).apply {
//            setTitle("Sort by:")
//            setSingleChoiceItems(groupName, checkedItem) { dialog, item ->
//                when (item) {
//                    0 -> {
//                        fireAdapter.filterShifts(ShiftListRecyclerAdapter.Sortation.Name())
//                    }
//                    1 -> {
//                        fireAdapter.filterShifts(ShiftListRecyclerAdapter.Sortation.DateAdded())
//                    }
//                    2 ->{
//                        fireAdapter.filterShifts(ShiftListRecyclerAdapter.Sortation.ShiftDate())
//                    }
//                }
//                dialog.dismiss()
//            }
//            create().show()
//        }
//
//    }
}