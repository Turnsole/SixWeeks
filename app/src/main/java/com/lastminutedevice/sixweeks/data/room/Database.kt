package com.lastminutedevice.sixweeks.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Workout::class, Test::class, WorkoutSet::class],
    version = 1,
    exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun dao(): RoomAccessObject
}