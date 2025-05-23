package com.lastminutedevice.sixweeks.data

import com.lastminutedevice.sixweeks.data.json.JsonWorkout
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import com.lastminutedevice.sixweeks.data.room.RoomAccessObject
import com.lastminutedevice.sixweeks.data.room.Test
import com.lastminutedevice.sixweeks.data.room.Workout
import com.lastminutedevice.sixweeks.data.room.WorkoutSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class Repository @Inject constructor(val dao: RoomAccessObject) {

    suspend fun saveWorkouts(jsonWorkouts: List<JsonWorkout>) {
        jsonWorkouts.forEach { workout ->
            val entity = Workout(
                week = workout.week,
                day = workout.day,
                level = workout.level,
                rest = workout.rest
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
    }

    fun loadWorkouts() : Flow<List<UserWorkout>> {
        val workoutsFlow =  dao.loadAllWorkouts()
        val completedFlow = dao.loadAllCompleted()
        val workoutSetFlow = dao.loadSets()

        return combine(workoutSetFlow, completedFlow, workoutsFlow) { setList, completedList, workoutList ->
            workoutList.map { workout ->
                UserWorkout(
                    week = workout.week,
                    day = workout.day,
                    level = workout.level,
                    rest = workout.rest,
                    completed = completedList.find { completed ->
                        completed.workoutId == workout.workoutId
                    } != null,
                    sets = setList
                        .filter { set -> set.workoutId == workout.workoutId }
                        .sortedBy { set -> set.ordinal }
                        .map { set -> set.reps }
                )
            }
        }
    }

    suspend fun saveTests(tests: List<Test>) {
        dao.insertTests(tests)
    }
}
