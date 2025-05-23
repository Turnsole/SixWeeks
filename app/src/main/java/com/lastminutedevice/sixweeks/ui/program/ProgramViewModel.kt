package com.lastminutedevice.sixweeks.ui.program

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProgramViewModel @Inject constructor(repository: Repository): ViewModel() {

    val workouts: LiveData<List<UserWorkout>> = repository.loadWorkouts()
        .asLiveData(
            context = this.viewModelScope.coroutineContext,
            timeoutInMs = 5000
        )
}
