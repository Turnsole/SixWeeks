package com.lastminutedevice.sixweeks.data

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["level", "week", "program"])
data class Level (
    val level: Int,
    val week: Int,
    val program: String
)
