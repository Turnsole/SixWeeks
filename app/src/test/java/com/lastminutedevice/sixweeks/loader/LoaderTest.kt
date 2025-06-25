package com.lastminutedevice.sixweeks.loader

import android.content.Context
import android.content.res.AssetManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lastminutedevice.sixweeks.data.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.ByteArrayInputStream


class LoaderTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockRepository: Repository

    @Mock
    lateinit var mockContext: Context

    @Mock
    lateinit var mockAssets: AssetManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadTest() = runTest {

        // Return an empty list.
        whenever(mockRepository.loadWorkouts()).thenReturn(flowOf(listOf()))

        // Open this string when prompted instead of the asset file.
        whenever(mockContext.assets).thenReturn(mockAssets)
        whenever(mockAssets.open(any())).thenReturn(ByteArrayInputStream("{\"workouts\" : []}\n".toByteArray()))

        Loader(
            repository = mockRepository,
            context = mockContext,
            dispatcher = UnconfinedTestDispatcher()
        ).load()

        advanceUntilIdle()

        verify(mockRepository).saveWorkouts(listOf()) // Empty list got saved.
    }
}
