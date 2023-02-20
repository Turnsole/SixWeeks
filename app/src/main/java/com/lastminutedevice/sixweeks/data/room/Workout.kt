package com.lastminutedevice.sixweeks.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout (
    @PrimaryKey(autoGenerate = true) val workoutId: Long = 0,
    val week: Int,
    val day: Int,
    val level: String,
    val rest: Int
)
