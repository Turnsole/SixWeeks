package com.lastminutedevice.sixweeks.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lastminutedevice.sixweeks.R
import com.lastminutedevice.sixweeks.data.models.UserWorkout

private const val PARAM_WORKOUT = "workout"

class TestFragment : BottomSheetDialogFragment() {

    private var paramWorkout: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramWorkout = it.getString(PARAM_WORKOUT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.test_input_fragment, container, false)
    }

    companion object {

        val tag : String = "TestFragment"

        @JvmStatic
        fun newInstance(workout: UserWorkout) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PARAM_WORKOUT, workout)
                }
            }
    }
}
