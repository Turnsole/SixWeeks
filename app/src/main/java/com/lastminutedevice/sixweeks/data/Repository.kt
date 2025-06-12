package com.lastminutedevice.sixweeks.data

import android.util.Log
import androidx.room.Transaction
import com.lastminutedevice.sixweeks.NextWorkoutResult
import com.lastminutedevice.sixweeks.data.json.JsonWorkout
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import com.lastminutedevice.sixweeks.data.room.CompletedWorkout
import com.lastminutedevice.sixweeks.data.room.RoomAccessObject
import com.lastminutedevice.sixweeks.data.room.Workout
import com.lastminutedevice.sixweeks.data.room.WorkoutSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(val dao: RoomAccessObject) {

    private val tag: String = this::class.java.simpleName

    @Transaction
    suspend fun saveWorkouts(jsonWorkouts: List<JsonWorkout>) {
        jsonWorkouts.forEach { workout ->
            val entity = Workout(
                week = workout.week,
                day = workout.day,
                level = workout.level,
                rest = workout.rest,
                testThreshold = workout.testThreshold
            )
            val workoutId = dao.insertWorkout(entity)
            val sets = workout.sets.map { set ->
                WorkoutSet(
                    ordinal = set.ordinal,
                    // Generated when the workout was created in the DB.
                    workoutId = workoutId,
                    reps = set.reps
                )
            }
            dao.insertSets(sets)
        }
        if (jsonWorkouts.isEmpty()) {
            Log.e(tag, "Workouts list was empty.")
        } else {
            Log.d(tag, "Loaded ${jsonWorkouts.size} workouts.")
        }
    }

    /**
        Get the last completed workout. If the date is today, then it was completed today,
        so load that workout from the DAO and emit it.

        If the last completed workout is from a date previous to today, then get the next
        workout based on the (auto-incremented) ID. (When available.)

        If there is no workout with the next ID, emit a state with no workout
        but programComplete is true.
     */
    fun getNextWorkout() : Flow<NextWorkoutResult> {
        return flow { emit(NextWorkoutResult(nextWorkout = null, programComplete = true)) }
    }

    fun loadWorkouts() : Flow<List<UserWorkout>> {
        val workoutsFlow =  dao.loadAllWorkouts()
        val completedFlow = dao.loadAllCompleted()
        val setFlow = dao.loadSets()

        return combine(setFlow, completedFlow, workoutsFlow) { setList, completedList, workoutList ->
            workoutList.map { workout ->
                UserWorkout(
                    id = workout.workoutId,
                    week = workout.week,
                    day = workout.day,
                    level = workout.level,
                    rest = workout.rest,
                    completed = completedList.find { completed ->
                        completed.workoutId == workout.workoutId
                    },
                    sets = setList
                        .filter { set -> set.workoutId == workout.workoutId }
                        .sortedBy { set -> set.ordinal }
                        .map { set -> set.reps },
                    testThreshold = workout.testThreshold
                )
            }
        }
    }

    suspend fun recordWorkout(userWorkout: UserWorkout, maxEffort: Int? = null) {
        val completedWorkout = CompletedWorkout(
            workoutId = userWorkout.id,
            date = System.currentTimeMillis(),
            motions = maxEffort ?: userWorkout.sets.sum()
        )
        dao.insertCompletedWorkout(completedWorkout)
    }
}
