package com.lastminutedevice.sixweeks.ui.program

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lastminutedevice.sixweeks.databinding.FragmentProgramBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * The Program fragment displays the complete list of workouts in this user's progression.
 * They can see their completed workouts as well as the remaining workouts for their level
 * (if the program being displayed has levels).
 */
@AndroidEntryPoint
class ProgramFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[ProgramViewModel::class.java]

        val binding = FragmentProgramBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.programOverview.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ProgramAdapter()
        binding.programOverview.adapter = adapter
        dashboardViewModel.workouts.observe(viewLifecycleOwner) { workouts ->
            adapter.updateList(newList = workouts)

            val lastCompleted = adapter.firstIncompletePosition()
            if (lastCompleted > 0) {
                binding.programOverview.smoothScrollToPosition(lastCompleted)
            }
        }
        return root
    }
}
