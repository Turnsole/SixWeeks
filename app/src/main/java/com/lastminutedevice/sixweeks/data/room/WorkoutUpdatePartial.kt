package com.lastminutedevice.sixweeks.data.room

/**
 * Only the fields needed to update a workout's completed status.
 */
data class WorkoutUpdatePartial(
    val workoutId: Long,
    val date: Long,
    val motions: Int
)
