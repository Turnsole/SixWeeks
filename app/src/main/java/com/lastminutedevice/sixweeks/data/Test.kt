package com.lastminutedevice.sixweeks.data

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["program", "week"])
data class Test(
    val result: Int,
    val program: String,
    val week: Int,
    val date: Long
)
