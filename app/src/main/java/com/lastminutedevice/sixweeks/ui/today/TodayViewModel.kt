package com.lastminutedevice.sixweeks.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lastminutedevice.sixweeks.NextWorkoutResult
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val workout: LiveData<NextWorkoutResult> = repository.getNextWorkout().asLiveData()

    val lastTest: LiveData<UserWorkout?> = repository.getLastTest().asLiveData()

    /**
     * If we show a rest day then we do not have the user manually record that
     * they rested. Mark it done.
     */
    fun restDayDisplayed(workout: UserWorkout) {
        if (workout.completed != null) {
            return
        }
        viewModelScope.launch {
            repository.recordWorkout(userWorkout = workout)
        }
    }
}
