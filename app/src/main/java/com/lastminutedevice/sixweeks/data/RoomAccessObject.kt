package com.lastminutedevice.sixweeks.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomAccessObject {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertLevels(levels: List<Level>)

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertSets(workoutSets: List<WorkoutSet>)

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertTests(tests: List<Test>)

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertWeeks(weeks: List<Week>)

    @Query("select * from workoutset where program = :program and week = :week and level = :level")
    suspend fun loadSets(program: String, week: Int, level: Int): List<WorkoutSet>

    @Query("select * from workoutset where program = :program")
    suspend fun loadAllSets(program: String): List<WorkoutSet>

    //@Query("select max(result) from test where program = :program and week = :week")
    //suspend fun loadTest(program: String, week: Int): Test
}
