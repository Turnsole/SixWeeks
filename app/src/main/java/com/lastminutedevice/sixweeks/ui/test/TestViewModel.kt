package com.lastminutedevice.sixweeks.ui.test

import androidx.lifecycle.ViewModel
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    suspend fun recordTest(userWorkout: UserWorkout, result: Int) {
        repository.recordWorkout(userWorkout = userWorkout, maxEffort = result)
    }
}
