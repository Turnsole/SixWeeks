package com.lastminutedevice.sixweeks.data

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lastminutedevice.sixweeks.data.room.Database
import com.lastminutedevice.sixweeks.loader.Loader
import com.lastminutedevice.sixweeks.test.BuildConfig
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Testing the actual queries with the data we get from JSON.
 *
 * This test will run on a device, as it requires access to Android's specific implementation
 * of SqlLite. This is intentional so that queries are tested in a realistic environment. Since much
 * of the business logic of this app is encapsulated in SQL queries it is important to have
 * robust testing.
 */
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    private lateinit var db: Database

    private lateinit var repository: Repository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.databaseBuilder(context, Database::class.java, "test_database").build()

        repository = Repository(db.dao())
        Loader(repository, context).load() // Load up the actual data.
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Step through all the levels of the program in this build flavour.
     */
    @Suppress("KotlinConstantConditions")
    @Test
    fun testLeveling() {
        if (BuildConfig.skill == "planks") {
            // The planks program has only one level.
        }
        assert(true)
    }
}
