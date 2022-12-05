package com.lastminutedevice.sixweeks.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface RoomAccessObject {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertLevels(vararg levels: Level)

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertSets(vararg workoutSets: WorkoutSet)

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertTests(vararg tests: Test)

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertWeeks(vararg weeks: Week)

    @Query("select * from workoutset where program = :program and week = :week and level = :level")
    suspend fun loadSets(program: String, week: Int, level: Int): List<WorkoutSet>

    @Query("select * from workoutset where program = :program")
    suspend fun loadAllSets(program: String, week: Int, level: Int): List<WorkoutSet>

    @Query("select max(result) from test where program = :program and week = :week")
    suspend fun loadTest(program: String, week: Int): Test
}
