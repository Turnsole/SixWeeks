package com.lastminutedevice.sixweeks.data

import com.lastminutedevice.sixweeks.data.json.JsonWorkout
import com.lastminutedevice.sixweeks.data.room.RoomAccessObject
import com.lastminutedevice.sixweeks.data.room.Test
import com.lastminutedevice.sixweeks.data.room.Workout
import com.lastminutedevice.sixweeks.data.room.WorkoutSet

class Repository(val dao: RoomAccessObject) {

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

    suspend fun saveTests(tests: List<Test>) {
        dao.insertTests(tests)
    }
}
