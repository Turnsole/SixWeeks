package com.lastminutedevice.sixweeks.data

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["program", "week", "level", "ordinal"])
data class WorkoutSet(
    val week: Int,
    val level: Int,
    val ordinal: Int,
    val completed: Boolean,
    val program: String,
    val reps: Int
)
