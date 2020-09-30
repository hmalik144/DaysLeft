package com.appttude.h_mal.days_left

import android.app.Activity
import android.view.View
import com.appttude.h_mal.days_left.models.ShiftObject
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.database.Query

class FireAdapter(
    activity: Activity?,
    modelClass: Class<ShiftObject>?,
    modelLayout: Int,
    ref: Query?) :
    FirebaseListAdapter<ShiftObject>(activity, modelClass, modelLayout, ref) {

    override fun populateView(v: View?, model: ShiftObject?, position: Int) {
//        v?.farm_name?.text = model?.abnObject?.abn
//        v?.date?.text = model?.shiftDate
//        v?.task_name?.text = model?.taskObject?.task
//        v?.type?.text = model?.taskObject?.workType
//        val locationString:String = model?.abnObject?.state + " - " + model?.abnObject?.postCode
//        v?.location?.text = locationString
//
//        v?.farm_name?.text = model?.abnObject?.companyName
//
//        var s = model?.taskObject?.workType + " - $" + model?.taskObject?.rate + "/"
//
//        if (model?.taskObject?.workType == FirebaseClass.HOURLY){
//            s = "$s  Hour"
//            v?.container_three?.visibility = View.VISIBLE
//            v?.container_five?.visibility = View.GONE
//
//            val time = model.timeObject?.timeIn + " - " + model.timeObject!!.timeOut
//            v?.time?.text = time
//
//            model.timeObject!!.breakEpoch.let { mins ->
//                val breakHolder = v?.container_four
//                if (mins > 0){
//                    breakHolder?.visibility = View.VISIBLE
//                    v?.break_time?.text = getBreakTimeString(model.timeObject!!.breakEpoch)
//                }else{
//                    breakHolder?.visibility = View.GONE
//                }
//            }
//        }else{
//            s = "$s  Unit"
//            v?.container_three?.visibility = View.GONE
//            v?.container_five?.visibility = View.VISIBLE
//
//            v?.units?.text = model?.unitsCount.toString()
//        }
//
//        v?.type?.text = s
    }



//    private fun getBreakTimeString(breakMins: Int): String {
//        val hoursFloat = (breakMins / 60).toFloat()
//
//        val hoursInt = Math.floor(hoursFloat.toDouble()).toInt()
//        val minsInt = breakMins - hoursInt * 60
//
//        var s = ""
//        if (hoursInt > 0) {
//            s = "$hoursInt h "
//        }
//
//        if (minsInt > 0) {
//            s = "$s$minsInt m"
//        }
//
//        return s
//    }
//
//    fun getId(i: Int): String? {
//        return getRef(i).key
//    }

}