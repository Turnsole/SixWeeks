package com.lastminutedevice.sixweeks.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lastminutedevice.sixweeks.R
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import com.lastminutedevice.sixweeks.data.room.Workout
import com.lastminutedevice.sixweeks.databinding.FragmentTodayBinding
import com.lastminutedevice.sixweeks.databinding.TodayCardRestBinding
import com.lastminutedevice.sixweeks.databinding.TodayCardTestBinding
import dagger.hilt.android.AndroidEntryPoint

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
                workout.sets.isEmpty() && workout.rest > 0 -> displayRest(workout)
                workout.sets.isEmpty() -> displayTest(workout)
                else -> displayWorkout(workout)
            }
            binding.dailyActivityContainer.addView(cardContents)
            binding.weekProgress.text = requireContext().getString(R.string.today_progress, workout.week)
        }
        return binding.root
    }

    fun displayRest(workout: UserWorkout) : View {
        return TodayCardRestBinding.inflate(layoutInflater).root
    }

    fun displayTest(workout: UserWorkout) : View {
        val testBinding = TodayCardTestBinding.inflate(layoutInflater)
        testBinding.testHeader.setText(R.string.today_test_header)
        return testBinding.root
    }

    fun displayWorkout(workout: UserWorkout) : View {
        return TodayCardRestBinding.inflate(layoutInflater).root
    }
}
