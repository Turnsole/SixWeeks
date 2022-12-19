package com.lastminutedevice.sixweeks.data

/**
 * This is the structure of the JSON file for each program.
 */
data class Program(
    val levels: List<Level>,
    val weeks: List<Week>,
    val sets: List<WorkoutSet>
)
