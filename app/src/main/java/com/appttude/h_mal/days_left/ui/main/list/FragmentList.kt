package com.appttude.h_mal.days_left.ui.main.list

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.addShift.AddNewShiftActivity
import com.appttude.h_mal.days_left.ui.addShift.AddShiftActivity
import com.appttude.h_mal.days_left.ui.addShift.AddShiftFragment
import com.appttude.h_mal.days_left.ui.login.FullscreenActivity
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModel
import com.appttude.h_mal.days_left.ui.main.list.ShiftListRecyclerAdapter.Sortation
import com.appttude.h_mal.days_left.ui.splashScreen.SplashFragment
import com.appttude.h_mal.days_left.utils.navigateTo
import com.appttude.h_mal.days_left.utils.showToast
import kotlinx.android.synthetic.main.fragment_list.*


class FragmentList : androidx.fragment.app.Fragment(), SearchView.OnQueryTextListener {
    val viewModel: ShiftsViewModel by activityViewModels()

    lateinit var fireAdapter: ShiftListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set custom firebase adapter on listview

        add_test.setOnClickListener {
            view.navigateTo(R.id.list_to_addShiftActivity)
        }

        fireAdapter =
            ShiftListRecyclerAdapter(
                view,
                viewModel
            )

        recycler_view.apply {
            adapter = fireAdapter
//            onItemLongClickListener =
//                AdapterView.OnItemLongClickListener { _, _, position, _ ->
//                    AlertDialog.Builder(context).apply {
//                        setTitle("Are you sure you want to delete?")
//                        setNegativeButton(android.R.string.no, null)
//                        setPositiveButton(android.R.string.yes) { _, _ ->
//                            fireAdapter.getRef(position).removeValue()
//                        }
//                        create().show()
//                    }
//                    true
//                }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list_fragment, menu)

        val menuItem = menu.findItem(R.id.app_bar_filter)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_soft -> {
                sortData()
                return false
            }
        }
        return false
    }

    private fun sortData() {
        val groupName = arrayOf("Name", "Date Added", "Date of shift")
        val checkedItem = -1

        AlertDialog.Builder(context).apply {
            setTitle("Sort by:")
            setSingleChoiceItems(groupName, checkedItem) { dialog, item ->
                when (item) {
                    0 -> {
                        fireAdapter.filterShifts(Sortation.Name())
                    }
                    1 -> {
                        fireAdapter.filterShifts(Sortation.DateAdded())
                    }
                    2 ->{
                        fireAdapter.filterShifts(Sortation.ShiftDate())
                    }
                }
                dialog.dismiss()
            }
            create().show()
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrBlank()){
            fireAdapter.filterList(null)
        }else{
            fireAdapter.filterList(newText)
        }
        return false
    }

//    private fun filterData() {
//        val groupName = arrayOf("Name", "Date Added", "Shift Type")
//        val checkedItem = -1
//
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Filter by:")
//        builder.setSingleChoiceItems(
//            groupName,
//            checkedItem,
//            DialogInterface.OnClickListener { dialog, item ->
//                dialog.dismiss()
//
//                when (item) {
//                    0 -> {
//                        val dialogBuilder = AlertDialog.Builder(context)
//                        dialogBuilder.setTitle("Select Employer:")
//                        //get layout
//                        val dialogView = View.inflate(
//                            context,
//                            R.layout.dialog_previous_abns_used, null
//                        )
//                        //hide button
//                        dialogView.button_list_dialog.visibility = View.GONE
//                        //get listview
//                        val listView = dialogView.list_item_list_dialog
//                        //get unique abn objects
//                        val uniqueAbnObjects = viewModel.shifts
//                        //populate list in view
//                        listView.adapter =
//                            AbnListAdapter(
//                                requireContext(),
//                                uniqueAbnObjects
//                            )
//                        //on item click listener
//                        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
//                            applyFilter(uniqueAbnObjects[position].abn!!, null)
//                        })
//                        //set view on dialog
//                        dialogBuilder.setView(dialogView)
//
//                        dialogBuilder.create().show()
//
//                    }
//                    1 -> {
//                        val customDialog =
//                            CustomDialog(requireContext())
//
//                        customDialog.setButton(BUTTON_POSITIVE,
//                            getContext()?.getString(android.R.string.yes),
//                            DialogInterface.OnClickListener { dialogNew, which ->
//                                //interface results back
//                                if (dateSelectionFrom != dateSelectionTo) {
//                                    applyFilter(dateSelectionFrom, dateSelectionTo)
//                                }
//
//                                customDialog.dismiss()
//                            })
//
//                        customDialog.create()
//                    }
//                    2 -> {
//                        val typeDialog = AlertDialog.Builder(context)
//                        val typeString = arrayOf("Hourly", "Piece Rate")
//
//                        typeDialog.setSingleChoiceItems(
//                            arrayOf("Hourly", "Piece Rate"), -1
//                        ) { dialog, which ->
//                            val q1 =
//                                ref.orderByChild("taskObject/workType").equalTo(typeString[which])
//
//                            fireAdapter = FireAdapter(
//                                activity,
//                                ShiftObject::class.java,
//                                R.layout.list_item,
//                                q1
//                            )
//                            page_two_list.adapter = fireAdapter
//                        }
//                        typeDialog.create().show()
//                    }
//                }
//            })
//    }
//
//    fun turnToUniqueAbnObject(shifts: ArrayList<ShiftObject>): List<AbnObject> {
//        val abnList = mutableListOf<AbnObject>()
//
//        shifts.forEach { shiftObject ->
//            shiftObject.abnObject?.let { abnList.add(it) }
//        }
//
//        return abnList.distinct()
//    }
//
//    fun applyFilter(arg1: String, arg2: String?) {
//        val q1: Query
//        if (arg2 == null) {
//            q1 = ref.orderByChild("abnObject/abn").equalTo(arg1)
//        } else {
//            q1 = ref.orderByChild("shiftDate").startAt(arg1).endAt(arg2)
//        }
//
//        fireAdapter = FireAdapter(
//            activity,
//            ShiftObject::class.java,
//            R.layout.list_item,
//            q1
//        )
//        page_two_list.adapter = fireAdapter
//    }


}
