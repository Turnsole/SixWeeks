package com.lastminutedevice.sixweeks.architecture

import android.content.Context
import androidx.room.Room
import com.lastminutedevice.sixweeks.data.room.Database
import com.lastminutedevice.sixweeks.data.room.RoomAccessObject
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) : Database {
        return Room.databaseBuilder(appContext, Database::class.java, "database").build()
    }

    @Provides
    fun provideDao(database: Database) : RoomAccessObject {
        return database.dao()
    }
}
