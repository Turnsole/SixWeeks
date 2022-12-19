package com.lastminutedevice.sixweeks.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Level::class, Test::class, Week::class, WorkoutSet::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dao() : RoomAccessObject
}
