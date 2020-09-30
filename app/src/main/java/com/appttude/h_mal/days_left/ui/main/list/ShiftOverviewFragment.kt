package com.appttude.h_mal.days_left.ui.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModel
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModelFactory
import com.appttude.h_mal.days_left.utils.navigateTo
import kotlinx.android.synthetic.main.fragment_shift_overview.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ShiftOverviewFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<ShiftsViewModelFactory>()
    private val viewModel by viewModels<ShiftsViewModel> { factory }

    lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        id = ShiftOverviewFragmentArgs.fromBundle(requireArguments()).id
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shift_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSingleShift(id).let {
            val adapter = ShiftDetailsRecycler(requireContext(), it)
            shift_details_recycler.adapter = adapter
        }

        edit_shift.setOnClickListener {
            val action =
                ShiftOverviewFragmentDirections.shiftOverviewToAddShiftActivity()
            action.shiftId = id
            view.navigateTo(action)
        }
    }

}