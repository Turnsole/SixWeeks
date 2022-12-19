package com.lastminutedevice.sixweeks.architecture

import android.app.Application
import androidx.room.Room
import com.lastminutedevice.sixweeks.data.Database
import com.lastminutedevice.sixweeks.data.Loader
import com.lastminutedevice.sixweeks.data.Repository

class SixWeeksApplication : Application() {

    /**
     * Only contains valid data after onCreate.
     */
    val repository: Repository by lazy {
        val db = Room.databaseBuilder(this, Database::class.java, "database").build()
        Repository(dao = db.dao())
    }

    override fun onCreate() {
        super.onCreate()
        Loader(context = this, repository = repository).load()
    }
}
