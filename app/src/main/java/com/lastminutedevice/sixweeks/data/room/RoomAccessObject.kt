package com.lastminutedevice.sixweeks.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomAccessObject {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkout(workouts: Workout): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSets(sets: List<WorkoutSet>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTests(tests: List<Test>)

    @Query("select * from workout where week = :week and day = :day and level = :level")
    suspend fun loadWorkout(week: Int, day: Int, level: Int): List<Workout>

    @Query("select * from workout order by week, day")
    fun loadAllWorkouts(): Flow<List<Workout>>

    @Query("select * from completedworkout")
    fun loadAllCompleted(): Flow<List<CompletedWorkout>>

    @Query("select * from workoutset")
    fun loadSets(): Flow<List<WorkoutSet>>
}
