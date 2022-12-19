package com.lastminutedevice.sixweeks.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Workout::class, Test::class, WorkoutSet::class, Rest::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dao(): RoomAccessObject
}
