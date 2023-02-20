package com.lastminutedevice.sixweeks.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true) val setId: Long = 0,
    val ordinal: Int,
    val workoutId: Long,
    val reps: Int
)
