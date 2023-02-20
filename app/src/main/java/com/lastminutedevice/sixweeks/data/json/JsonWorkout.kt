package com.lastminutedevice.sixweeks.data.json

/**
 * The Json file doesn't have IDs, and it contains the list
 * of sets as part of the workout (instead of in a separate table like the
 * database does).
 */
class JsonWorkout(
    val week: Int,
    val day: Int,
    val level: String,
    val rest: Int = 45, // TODO add the real rests to the Json file
    val sets: List<JsonWorkoutSet>
)
