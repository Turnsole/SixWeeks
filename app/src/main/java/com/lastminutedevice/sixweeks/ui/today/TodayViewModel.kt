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

    fun recordWorkout(userWorkout: UserWorkout, threshold: Int? = 0) {
        viewModelScope.launch { repository.recordWorkout(userWorkout, threshold) }
    }
}
