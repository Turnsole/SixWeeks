package com.lastminutedevice.sixweeks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout (
    val week: Int,
    val day: Int,
    val level: Int,
    @PrimaryKey
    val id: Int,
    val program: String
)
