package com.lastminutedevice.sixweeks.data

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["program", "ordinal"])
data class Week(
    val ordinal: Int,
    val program: String,
    val rest: Int
)
