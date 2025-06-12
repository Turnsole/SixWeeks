package com.lastminutedevice.sixweeks

import com.lastminutedevice.sixweeks.data.models.UserWorkout

data class NextWorkoutResult(
    val nextWorkout: UserWorkout?,
    val programComplete: Boolean
)
