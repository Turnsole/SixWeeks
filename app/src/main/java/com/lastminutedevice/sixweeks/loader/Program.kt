package com.lastminutedevice.sixweeks.loader

import com.lastminutedevice.sixweeks.data.Rest
import com.lastminutedevice.sixweeks.data.Workout
import com.lastminutedevice.sixweeks.data.WorkoutSet

/**
 * This is the structure of the JSON file for each program.
 */
data class Program(
    val workouts: List<Workout>,
    val sets: List<WorkoutSet>,
    val rests: List<Rest>
)
