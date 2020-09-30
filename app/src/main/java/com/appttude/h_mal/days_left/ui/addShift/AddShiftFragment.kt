package com.appttude.h_mal.days_left.ui.addShift

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.helper.ShiftHelper
import com.appttude.h_mal.days_left.helper.ShiftHelper.Companion.ifPieceRate
import com.appttude.h_mal.days_left.models.AbnObject
import com.appttude.h_mal.days_left.models.ShiftObject
import com.appttude.h_mal.days_left.models.TaskObject
import com.appttude.h_mal.days_left.models.TimeObject
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModel
import com.appttude.h_mal.days_left.ui.main.shiftInfo.DateDialog
import com.appttude.h_mal.days_left.ui.main.shiftInfo.TimeDialog
import com.appttude.h_mal.days_left.utils.*
import kotlinx.android.synthetic.main.fragment_add_shift.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*


class AddShiftFragment : Fragment(), KodeinAware {
    var timeObject: TimeObject? = null
    var abnObject: AbnObject? = null
    var taskObject: TaskObject? = null

    override val kodein by kodein()
    val helper by instance<ShiftHelper>()

    val viewModel by activityViewModels<ShiftsViewModel>()
    var id: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_shift, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { !it.isEmpty }?.let {
            // get shift Id from nav args
            id = AddNewShiftActivityArgs.fromBundle(it).shiftId
        }

        viewModel.currentShift.observe(viewLifecycleOwner, Observer { shift ->
            progress_bar.hide()

            shift?.abnObject?.let {
                abnObject = it
                setEmployerCard()
            }

            shift?.taskObject?.let {
                taskObject = it
                setTaskCard()
            }

            shift.timeObject?.let { tO ->
                timeObject = tO
                setTimeSummary()
            }

            shift.unitsCount?.formatToTwoDp()?.let { unitsCount ->
                units.setText(unitsCount)
            }

            date.setText(shift.shiftDate)

        })

        // get single shift and observe
        id?.let { id ->
            viewModel.retrieveSingleShift(id)
        }

        // apply on click listeners
        select_employer_card.setOnClickListener{
            view.navigateTo(R.id.addShift_to_employersFragment_2)
        }
        select_task_card.setOnClickListener{
            view.navigateTo(R.id.addShift_to_tasksFragment)
        }
        search_button.setOnClickListener(submitListener)
        select_times_card.setOnClickListener {
            TimeDialog(requireContext(), timeObject) { retrievedTime ->
                viewModel.setAddShift(ShiftObject(timeObject = retrievedTime))
                setTimeSummary()
            }
        }

        date.apply {
            isFocusable = false
            setOnClickListener {
                val dateDialog =
                    DateDialog(
                        this,
                        requireContext(),
                        {
                            viewModel.setAddShift(ShiftObject(shiftDate = it))
                        }
                    )
                dateDialog.show()
            }
        }
    }

    private val submitListener: View.OnClickListener = View.OnClickListener {
        if (timeObject == null) {
            context?.showToast("Time information missing")
            return@OnClickListener
        }
        if (abnObject == null) {
            context?.showToast("Employer information missing")
            return@OnClickListener
        }
        if (taskObject == null) {
            context?.showToast("Task information missing")
            return@OnClickListener
        }
        if (date.text.isBlank()) {
            context?.showToast("Date missing")
            return@OnClickListener
        }
        if (taskObject?.workType == PIECE_CONST && units.text.isEmpty()) {
            context?.showToast("Units information missing")
            return@OnClickListener
        }

        val shiftobj = ShiftObject(
            date.text.toString(),
            getDateTimeString(),
            abnObject,
            taskObject,
            null,
            timeObject
        )

        units.text?.takeIf { it.isNotBlank() }?.toString()?.trim()?.toFloat()?.let { units ->
            shiftobj.unitsCount = units
        }

        id?.let {
            viewModel.updateShift(it, shiftobj)
            return@OnClickListener
        }
        viewModel.postNewShift(shiftobj)
    }

    private fun setEmployerCard() {
        if (lable_1.isVisible) {
            toggleViewVisibility(select_employer_card)
        }

        employer_name.text = abnObject?.companyName
        val loc = abnObject?.state + " " + abnObject?.postCode
        employer_location.text = loc
    }

    // Move part to helper method
    private fun setTaskCard() {
        if (lable_2.isVisible) {
            toggleViewVisibility(select_task_card)
        }
        task.text = taskObject?.task

        val summary = helper.createTaskSummary(taskObject)
        task_summary.text = summary

        taskObject?.workType?.ifPieceRate({
            units.visibility = View.VISIBLE
            select_times_card.visibility = (View.VISIBLE)
        },{
            units.visibility = View.GONE
            select_times_card.visibility = (View.VISIBLE)
        })
    }

    private fun toggleViewVisibility(cardView: CardView) {
        when (cardView.id) {
            R.id.select_employer_card -> {
                employer_layout.toggleVisibility()
                lable_1.toggleVisibility()
                return
            }
            R.id.select_task_card -> {
                task_layout.toggleVisibility()
                lable_2.toggleVisibility()
                return
            }
            R.id.select_times_card -> {
                time_layout.toggleVisibility()
                lable_3.toggleVisibility()
                return
            }
        }

    }

    private fun setTimeSummary() {
        if (lable_3.isVisible) {
            toggleViewVisibility(select_times_card)
        }

        time_summary.text = helper.getTimeInOutString(timeObject)
        time.text = helper.convertTimeFloat(timeObject?.hours)

        timeObject?.breakInt?.takeIf { it > 0 }?.let{
            break_holder.show()
            break_summary.text = "$it minutes"
            return
        }
        break_holder.hide()
    }

    private fun getDateTimeString(): String {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH)

        return sdf.format(cal.time)
    }
}