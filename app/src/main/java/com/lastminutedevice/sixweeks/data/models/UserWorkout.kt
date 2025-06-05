package com.lastminutedevice.sixweeks.data.models

import com.lastminutedevice.sixweeks.data.room.CompletedWorkout
import java.io.Serializable

data class UserWorkout(
    val id: Long,
    val week: Int,
    val day: Int,
    val level: String,
    val rest: Int,
    val completed: CompletedWorkout? = null,
    val sets: List<Int>,
    val testThreshold: Int? = null
) : Serializable
