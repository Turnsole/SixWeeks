package com.lastminutedevice.sixweeks.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(repository: Repository) : ViewModel() {

    // TODO optimize loading to get only the latest incomplete workout, or an error.
    val workout: LiveData<UserWorkout> = repository.loadWorkouts()
        .filter { it.isNotEmpty() }
        .map { it.first() }
        .asLiveData(
            context = this.viewModelScope.coroutineContext,
            timeoutInMs = 5000
        )
}
