package com.lastminutedevice.sixweeks.data.models

data class UserWorkout(
    val week: Int,
    val day: Int,
    val level: String,
    val rest: Int,
    val completed: Boolean,
    val sets: List<Int>,
    val testThreshold: Int? = null
)
