package com.lastminutedevice.sixweeks.data

class Repository(val dao: RoomAccessObject) {

    suspend fun saveWorkouts(workouts: List<Workout>) {
        dao.insertWorkouts(workouts)
    }

    suspend fun saveTests(tests: List<Test>) {
        dao.insertTests(tests)
    }

    suspend fun saveWorkoutSets(sets: List<WorkoutSet>) {
        dao.insertSets(sets)
    }

    suspend fun saveRest(rests: List<Rest>) {
        dao.insertRest(rests)
    }
}
