package com.lastminutedevice.sixweeks.ui.today

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    // TODO optimize loading to get only the latest incomplete workout, or an error.
    val workout: LiveData<UserWorkout> = repository.loadWorkouts()
        .filter { list ->
            Log.d("TodayViewModel", "Size: ${list.size}")
            list.isNotEmpty()
        }
        .asLiveData(context = this.viewModelScope.coroutineContext)
        .map { list -> list.first() }

    fun recordWorkout(userWorkout: UserWorkout, threshold: Int? = 0) {
        viewModelScope.launch {
            repository.recordWorkout(userWorkout, threshold)
        }
    }
}
