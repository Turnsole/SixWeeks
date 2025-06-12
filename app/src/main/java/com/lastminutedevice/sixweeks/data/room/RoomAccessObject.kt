package com.lastminutedevice.sixweeks.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
@Dao
interface RoomAccessObject {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkout(workout: Workout): Long

    @Query("select * from workout order by week, day")
    fun loadAllWorkouts(): Flow<List<Workout>>

    @Update(entity = Workout::class)
    suspend fun completeWorkout(partial: WorkoutUpdatePartial)

    @Query("select * from workout where date = (select max(date) from workout) limit 1")
    fun mostRecentCompleted(): Flow<Workout?>

    @Query("select * from workout where workoutId = :workoutId")
    suspend fun getWorkout(workoutId: Long): Workout?
}
