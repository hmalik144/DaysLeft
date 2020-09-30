package com.appttude.h_mal.days_left.ui.main.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.models.AbnObject
import com.appttude.h_mal.days_left.models.ShiftObject
import com.appttude.h_mal.days_left.models.TaskObject
import com.appttude.h_mal.days_left.utils.HOURLY_CONST
import com.appttude.h_mal.days_left.utils.formatToTwoDp
import kotlinx.android.synthetic.main.list_item_title.view.*

class ShiftDetailsRecycler(
    val context: Context,
    liveShift: LiveData<ShiftObject>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var shift: ShiftObject? = null

    init {
        liveShift.observeForever{
            shift = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val item = LayoutInflater.from(context)
                    .inflate(R.layout.list_item_title, parent, false)
                ItemHeader(item)
            }
            else ->{
                val item = LayoutInflater.from(context)
                    .inflate(R.layout.item_two_layout, parent, false)
                ItemOne(item)
            }
        }
    }

    override fun getItemCount(): Int{
        return if (shift == null){
            1
        }else{
            15
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            // header
            1 -> {
                val viewHolderCurrent = holder as ItemHeader
                viewHolderCurrent.title.text = when(position){
                    0 -> context.getString(R.string.employer_details)
                    4 -> context.getString(R.string.shift_details)
                    10 -> context.getString(R.string.date_details)
                    else -> ""
                }
            }
            2 -> {
                val viewHolderCurrent = holder as ItemOne

                val pair = getItemDetails(position)
                viewHolderCurrent.top.text = pair.first
                viewHolderCurrent.bottom.text = pair.second
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0,4,10 -> 1
            else -> 2
        }
    }

    private fun getItemDetails(position: Int): Pair<String, String?> {
        return when(position){
            1 -> Pair(context.getString(R.string.employer_name_title), shift?.abnObject?.companyName)
            2 -> Pair(context.getString(R.string.employer_abn_title), shift?.abnObject?.abn)
            3 -> Pair(context.getString(R.string.employer_location_title), getLocation(shift?.abnObject) )

            5 -> Pair(context.getString(R.string.shift_date_details), shift?.shiftDate)
            6 -> Pair(context.getString(R.string.shift_task_details), shift?.taskObject?.task)
            7 -> Pair(context.getString(R.string.shift_rate_details), getRateOfPay(shift?.taskObject))
            8 -> Pair(context.getString(R.string.shift_units_details), calculateUnitString())
            9 -> Pair(context.getString(R.string.shift_total_pay_details), calculateTotalPay().toString())

            11 -> Pair(context.getString(R.string.shift_time_in_details), shift?.timeObject?.timeIn)
            12 -> Pair(context.getString(R.string.shift_time_out_details), shift?.timeObject?.timeOut)
            13 -> Pair(context.getString(R.string.shift_break_details), getBreak())
            14 -> Pair(context.getString(R.string.shift_hours_details), getHours().toString())
            else -> Pair("","")
        }
    }

    private fun getLocation(abnObject: AbnObject?): String? {
        return abnObject?.let {
            "${it.postCode} ${it.state}"
        }
    }

    private fun getRateOfPay(task: TaskObject?): String? {
        return task?.let {
            val rate = "$${task.rate?.formatToTwoDp()}"
            val type = if (task.workType == HOURLY_CONST){
                "hour"
            }else{
                "unit"
            }
            "$rate per $type"
        }
    }

    fun calculateUnitString(): String? {
        return getUnits()?.formatToTwoDp()
    }

    private fun getUnits(): Float? {
        return if (shift?.taskObject?.workType == HOURLY_CONST){
            getHours()
        }else{
            shift?.unitsCount
        }
    }

    fun getHours() = shift?.timeObject?.hours ?: 0.00f

    fun calculateTotalPay(): Float {
        val units: Float = getUnits() ?: return 0.00f
        val rate: Float = shift?.taskObject?.rate ?: return 0.00f
        return units * rate
    }

    fun getBreak(): String {
        val breakMins = shift?.timeObject?.breakInt ?: 0
        return breakMins.toString()
    }

    internal inner class ItemOne(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val top = itemView.findViewById<TextView>(android.R.id.text1)
        val bottom = itemView.findViewById<TextView>(android.R.id.text2)
    }

    internal inner class ItemHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.title
    }

}