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

@AndroidEntryPoint
class ProgramFragment : Fragment() {

    private var _binding: FragmentProgramBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[ProgramViewModel::class.java]

        _binding = FragmentProgramBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.programOverview.layoutManager = LinearLayoutManager(requireContext())

        // TODO set data instead of constructing with it
        dashboardViewModel.workouts.observe(viewLifecycleOwner) { workouts ->
           binding.programOverview.adapter = ProgramAdapter(workouts)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
