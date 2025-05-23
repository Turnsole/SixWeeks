package com.lastminutedevice.sixweeks.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(repository: Repository) : ViewModel() {

    val workout: LiveData<UserWorkout> = repository.loadWorkouts()
        .map { it.last() }
        .asLiveData(
            context = this.viewModelScope.coroutineContext,
            timeoutInMs = 5000
        )
}
