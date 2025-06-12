package com.lastminutedevice.sixweeks.data

import android.util.Log
import androidx.room.Transaction
import com.lastminutedevice.sixweeks.NextWorkoutResult
import com.lastminutedevice.sixweeks.data.json.JsonWorkout
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import com.lastminutedevice.sixweeks.data.room.RoomAccessObject
import com.lastminutedevice.sixweeks.data.room.Workout
import com.lastminutedevice.sixweeks.data.room.WorkoutSet
import com.lastminutedevice.sixweeks.data.room.WorkoutUpdatePartial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(val dao: RoomAccessObject) {

    private val tag: String = this::class.java.simpleName

    @Transaction
    suspend fun saveWorkouts(jsonWorkouts: List<JsonWorkout>) {
        jsonWorkouts.forEach { workout ->
            val sets = workout.sets.map { set ->
                WorkoutSet(
                    ordinal = set.ordinal,
                    reps = set.reps
                )
            }
            val entity = Workout(
                week = workout.week,
                day = workout.day,
                level = workout.level,
                rest = workout.rest,
                testThreshold = workout.testThreshold,
                sets = sets
            )
            dao.insertWorkout(workout = entity)
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
         return dao.mostRecentCompleted().map { completedWorkout ->
             val entity = if (completedWorkout == null) {
                 dao.getWorkout(workoutId = 1) // No completed workouts, so start at the beginning.
             } else if (completedWorkout.isToday()) {
                completedWorkout // Show the workout you did today.
            } else {
                dao.getWorkout(workoutId = completedWorkout.workoutId + 1) // Get the next workout.
             }
             if (entity != null) {
                 NextWorkoutResult(
                     nextWorkout = UserWorkout(
                         id = entity.workoutId,
                         week = entity.week,
                         day = entity.day,
                         level = entity.level,
                         rest = entity.rest,
                         completed = entity.completed,
                         sets = entity.sets.sortedBy { it.ordinal }.map { it.reps },
                         testThreshold = entity.testThreshold
                     ),
                     programComplete = false
                 )
             } else {
                 NextWorkoutResult(nextWorkout = null, programComplete = true)
             }
        }
    }

    /**
     * Checks if the epoch timestamp (in milliseconds) represents a date that is today.
     *
     * @return True if the timestamp is from today, false otherwise.
     */
    fun Workout?.isToday(): Boolean {
        if (this == null || this.completed == null) return false
        val localDateForTimestamp = Instant.ofEpochMilli(this.completed.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return LocalDate.now(ZoneId.systemDefault()) == localDateForTimestamp
    }

    fun loadWorkouts() : Flow<List<UserWorkout>> {
        return dao.loadAllWorkouts().map { workoutList ->
            workoutList.map { workout ->
                UserWorkout(
                    id = workout.workoutId,
                    week = workout.week,
                    day = workout.day,
                    level = workout.level,
                    rest = workout.rest,
                    completed = workout.completed,
                    sets = workout.sets.map { it.reps }, // Just need the reps for the UI.
                    testThreshold = workout.testThreshold
                )
            }
        }
    }

    suspend fun recordWorkout(userWorkout: UserWorkout, maxEffort: Int? = null) {
        val partial = WorkoutUpdatePartial(
            workoutId = userWorkout.id,
            date = System.currentTimeMillis(),
            motions = maxEffort ?: userWorkout.sets.sum()
        )
        dao.completeWorkout(partial)
    }
}
