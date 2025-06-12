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
import com.lastminutedevice.sixweeks.databinding.TodayCardCompleteBinding
import com.lastminutedevice.sixweeks.databinding.TodayCardErrorBinding
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

        val viewModel = ViewModelProvider(this)[TodayViewModel::class.java]
        val binding = FragmentTodayBinding.inflate(inflater, container, false)

        viewModel.workout.observe(viewLifecycleOwner) { state ->
            val cardContents = when {
                state.nextWorkout == null -> {
                    if (state.programComplete) {
                        displayProgramComplete()
                    } else {
                        displayError()
                    }
                }
                state.nextWorkout.testThreshold != null -> {
                    displayTest(workout = state.nextWorkout, fab = binding.fab)
                }

                state.nextWorkout.sets.isEmpty() -> {
                    binding.fab.visibility = View.GONE
                    displayRest()
                }

                else -> {
                    displayWorkout(workout = state.nextWorkout, fab = binding.fab, viewModel = viewModel)
                }
            }
            binding.dailyActivityContainer.removeAllViews()
            binding.dailyActivityContainer.addView(cardContents)

            state.nextWorkout?.let { workout ->
                binding.weekProgressMessage.text = requireContext()
                    .getString(R.string.today_progress, workout.week)
            } ?: run { binding.weekProgressCard.visibility = View.GONE }
        }
        return binding.root
    }

    fun displayRest(): View {
        return TodayCardRestBinding.inflate(layoutInflater).root
    }

    fun displayTest(workout: UserWorkout, fab: FloatingActionButton): View {
        val testBinding = TodayCardTestBinding.inflate(layoutInflater)
        testBinding.testHeader.setText(R.string.today_test_header)
        if (workout.completed != null) {
            testBinding.testResult.text = requireContext().getString(
                R.string.today_test_result,
                workout.completed.motions,
                workout.testThreshold
            )
        } else {
            testBinding.testResult.text = requireContext().getString(
                R.string.today_test_threshold,
                workout.testThreshold
            )
        }

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

    fun displayProgramComplete() : View {
        return TodayCardCompleteBinding.inflate(layoutInflater).root
    }

    fun displayError() : View {
        return TodayCardErrorBinding.inflate(layoutInflater).root
    }
}
