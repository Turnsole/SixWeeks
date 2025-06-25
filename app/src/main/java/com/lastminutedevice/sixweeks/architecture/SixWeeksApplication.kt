package com.lastminutedevice.sixweeks.architecture

import android.app.Application
import com.lastminutedevice.sixweeks.data.Repository
import com.lastminutedevice.sixweeks.loader.Loader
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class SixWeeksApplication : Application() {

    @Inject lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            Loader(repository = repository, context = this@SixWeeksApplication).load()
        }
    }
}
