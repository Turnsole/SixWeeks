package com.lastminutedevice.sixweeks.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomAccessObject {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkout(workouts: Workout): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSets(sets: List<WorkoutSet>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTests(tests: List<Test>)

    @Query("select * from workout where week = :week and day = :day and level = :level")
    suspend fun loadSets(week: Int, day: Int, level: Int): List<Workout>

    @Query("select * from workout order by week, day")
    suspend fun loadAllSets(): List<Workout>

    //@Query("select max(result) from test where program = :program and week = :week")
    //suspend fun loadTest(program: String, week: Int): Test
}
