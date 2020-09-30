package com.appttude.h_mal.days_left.ui.addShift

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.models.ShiftObject
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModel
import com.appttude.h_mal.days_left.utils.navigateTo
import com.appttude.h_mal.days_left.utils.popBack
import kotlinx.android.synthetic.main.fragment_employers.*
import kotlinx.android.synthetic.main.fragment_list.add_test


class EmployersFragment : Fragment() {
    val viewModel: ShiftsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_test.setOnClickListener {
            view.navigateTo(R.id.employers_to_addEmployerFragment)
        }

        val employerAdapter = EmployersRecycler {
            viewModel.setAddShift(ShiftObject(abnObject = it))
            view.popBack()
        }

        employer_recycler.adapter = employerAdapter

        viewModel.getMyEmployers().observeForever {
            employerAdapter.apply {
                updateList(it)
                notifyDataSetChanged()
            }
        }
    }

}