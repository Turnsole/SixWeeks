package com.lastminutedevice.sixweeks.data

import androidx.room.Entity

@Entity(primaryKeys = ["week", "day", "program"])
data class Rest(
    val week: Int,
    val day: Int,
    val rest: Int,
    val program: String
)
