package com.lastminutedevice.sixweeks.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutId"],
            // Updating the loaded workouts doesn't erase user data.
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class CompletedWorkout(
    @PrimaryKey val workoutId: Long,
    val date: Long
)
