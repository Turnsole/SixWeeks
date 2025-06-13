package com.lastminutedevice.sixweeks.data

import com.lastminutedevice.sixweeks.data.room.CompletedWorkout
import com.lastminutedevice.sixweeks.data.room.RoomAccessObject
import com.lastminutedevice.sixweeks.data.room.Workout
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class RepositoryTest {

    @Mock
    lateinit var mockWorkout: Workout

    @Mock
    lateinit var mockDatabase: RoomAccessObject

    @InjectMocks
    lateinit var repository: Repository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `first workout fetched when no workouts completed`() = runTest {
        whenever(mockDatabase.getWorkout(1)).thenReturn(mockWorkout)
        whenever(mockDatabase.mostRecentCompleted()).thenReturn(flowOf(null))

        repository.getNextWorkout().first()

        verify(mockDatabase).mostRecentCompleted()
        verify(mockDatabase).getWorkout(workoutId = 1)
        verifyNoMoreInteractions(mockDatabase)
    }

    @Test
    fun `if the most recent workout was today then return`() = runTest {
        val workoutDoneToday = Workout(
            workoutId = 69,
            week = 3,
            day = 3,
            level = 3,
            rest = 3,
            testThreshold = 3,
            sets = listOf(),
            completed = CompletedWorkout(
                date = System.currentTimeMillis(),
                motions = 3
            )
        )

        whenever(mockDatabase.mostRecentCompleted()).thenReturn(flowOf(workoutDoneToday))
        val result = repository.getNextWorkout().first().nextWorkout
        assertEquals(result?.id, workoutDoneToday.workoutId)
    }
}
