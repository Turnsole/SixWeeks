package com.lastminutedevice.sixweeks.data.room

import androidx.room.Entity

@Entity(primaryKeys = ["level", "week"])
data class Test(
    val result: Int,
    val level: String,
    val week: Int,
    val date: Long
)