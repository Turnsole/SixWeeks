package com.lastminutedevice.sixweeks.architecture

import android.app.Application
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.loader.Loader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SixWeeksApplication : Application() {

    /**
     * Only contains valid data after onCreate.
     */
    @Inject lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        Loader(context = this, repository = repository).load()
    }
}
