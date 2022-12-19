package com.lastminutedevice.sixweeks.data

import androidx.room.Entity

@Entity(primaryKeys = ["level", "week", "program"])
data class Level (
    val level: Int,
    val week: Int,
    val program: String
)
