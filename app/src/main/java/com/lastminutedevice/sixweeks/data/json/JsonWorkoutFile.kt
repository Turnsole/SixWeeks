package com.lastminutedevice.sixweeks.data.json

/**
 * The JSON files for each program are lists of workouts to be imported into the DB one-by-one.
 */
data class JsonWorkoutFile(val workouts: List<JsonWorkout>)
