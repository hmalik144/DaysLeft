package com.appttude.h_mal.days_left.ui.addShift

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.appttude.h_mal.days_left.R
import com.appttude.h_mal.days_left.ui.main.ShiftsViewModel
import com.appttude.h_mal.days_left.utils.navigateTo
import kotlinx.android.synthetic.main.fragment_tasks.*

class TasksFragment : Fragment() {
    val viewModel: ShiftsViewModel by activityViewModels()

    var abn: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        abn = viewModel.currentShift.value?.abnObject?.abn
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_test.setOnClickListener {
            view.navigateTo(R.id.action_tasksFragment_to_addTaskFragment)
        }

        tasks_recycler.adapter =
            TaskRecycler(
                view,
                abn,
                viewModel
            )
    }
}