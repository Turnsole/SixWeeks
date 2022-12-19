package com.lastminutedevice.sixweeks.loader

import android.content.Context
import android.util.Log
import com.lastminutedevice.sixweeks.data.Repository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Loads JSON files packaged with the app into the Repository.
 */
class Loader(private val repository: Repository, private val context: Context) {

    private val tag: String = this::class.java.simpleName

    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    private val loaderScope = CoroutineScope(Job() + Dispatchers.Main)

    fun load() {
        loaderScope.launch {
            importProgram(name = "pushups")
        }
    }

    private suspend fun importProgram(name: String) {
        val json: String = loadAssetTextAsString(name = name)
        val jsonAdapter: JsonAdapter<Program> = moshi.adapter(Program::class.java)
        val deserializedObject: Program? = jsonAdapter.fromJson(json)

        if (deserializedObject != null) {
            repository.saveWorkouts(workouts = deserializedObject.workouts)
            repository.saveWorkoutSets(sets = deserializedObject.sets)
            repository.saveRest(rests = deserializedObject.rests)
        } else {
            Log.e(tag, "Failed to deserialize level.")
        }
    }

    private fun loadAssetTextAsString(name: String): String {
        var inputReader: BufferedReader? = null
        try {
            val buffer = StringBuilder()
            val inputStream: InputStream = context.assets.open("$name.json")
            inputReader = BufferedReader(InputStreamReader(inputStream))
            var string: String?
            var isFirst = true
            while (inputReader.readLine().also { string = it } != null) {
                if (isFirst) isFirst = false else buffer.append('\n')
                buffer.append(string)
            }
            return buffer.toString()
        } catch (e: IOException) {
            Log.e(tag, "Failure opening $name")
        } finally {
            try {
                inputReader?.close()
            } catch (e: IOException) {
                Log.e(tag, "Failure closing $name")
            }
        }
        throw Exception("Couldn't load asset file.")
    }
}
