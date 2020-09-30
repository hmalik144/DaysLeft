package com.appttude.h_mal.days_left.ui.main.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.models.ShiftObject
import com.appttude.h_mal.days_left.models.TimeObject
import com.appttude.h_mal.days_left.utils.*

class RecyclerGridAdapter(
    val context: Context,
    private val shiftList: List<ShiftObject?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var uniqueEntries: Int = 0
    var typeCount: IntArray

    init {
        uniqueEntries = countDistinct()
        typeCount = countShiftType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemThree = context.inflateLayout(R.layout.item_three, parent)
        return ItemThree(itemThree)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewCounting = holder as ItemThree

        val cardTitle = viewCounting.cardTitle
        val cardIcon = viewCounting.cardIcon
        val units = viewCounting.units
        val totalEarned = viewCounting.totalEarned
        val total: String

        when (position){
            0 ->{
                cardTitle.text = HOURLY_CONST
                cardIcon.setImageResource(R.drawable.clock_icon)
                val hours = calculateHours().formatToTwoDp() + " hours"
                units.text = hours
                total = calculateAccumulatedPay(Counting.hourly()).formatToTwoDp() + " hours"
            }
            1 ->{
                cardTitle.text = PIECE_CONST
                cardTitle.setTextColor(context.loadColour(R.color.three))
                cardIcon.setImageResource(R.drawable.piece)
                cardIcon.rotation = 270f
                val pieces = calculateUnits()?.formatToTwoDp()
                units.text = pieces
                total = String.format("%.2f", calculateAccumulatedPay(Counting.piece()))
            }
            else -> return
        }
        totalEarned.text = total

    }

    internal inner class ItemThree(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTitle: TextView = itemView.findViewById(R.id.card_title)
        val cardIcon: ImageView = itemView.findViewById(R.id.card_icon)
        val units: TextView = itemView.findViewById(R.id.units)
        val totalEarned: TextView = itemView.findViewById(R.id.total_earned)
    }

    private fun calculateHours(): Double {
        return filterShifts(Counting.hourly()).sumByDouble {
            getTotalHours(it?.timeObject)
        }
    }

    private fun getTotalHours(time: TimeObject?): Double {
        val breakTime = time?.breakInt?.toDouble() ?: return 0.00
        val hours = time.hours?.toDouble() ?:  return 0.00
        return (hours.minus(breakTime))
    }

    private fun calculateUnits(): Double? {
        return filterShifts(Counting.piece()).sumByDouble {
            it?.unitsCount?.toDouble() ?: 0.00
        }
    }


    private fun calculateAccumulatedPay(op: Counting): Double {
        val shifts = filterShifts(op)

        return when (op) {
            is Counting.hourly -> {
                shifts.sumByDouble {
                    getTotalForHourly(it)
                }
            }
            is Counting.piece -> {
                shifts.sumByDouble {
                    getTotalForPiece(it)
                }
            }
            is Counting.both -> {
                shifts.sumByDouble {
                    getTotalForHourly(it) + getTotalForPiece(it)
                }
            }
        }
    }

    private fun getTotalForHourly(shift: ShiftObject?): Double {
        val rate = shift?.taskObject?.rate ?: return 0.00
        val hours = getTotalHours(shift.timeObject)
        return rate * hours
    }

    private fun getTotalForPiece(shift: ShiftObject?): Double {
        val rate = shift?.taskObject?.rate ?: return 0.00
        val units = shift.unitsCount ?: return 0.00
        return (rate * units).toDouble()
    }

    sealed class Counting {
        class hourly : Counting()
        class piece : Counting()
        class both : Counting()
    }

    private fun filterShifts(op: Counting) = when (op) {
        is Counting.hourly -> shiftList.filter { it?.taskObject?.workType == HOURLY_CONST }
        is Counting.piece -> shiftList.filter { it?.taskObject?.workType == PIECE_CONST }
        is Counting.both -> shiftList
    }

    private fun countDistinct(): Int = shiftList.distinctBy {
        it?.shiftDate
    }.count()


    private fun countShiftType(): IntArray {
        filterShifts(Counting.hourly())

        val i = filterShifts(Counting.hourly()).count()
        val j = filterShifts(Counting.piece()).count()
        return intArrayOf(i, j)
    }
}