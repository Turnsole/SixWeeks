package com.lastminutedevice.sixweeks.data

import androidx.room.Entity

@Entity(primaryKeys = ["workoutId", "set"])
data class WorkoutSet(
    val workoutId: Int,
    val set: Int,
    val reps: Int
)
