package com.lastminutedevice.sixweeks.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomAccessObject {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkouts(workouts: List<Workout>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSets(workoutSets: List<WorkoutSet>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRest(rests: List<Rest>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTests(tests: List<Test>)

    @Query("select * from workout where program = :program and week = :week and day = :day and level = :level")
    suspend fun loadSets(program: String, week: Int, day: Int, level: Int): WorkoutRelation

    @Query("select * from workout where program = :program order by week, day")
    suspend fun loadAllSets(program: String): List<WorkoutRelation>

    //@Query("select max(result) from test where program = :program and week = :week")
    //suspend fun loadTest(program: String, week: Int): Test
}
