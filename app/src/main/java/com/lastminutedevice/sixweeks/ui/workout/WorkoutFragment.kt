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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WorkoutFragment() : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWorkoutBinding.inflate(layoutInflater, container, false)
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
                setView.workoutSets.text = resources.getQuantityString(R.plurals.reps, reps, reps)
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
