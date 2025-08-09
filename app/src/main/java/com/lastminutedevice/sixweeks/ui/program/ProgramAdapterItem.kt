package com.lastminutedevice.sixweeks.ui.program

import com.lastminutedevice.sixweeks.data.models.UserWorkout

data class ProgramAdapterItem(
    val week : Int,
    val workout: UserWorkout? = null
) : Comparable<ProgramAdapterItem> {

    // Returns zero if this object is equal to the specified other object,
    // a negative number if it's less than other,
    // or a positive number if it's greater than other.
    override fun compareTo(other: ProgramAdapterItem): Int {
        // Sort first by week
        if (this.week < other.week) return -1
        if (this.week > other.week) return 1

        // Within a week, put the header on top.
        if (this.workout == null) return -1
        if (other.workout == null) return 1

        // Sort the remaining items by day.
        if (this.workout.day < other.workout.day) return -1
        if (this.workout.day > other.workout.day) return 1

        return 0
    }
}
