package com.lastminutedevice.sixweeks.ui.workout

import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.lastminutedevice.sixweeks.R
import com.lastminutedevice.sixweeks.databinding.FragmentWorkoutBinding
import com.lastminutedevice.sixweeks.databinding.FragmentWorkoutRestBinding
import com.lastminutedevice.sixweeks.databinding.FragmentWorkoutSetBinding
import com.lastminutedevice.sixweeks.ui.SetDisplayCalculator
import dagger.hilt.android.AndroidEntryPoint


/**
 * This fragment displays the daily workout, which is a timer (if appropriate) or
 * the number of reps to complete.
 */
@AndroidEntryPoint
class WorkoutFragment() : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set this DialogFragment to have rounded corners.
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        // Set up the ViewModel and View binding.
        val binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val setView = FragmentWorkoutSetBinding.inflate(inflater, binding.root, false)
        val restView = FragmentWorkoutRestBinding.inflate(inflater, binding.root, false)

        val viewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                WorkoutViewModel.ViewState.SET -> {
                    binding.container.removeAllViews()
                    binding.container.addView(setView.root)
                }

                WorkoutViewModel.ViewState.REST -> {
                    binding.container.removeAllViews()
                    binding.container.addView(restView.root)
                }

                WorkoutViewModel.ViewState.DISMISS -> dismiss()
            }
        }

        viewModel.reps.observe(viewLifecycleOwner) { reps ->
            reps?.let {
                val formattedReps = SetDisplayCalculator(resources).repString(reps)
                setView.workoutSets.text = resources.getString(R.string.reps, formattedReps)
            }
        }
        setView.workoutSetsDoneButton.setOnClickListener { viewModel.completeSet() }

        viewModel.countDown.observe(viewLifecycleOwner) { seconds ->
            restView.countdown.text = resources.getString(R.string.rest_message, seconds)
        }

        return binding.root
    }

    override fun onResume() {
        // Make the dialog 90% of the width of the screen.
        dialog?.window?.let { window ->
            val size = Point()
            val display = window.windowManager.defaultDisplay
            display.getSize(size)
            window.setLayout((size.x * 0.90).toInt(), (size.y * 0.25).toInt())
            window.setGravity(Gravity.CENTER)
        }
        super.onResume()
    }
}
