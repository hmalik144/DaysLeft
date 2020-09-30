package com.appttude.h_mal.days_left.ui.main.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.helper.ShiftHelper
import com.appttude.h_mal.days_left.helper.ShiftHelper.Companion.ifPieceRate
import com.appttude.h_mal.days_left.models.ShiftObject
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModel
import com.appttude.h_mal.days_left.utils.hide
import com.appttude.h_mal.days_left.utils.inflateLayout
import com.appttude.h_mal.days_left.utils.navigateTo
import com.appttude.h_mal.days_left.utils.show
import kotlinx.android.synthetic.main.list_item.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ShiftListRecyclerAdapter(
    val view: View,
    val viewModel: ShiftsViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), KodeinAware {

    override val kodein by kodein(view.context)
    val helper by instance<ShiftHelper>()

    var list: List<Pair<String, ShiftObject>>? = null

    init {
        viewModel.shiftData.observeForever {
            list = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemOne = view.context.inflateLayout(R.layout.list_item, parent)
        return ShiftViewHolder(itemOne)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val container = holder as ShiftViewHolder
        val item = list?.get(position)?.second

        container.populateView(item)

        container.itemView.setOnClickListener {
            val id = list?.get(position)?.first

            id?.let {
                val actions = FragmentListDirections.listToShiftOverviewFragment(id)
                view.navigateTo(actions)
            }

        }
    }

    internal inner class ShiftViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun populateView(model: ShiftObject?) {
            itemView.item_title.text = model?.abnObject?.abn
            itemView.date?.text = model?.shiftDate
            itemView.task_name?.text = model?.taskObject?.task
            itemView.type?.text = model?.taskObject?.workType
            val locationString: String =
                model?.abnObject?.state + " - " + model?.abnObject?.postCode
            itemView.location?.text = locationString
            val breakHolder = itemView.container_four

            itemView.item_title?.text = model?.abnObject?.companyName

            val s = helper.createTaskSummary(model?.taskObject)
            itemView.type?.text = s

            model?.taskObject?.workType?.ifPieceRate({
                itemView.container_three?.hide()
                itemView.container_five?.show()

                itemView.units?.text = model.unitsCount.toString()
            }, {
                itemView.container_three?.hide()
                itemView.container_five?.show()

                itemView.time?.text = helper.getTimeInOutString(model.timeObject)
                itemView.units?.text = helper.convertTimeFloat(model.timeObject?.hours)

                model.timeObject!!.breakInt?.takeIf { it > 0 }?.let { mins ->
                    breakHolder?.show()
                    itemView.break_time?.text =
                        helper.getBreakTimeString(model.timeObject?.breakInt)
                    return@ifPieceRate
                }
                breakHolder?.hide()
            })
        }

    }

    fun filterList(constraint: String?) {
        constraint?.let {
            list = list?.filter {
                it.toString().contains(constraint, ignoreCase = true)
            }
            notifyDataSetChanged()
            return
        }
        list = viewModel.shiftData.value
        notifyDataSetChanged()
    }

    sealed class Sortation {
        class Name : Sortation()
        class Type : Sortation()
        class DateAdded : Sortation()
        class ShiftDate : Sortation()
        class units : Sortation()
        class default : Sortation()
    }

    private fun selectSortation(op: Sortation, reverse: Boolean) {
        list = when (op) {
            is Sortation.Name -> list?.sortedWith(compareBy { it.second.abnObject?.companyName })
                ?.reversed(reverse)
            is Sortation.Type -> list?.sortedWith(compareBy { it.second.taskObject?.task })
                ?.reversed(reverse)
            is Sortation.DateAdded -> list?.sortedWith(compareBy { it.second.dateTimeAdded })
                ?.reversed(reverse)
            is Sortation.ShiftDate -> list?.sortedWith(compareBy { it.second.shiftDate })
                ?.reversed(reverse)
            is Sortation.units -> list?.sortedWith(compareBy { it.second.unitsCount })
                ?.reversed(reverse)
            is Sortation.default -> viewModel.shiftData.value
        }
    }

    private fun <T : Any> List<T>.reversed(b: Boolean): List<T> {
        return apply {
            if (b) reversed()
        }
//
//        return if (b) {
//            this.reversed()
//        } else {
//            this
//        }
    }

    fun filterShifts(op: Sortation, reverse: Boolean = false) {
        selectSortation(op, reverse)
        notifyDataSetChanged()
    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//
//
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//
//            }
//        }
//    }
}