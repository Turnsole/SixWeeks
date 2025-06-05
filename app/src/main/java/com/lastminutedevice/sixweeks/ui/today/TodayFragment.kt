package com.lastminutedevice.sixweeks.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lastminutedevice.sixweeks.R
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import com.lastminutedevice.sixweeks.databinding.FragmentTodayBinding
import com.lastminutedevice.sixweeks.databinding.TodayCardRestBinding
import com.lastminutedevice.sixweeks.databinding.TodayCardTestBinding
import com.lastminutedevice.sixweeks.databinding.TodayCardWorkoutBinding
import com.lastminutedevice.sixweeks.ui.test.TestFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class TodayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel = ViewModelProvider(this)[TodayViewModel::class.java]
        val binding = FragmentTodayBinding.inflate(inflater, container, false)

        homeViewModel.workout.observe(viewLifecycleOwner) { workout ->
            val cardContents = when {
                workout.testThreshold != null -> {
                    displayTest(workout = workout, fab = binding.fab, viewModel = homeViewModel)
                }

                workout.sets.isEmpty() -> {
                    binding.fab.visibility = View.GONE
                    displayRest()
                }

                else -> {
                    displayWorkout(workout = workout, fab = binding.fab, viewModel = homeViewModel)
                }
            }
            binding.dailyActivityContainer.removeAllViews()
            binding.dailyActivityContainer.addView(cardContents)
            binding.weekProgress.text =
                requireContext().getString(R.string.today_progress, workout.week)
        }
        return binding.root
    }

    fun displayRest(): View {
        return TodayCardRestBinding.inflate(layoutInflater).root
    }

    /**
     * TODO if the workout is completed then display the number of max reps done.
     */
    fun displayTest(workout: UserWorkout, fab: FloatingActionButton, viewModel: TodayViewModel): View {
        val testBinding = TodayCardTestBinding.inflate(layoutInflater)
        testBinding.testHeader.setText(R.string.today_test_header)
        testBinding.testResult.text = requireContext().getString(
            R.string.today_test_threshold,
            workout.testThreshold
        )

        fab.visibility = View.VISIBLE
        fab.setImageResource(R.drawable.note)
        fab.setOnClickListener {
            val fragment = TestFragment.newInstance(workout)
            fragment.show(parentFragmentManager, TestFragment.TAG)
        }

        return testBinding.root
    }

    fun displayWorkout(workout: UserWorkout, fab: FloatingActionButton, viewModel: TodayViewModel): View {
        val binding = TodayCardWorkoutBinding.inflate(layoutInflater)
        binding.workoutHeader.text = requireContext().getString(
            R.string.today_workout_header,
            LocalDate.now().format(DateTimeFormatter.ofPattern("E, MMMM d"))
        )
        binding.workoutSets.text = requireContext().getString(
            R.string.today_workout,
            workout.rest,
            workout.sets.joinToString(", ")
        )

        fab.visibility = View.VISIBLE
        fab.setImageResource(R.drawable.ink_marker)
        fab.setOnClickListener {
            viewModel.recordWorkout(workout)
        }

        return binding.root
    }
}
