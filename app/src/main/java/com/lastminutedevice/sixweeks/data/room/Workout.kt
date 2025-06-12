package com.lastminutedevice.sixweeks.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout (
    @PrimaryKey(autoGenerate = true) val workoutId: Long = 0,
    val week: Int,
    val day: Int,
    val level: String,
    val rest: Int,
    /** Some days of some programs are a test of $threshold+ motions. Results recorded to the Test table. **/
    val testThreshold: Int? = null,
    val sets: List<WorkoutSet>,
    @Embedded
    val completed: CompletedWorkout? = null
)
