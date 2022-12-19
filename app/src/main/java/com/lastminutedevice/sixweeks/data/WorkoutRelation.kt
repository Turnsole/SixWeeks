package com.lastminutedevice.sixweeks.data

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutRelation(
    @Embedded
    val workout: Workout,
    @Relation(parentColumn = "id", entityColumn = "workoutId")
    val sets: List<WorkoutSet>
)
