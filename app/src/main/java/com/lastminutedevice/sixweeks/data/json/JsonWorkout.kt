package com.lastminutedevice.sixweeks.data.json

/**
 * The Json file doesn't have IDs, and it contains the list
 * of sets as part of the workout (instead of in a separate table like the
 * database does).
 *
 * The testThreshold field is only present on test days.
 */
class JsonWorkout(
    val week: Int,
    val day: Int,
    val level: Int = 0,
    val rest: Int = 0,
    val sets: List<JsonWorkoutSet>,
    val testThreshold: Int = 0
)
