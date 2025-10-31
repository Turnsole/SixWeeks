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

    @Query("select * from workout where workoutId = (select max(workoutId) from workout where date is not null and testThreshold > 0)")
    fun getLastTest(): Flow<Workout?>

    /**
     * Returns the next workout for this week, this day and level.
     *
     * @param week this week
     * @param day today (not the day you're looking for, let the query figure that out)
     * @param level the level of the current workout
     *
     * @return null if there are no more workouts this week.
     */
    @Query("select * from workout where week = :week and day = (:day + 1) and level = :level")
    suspend fun getNextWorkoutThisWeek(week: Int, day: Int, level: Int) : Workout?

    /**
     * Returns the first workout of next week for this level.
     *
     * @return null if the level ends this week (time for a test).
     */
    @Query("select * from workout where week = (:week + 1) and day = 1 and level = :level")
    suspend fun getFirstWorkoutNextWeek(week: Int, level: Int) : Workout?

    /**
     * Finds the next workout which is an uncompleted test.
     *
     * @return the next test's threshold
     */
    @Query("select * from workout where workoutId = (select min(workoutId) from workout where date is null and testThreshold > 0)")
    suspend fun getNextTest() : Workout?

    @Query("select count(*) from workout")
    suspend fun totalWorkouts() : Int
}
