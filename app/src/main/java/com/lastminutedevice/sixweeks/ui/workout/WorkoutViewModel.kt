package com.lastminutedevice.sixweeks.ui.workout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val tag : String = WorkoutViewModel::class.java.simpleName

    private val _currentReps = MutableLiveData<Int?>()

    private val _viewState = MutableLiveData<ViewState>()

    private val _countDown = MutableLiveData<Int>()

    private lateinit var _sets : ArrayDeque<Int>

    private var _rest : Int? = null

    private lateinit var _workout : UserWorkout

    /** The current number of reps to display. */
    val reps : LiveData<Int?> = _currentReps

    /** The UI to display. */
    val viewState : LiveData<ViewState> = _viewState

    /** Where in the rest countdown one is. */
    val countDown : LiveData<Int> = _countDown

    init {
        viewModelScope.launch {
            repository.getNextWorkout().collectLatest { result ->
                result.nextWorkout
                    ?.let { workout ->
                        _sets = ArrayDeque<Int>(workout.sets)
                        _rest = 10 // workout.rest TODO
                        _workout = workout
                        _currentReps.postValue(_sets.removeFirstOrNull())
                        _viewState.postValue(ViewState.SET)
                    }
                    ?.run { Log.e(tag, "No next workout") }
            }
        }
    }

    /**
     * Each time the user completes a set, run down the rest timer or
     * emit a dismiss state as appropriate.
     */
    fun completeSet() {
        val nextSet = _sets.removeFirstOrNull()
        if (nextSet == null) {
            viewModelScope.launch {
                repository.recordWorkout(
                    userWorkout = _workout,
                    maxEffort = _workout.sets.sum() // whole workout only, for now
                )
                _viewState.postValue(ViewState.DISMISS)
            }
        } else {
            _currentReps.postValue(nextSet)
            _viewState.postValue(ViewState.REST)

            viewModelScope.launch {
                if (_rest != null) {
                    for (i in _rest!! downTo 0) {
                        _countDown.postValue(i)
                        if (i > 0) { delay(1000L) }
                    }
                }
                _viewState.postValue(ViewState.SET)
            }
        }
    }

    enum class ViewState {
        SET,
        REST,
        DISMISS
    }
}
