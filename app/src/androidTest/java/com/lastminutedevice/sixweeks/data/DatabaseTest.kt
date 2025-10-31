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
 * Testing the actual queries against a live DB, using known (hard-coded) values from the program.
 *
 * This test will run on a device, as it requires access to Android's specific implementation
 * of SqlLite. This is intentional so that queries are tested in a realistic environment. Since much
 * of the business logic of this app is encapsulated in SQL queries it is important to have
 * robust testing.
 *
 * This will also validate the JSON data bundled with the app.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DatabaseTest {

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
    fun `no workouts are completed at db creation time`() = runTest {
        // Initially there are no completed tests or workouts.
        assertNull(actual = db.dao().getLastTest().first())
        assertNull(actual = db.dao().mostRecentCompleted().first())
    }

    @Test
    fun `do all five tests by completing minimum pushup reps`() = runTest {
        assumeThat(
            "Test is only for pushups flavour",
            BuildConfig.skill,
            CoreMatchers.equalTo("pushups")
        )

        var test: Workout? = db.dao().getNextTest()

        var counter: Int = 0

        while (test != null) { // Loop through each week's test.
            counter++

            db.dao().completeWorkout(   // Complete each test.
                partial = WorkoutUpdatePartial(
                    workoutId = test.workoutId,
                    date = System.currentTimeMillis(),
                    motions = test.testThreshold // Minimum required seconds of planking.
                )
            )

            test = db.dao().getNextTest() // Get a new test.
        }

        assertEquals(expected = 5, actual = counter)
    }

    /**
     * This tests the entire planks program. It has only one level, and no tests (thus no weeks),
     * so progress through every workout in order of day.
     */
    @Test
    fun `all plank workouts - next workout this week`() = runTest {

        assumeThat(
            "Test is only for planks flavour",
            BuildConfig.skill,
            CoreMatchers.equalTo("planks")
        )

        // Check the logic to keep getting the next workout. Planks program only has week 1.
        var counter: Int = 0
        var workout: Workout? = db.dao().getWorkout(1)
        while (workout != null) {
            counter ++
            workout = db.dao().getNextWorkoutThisWeek(week = 1, day = workout.day, level = 0)
        }
        assertEquals(message = "Did not progress to 42 workouts.", expected = 42, actual = counter)
    }
}
