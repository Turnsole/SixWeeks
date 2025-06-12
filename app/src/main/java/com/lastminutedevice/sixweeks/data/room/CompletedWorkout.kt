package com.lastminutedevice.sixweeks.data.room

data class CompletedWorkout(
    val date: Long,
    /**
     * If this was a test it was the user's max effort. It this was a workout it was the sum of
     * reps completed in the workout. Summary may be presented to the user at the end of a program.
     **/
    val motions: Int
)
