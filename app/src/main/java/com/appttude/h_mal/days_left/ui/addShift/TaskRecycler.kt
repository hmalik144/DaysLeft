package com.appttude.h_mal.days_left.ui.addShift

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.helper.ShiftHelper
import com.appttude.h_mal.days_left.helper.ShiftHelper.Companion.ifPieceRate
import com.appttude.h_mal.days_left.models.ShiftObject
import com.appttude.h_mal.days_left.models.TaskObject
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModel
import com.appttude.h_mal.days_left.utils.HOURLY_CONST
import com.appttude.h_mal.days_left.utils.PIECE_CONST
import com.appttude.h_mal.days_left.utils.inflateLayout
import com.appttude.h_mal.days_left.utils.popBack
import kotlinx.android.synthetic.main.general_list_item.view.*
import kotlinx.android.synthetic.main.icon_text_layout.view.*
import kotlinx.android.synthetic.main.task_list_item.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class TaskRecycler(
    val view: View,
    val abn: String?,
    val viewModel: ShiftsViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), KodeinAware {

    override val kodein by kodein(view.context)
    val helper by instance<ShiftHelper>()


    var list: MutableList<TaskObject>? = null

    init {
        abn?.let {
            viewModel.getTasks(abn).observeForever {
                list = it as MutableList<TaskObject>?
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemOne = view.context.inflateLayout(R.layout.general_list_item, parent)
        return TaskViewHolder(itemOne)
        //todo: add a empty list view
    }

    override fun getItemCount(): Int {
        return list?.size ?: 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val container = holder as TaskViewHolder
        val item = list?.get(position)

        container.populateView(item)
    }

    internal inner class TaskViewHolder(v: View): RecyclerView.ViewHolder(v){

        fun populateView(model: TaskObject?){
            itemView.item_title.text = model?.task
            val typeLayout = itemView.location_layout
            val rateLayout = itemView.abn_layout

            typeLayout.apply {
                icon.setImageResource(R.drawable.task)

                title.text = "Work type"
                model?.workType.ifPieceRate({
                    item_text.text = PIECE_CONST
                },{
                    item_text.text = HOURLY_CONST
                })
            }

            rateLayout.apply {
                icon.setImageResource(R.drawable.ic_baseline_attach_money_24)
                title.text = "Pay rate"
                item_text.text = helper.createRateSummary(model)
            }

            itemView.setOnClickListener {
                viewModel.setAddShift(ShiftObject(taskObject = model))
                view.popBack()
            }
        }

    }

    fun addItem(taskObject: TaskObject){
        list?.add(taskObject)
        notifyDataSetChanged()
    }

}
