package com.appttude.h_mal.days_left.ui.addShift

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.models.AbnObject
import com.appttude.h_mal.days_left.utils.inflateLayout
import com.appttude.h_mal.days_left.utils.showToast
import kotlinx.android.synthetic.main.general_list_item.view.*
import kotlinx.android.synthetic.main.icon_text_layout.view.*

class EmployersRecycler(
    val selected: ((AbnObject) -> Unit)
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var list = mutableListOf<AbnObject>()

    fun updateList(abnList: List<AbnObject>){
        list.clear()
        list.addAll(abnList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0){
            val itemOne = parent.context.inflateLayout(R.layout.item_employer_search_empty, parent)
            EmptyViewHold(itemOne)
        }else{
            val itemOne = parent.context.inflateLayout(R.layout.general_list_item, parent)
            AbnViewHolder(itemOne)
        }
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) 1
        else list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AbnViewHolder){
            val item = list?.get(position)
            holder.populateView(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.size > 0){
            1
        }else{
            0
        }
    }

    internal inner class AbnViewHolder(v: View): RecyclerView.ViewHolder(v){

        fun populateView(model: AbnObject?){
            itemView.item_title.text = model?.companyName
            val locationLayout = itemView.location_layout
            val abnLayout = itemView.abn_layout

            locationLayout.apply {
                val location = "${model?.state} ${model?.postCode}"
                icon.setImageResource(R.drawable.marker)
                title.text = "Location"
                item_text.text = location
            }

            abnLayout.apply {
                icon.setImageResource(R.drawable.ic_baseline_assignment_24)
                title.text = "ABN"
                item_text.text = model?.abn
            }

            itemView.setOnClickListener {
                model?.let{ selected.invoke(model) }
            }
        }

    }

    internal inner class EmptyViewHold(v: View): RecyclerView.ViewHolder(v){

    }

//    fun filterList(constraint: String?){
//        constraint?.let {
//            list = list?.filter {
//                it.toString().contains(constraint, ignoreCase = true)
//            }
//            notifyDataSetChanged()
//            return
//        }
//        list = viewModel.shiftData.value
//        notifyDataSetChanged()
//    }
//
//    sealed class Sortation {
//        class Name : Sortation()
//        class Type : Sortation()
//        class DateAdded : Sortation()
//        class ShiftDate : Sortation()
//        class units: Sortation()
//        class default: Sortation()
//    }
//
//    private fun selectSortation(op: Sortation, reverse: Boolean) {
//        list = when (op) {
//            is Sortation.Name -> list?.sortedWith(compareBy { it.second.abnObject?.companyName })?.reversed(reverse)
//            is Sortation.Type -> list?.sortedWith(compareBy  { it.second.taskObject?.task })?.reversed(reverse)
//            is Sortation.DateAdded -> list?.sortedWith(compareBy  { it.second.dateTimeAdded })?.reversed(reverse)
//            is Sortation.ShiftDate -> list?.sortedWith(compareBy  { it.second.shiftDate })?.reversed(reverse)
//            is Sortation.units -> list?.sortedWith(compareBy  { it.second.unitsCount })?.reversed(reverse)
//            is Sortation.default -> viewModel.shiftData.value
//        }
//    }
//
//    private fun <T: Any> List<T>.reversed(b: Boolean): List<T>{
//        return if (b){
//            this.reversed()
//        }else {
//            this
//        }
//    }
//
//    fun filterShifts(op: Sortation, reverse: Boolean){
//        selectSortation(op, reverse)
//        notifyDataSetChanged()
//    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//
//            }
//        }
//    }
}