package com.lastminutedevice.sixweeks.data

class Repository(val dao: RoomAccessObject) {

    suspend fun saveLevels(levels: List<Level>) {
        dao.insertLevels(levels)
    }

    suspend fun saveWeeks(weeks: List<Week>) {
        dao.insertWeeks(weeks)
    }

    suspend fun saveWorkoutSets(sets: List<WorkoutSet>) {
        dao.insertSets(sets)
    }

    suspend fun saveTests(tests: List<Test>) {
        dao.insertTests(tests)
    }

    suspend fun getSets(program: String, week: Int, level: Int): List<WorkoutSet> {
        return dao.loadAllSets(program = program)
    }

    suspend fun getSet(program: String, week: Int, level: Int): List<WorkoutSet> {
        return dao.loadSets(program, week, level)
    }

    //suspend fun get(program: String, week: Int): Test {
    //    return dao.loadTest(program, week)
    //}
}
