package com.lastminutedevice.sixweeks.data.room

import androidx.room.Entity

@Entity(primaryKeys = ["program", "week"])
data class Test(
    val result: Int,
    val program: String,
    val week: Int,
    val date: Long
)