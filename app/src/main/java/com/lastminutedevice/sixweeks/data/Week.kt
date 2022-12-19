package com.lastminutedevice.sixweeks.data

import androidx.room.Entity


@Entity(primaryKeys = ["program", "ordinal"])
data class Week(
    val ordinal: Int,
    val program: String,
    val rest: Int
)
