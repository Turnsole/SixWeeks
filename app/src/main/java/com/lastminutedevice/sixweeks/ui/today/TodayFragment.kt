package com.lastminutedevice.sixweeks.ui.today

import android.annotation.SuppressLint
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
import com.lastminutedevice.sixweeks.ui.SetDisplayCalculator
import com.lastminutedevice.sixweeks.ui.test.TestFragment
import com.lastminutedevice.sixweeks.ui.workout.WorkoutFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * This is the home fragment which displays the user's progress through the program
 * as well as a preview of today's workout.
 */
@AndroidEntryPoint
class TodayFragment : Fragment() {

    @SuppressLint("StringFormatMatches") // Different programs show weeks or not.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this)[TodayViewModel::class.java]
        val binding = FragmentTodayBinding.inflate(inflater, container, false)

        // Show the last test results.
        viewModel.lastTest.observe(viewLifecycleOwner) { test ->
            test?.completed?.motions?.let { motions ->
                binding.lastTestCard.visibility = View.VISIBLE
                val localDateForTimestamp = Instant.ofEpochMilli(test.completed.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                binding.lastTestDate.text = localDateForTimestamp.format(DateTimeFormatter.ofPattern("MMMM d, y"))
                val motions = resources.getString(R.string.reps, motions)
                binding.lastTestMessage.text = getString(R.string.today_last_test, motions)
            }
        }

        // Show today's workout.
        viewModel.workout.observe(viewLifecycleOwner) { state ->
            val cardContents = when {
                state.nextWorkout == null -> {
                    if (state.programComplete) {
                        displayProgramComplete()
                    } else {
                        displayError()
                    }
                }
                state.nextWorkout.testThreshold > 0 -> {
                    displayTest(
                        workout = state.nextWorkout,
                        fab = binding.fab
                    )
                }

                // If there was no test, and no sets, this is a rest day.
                state.nextWorkout.sets.isEmpty() -> {
                    viewModel.restDayDisplayed(state.nextWorkout)
                    displayRest()
                }

                else -> {
                    displayWorkout(
                        workout = state.nextWorkout,
                        fab = binding.fab
                    )
                }
            }
            binding.dailyActivityContainer.removeAllViews()
            binding.dailyActivityContainer.addView(cardContents)

            // Show overall progress.
            val totalWorkouts = viewModel.totalWorkouts.value!!.toFloat()
            state.nextWorkout?.let { workout ->
                binding.weekProgressCard.visibility = View.VISIBLE
                binding.weekProgressMessage.text = getString(R.string.today_progress, workout.day, workout.week)
                binding.progressBar.progress = ((workout.id / totalWorkouts) * 100).toInt()
            }
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
            testBinding.testResult.text = getString(
                R.string.today_test_result,
                workout.completed.motions,
                workout.testThreshold
            )
        } else {
            testBinding.testResult.text = getString(
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

    fun displayWorkout(workout: UserWorkout, fab: FloatingActionButton): View {
        val binding = TodayCardWorkoutBinding.inflate(layoutInflater)
        binding.workoutHeader.text = getString(R.string.today_workout_header)

        val setDisplayCalculator = SetDisplayCalculator(resources)
        if (workout.completed == null) {
            binding.workoutSets.text = getString(
                R.string.today_workout,
                workout.rest,
                workout.sets.joinToString(", ") { setDisplayCalculator.repString(reps = it) }
            )

            fab.visibility = View.VISIBLE
            fab.setImageResource(R.drawable.ink_marker)
            fab.setOnClickListener {
                WorkoutFragment().show(parentFragmentManager, "workout")
            }
        } else {
            fab.visibility = View.GONE

            val sum = setDisplayCalculator.repString(workout.sets.sum())
            val reps = resources.getString(R.string.reps, sum)
            binding.workoutSets.text = getString(R.string.today_completed, reps)
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
