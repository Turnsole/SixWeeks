package com.lastminutedevice.sixweeks.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import com.lastminutedevice.sixweeks.databinding.TestInputFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val PARAM_WORKOUT = "workout"

/**
 * This fragment displays an exhaustion "test" which the user submits in order to determine
 * their "level". This is a bottom sheet which pops up when prompted from the Today fragmnet.
 *
 * Depending on the program it may display a timer or a text input field for reps.
 */
@AndroidEntryPoint
class TestFragment : BottomSheetDialogFragment() {

    private var paramWorkout: UserWorkout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramWorkout = BundleCompat.getSerializable(it, PARAM_WORKOUT, UserWorkout::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = ViewModelProvider(this)[TestViewModel::class.java]
        val binding = TestInputFragmentBinding.inflate(layoutInflater)
        binding.testInput.hint = paramWorkout?.testThreshold?.toString()
        binding.testSubmitButton.setOnClickListener {
            val maxEffort = binding.testInput.text.toString().toInt()
            paramWorkout?.let {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.recordTest(it, maxEffort)
                    withContext(Dispatchers.IO) {
                        dismiss()
                    }
                }
            }
        }
        return binding.root
    }

    companion object {

        const val TAG: String = "TestFragment"

        @JvmStatic
        fun newInstance(workout: UserWorkout) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PARAM_WORKOUT, workout)
                }
            }
    }
}
