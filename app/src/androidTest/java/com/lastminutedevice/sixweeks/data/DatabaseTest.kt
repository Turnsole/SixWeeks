package com.lastminutedevice.sixweeks.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lastminutedevice.sixweeks.data.room.Database
import com.lastminutedevice.sixweeks.data.room.Workout
import com.lastminutedevice.sixweeks.data.room.WorkoutUpdatePartial
import com.lastminutedevice.sixweeks.loader.Loader
import com.lastminutedevice.sixweeks.test.BuildConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assume.assumeThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Testing the actual queries with the data we get from JSON.
 *
 * This test will run on a device, as it requires access to Android's specific implementation
 * of SqlLite. This is intentional so that queries are tested in a realistic environment. Since much
 * of the business logic of this app is encapsulated in SQL queries it is important to have
 * robust testing.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    private val weeks = 6 // It's in the program name, and not expected to change.

    private lateinit var db: Database

    private lateinit var repository: Repository

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.databaseBuilder(context, Database::class.java, "test_database").build()
        repository = Repository(db.dao())

        launch {
            Loader(
                repository = repository,
                context = context
            ).load() // Load up the actual data.
        }

        advanceUntilIdle() // Setup waits for the data to load (context handled by runTest).
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun noWorkoutsDoneAtStart() = runTest {
        // Initially there are no completed tests or workouts.
        assertNull(actual = db.dao().getLastTest().first())
        assertNull(actual = db.dao().mostRecentCompleted().first())
    }

    @Test
    fun testPushups() = runTest {
        assumeThat(
            "Test is only for pushups flavour",
            BuildConfig.skill,
            CoreMatchers.equalTo("pushups")
        )

        var test: Workout? = db.dao().getNextTest()

        var counter: Int = 0

        while (test != null) { // Loop through each week's test.
            counter++

            // Do the test, with the minimum required seconds of planking.
            db.dao().completeWorkout(
                partial = WorkoutUpdatePartial(
                    workoutId = test.workoutId,
                    date = System.currentTimeMillis(),
                    motions = test.testThreshold
                )
            )

            // Go through the next week.

            // Get a new test.
            test = db.dao().getNextTest()
        }

        assertEquals(expected = weeks, actual = counter)
    }

    @Test
    fun testAllPlanks() = runTest {
        assumeThat(
            "Test is only for planks flavour",
            BuildConfig.skill,
            CoreMatchers.equalTo("planks")
        )

        // Check that all the workouts made it into the DB.
        val list = db.dao().loadAllWorkouts().first()
        assertEquals(expected = 42, actual = list.size)
    }


    /**
     * The planks program has only one level, and no tests, so progress through whole program.
     */
    @Test
    fun testPlanks() = runTest {

        assumeThat(
            "Test is only for planks flavour",
            BuildConfig.skill,
            CoreMatchers.equalTo("planks")
        )

        // Check the logic to keep getting the next workout.
        var workout: Workout? = db.dao().getWorkout(1)
        while (workout != null) {
            workout = db.dao().getNextWorkoutThisWeek(
                week = workout.week,
                day = workout.day + 1,
                level = workout.level
            )
        }
    }
}
