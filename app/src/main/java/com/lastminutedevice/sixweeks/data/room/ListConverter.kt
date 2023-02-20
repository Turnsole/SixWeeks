package com.lastminutedevice.sixweeks.data.room

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Provides conversion for lists into the embedded table (stored as a String)
 * containing lists of workout sets.
 */
class ListConverter {

    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun convertJsonToListObject(json: String): List<WorkoutSet> {
        return moshi.adapter<List<WorkoutSet>>(
            Types.newParameterizedType(
                List::class.java,
                WorkoutSet::class.java
            )
        )
            .fromJson(json).orEmpty()
    }

    @TypeConverter
    fun convertListObjectToJson(objectData: List<WorkoutSet>): String {
        return moshi.adapter<List<WorkoutSet>>(
            Types.newParameterizedType(
                List::class.java,
                WorkoutSet::class.java
            )
        )
            .toJson(objectData)
    }
}