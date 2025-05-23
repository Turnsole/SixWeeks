package com.lastminutedevice.sixweeks.ui.program

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.data.room.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProgramViewModel @Inject constructor(): ViewModel() {

    @Inject lateinit var repository: Repository

    val workouts: LiveData<List<Workout>> = repository.loadWorkouts()

}
