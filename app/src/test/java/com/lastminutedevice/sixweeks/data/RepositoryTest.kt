package com.lastminutedevice.sixweeks.data

import com.lastminutedevice.sixweeks.data.json.JsonWorkout
import com.lastminutedevice.sixweeks.data.room.CompletedWorkout
import com.lastminutedevice.sixweeks.data.room.RoomAccessObject
import com.lastminutedevice.sixweeks.data.room.Workout
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class RepositoryTest {

    @Mock
    lateinit var mockDao: RoomAccessObject

    lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = Repository(mockDao)
    }

    @Test
    fun `every workout in the JSON data is saved`() = runTest {
        val mockWorkout = Mockito.mock<JsonWorkout>()
        val list = listOf<JsonWorkout>(mockWorkout, mockWorkout, mockWorkout)
        repository.saveWorkouts(list)
        verify(
            mock = mockDao,
            mode = times(numInvocations = list.size)
        ).insertWorkout(workout = any())
    }

    @Test
    fun `getNextWorkout - user has no workouts completed`() = runTest {
        whenever(mockDao.mostRecentCompleted()).thenReturn(flowOf(null))
        repository.getNextWorkout().first()

        // Verify that the first workout was fetched. (Don't bother mocking it.)
        verify(
            mock = mockDao,
            mode = times(1)
        ).getWorkout(1)
    }

    @Test
    fun `getNextWorkout - user worked out today`() = runTest {
        val testWorkoutId = 999L
        val mockWorkout = mockWorkout(date = System.currentTimeMillis(), workoutId = testWorkoutId)
        whenever(mockDao.mostRecentCompleted()).thenReturn(flowOf(mockWorkout))

        // Since the dao provides a most-recent-workout today, that is used.
        val result = repository.getNextWorkout().first()
        assertEquals(testWorkoutId, result.nextWorkout?.id)
    }

    @Test
    fun `isToday - most recent workout was NOT today`() = runTest {
        val mockWorkout = mockWorkout(date = 0, workoutId = 99L)
        whenever(mockDao.mostRecentCompleted()).thenReturn(flowOf(mockWorkout))

        // Since the dao provides a most-recent-workout in the past, go to this week.
        repository.getNextWorkout().first()

        // Verify that the first workout was fetched. (Don't bother mocking it.)
        verify(
            mock = mockDao,
            mode = times(1)
        ).getNextWorkoutThisWeek(
            0,
            0,
            0
        )
    }

    @Test
    fun `getNextWorkout - next workout is next week`() = runTest {
        val thisWeek = 3
        val level = 2

        // At least one workout has been completed, but not today.
        val mockWorkout = mockWorkout(date = 0, workoutId = 99L, week = thisWeek, level = level)
        whenever(mockDao.mostRecentCompleted()).thenReturn(flowOf(mockWorkout))

        // There are no more workouts this week.
        whenever(mockDao.getNextWorkoutThisWeek(any(), any(), any())).thenReturn(null)

        // Verify that the first workout next week was fetched. (Don't bother mocking it.)
        repository.getNextWorkout().first()
        verify(
            mock = mockDao,
            mode = times(1)
        ).getFirstWorkoutNextWeek(
            week = thisWeek,
            level = level
        )
    }

    @Test
    fun `getNextWorkout - final test`() = runTest {
        val thisWeek = 3
        val level = 2

        // At least one workout has been completed, but not today.
        val mockWorkout = mockWorkout(date = 0, workoutId = 99L, week = thisWeek, level = level)
        whenever(mockDao.mostRecentCompleted()).thenReturn(flowOf(mockWorkout))

        // There are no more workouts this week.
        whenever(mockDao.getNextWorkoutThisWeek(any(), any(), any())).thenReturn(null)

        // Verify that the first workout next week was fetched. (Don't bother mocking it.)
        repository.getNextWorkout().first()
        verify(
            mock = mockDao,
            mode = times(1)
        ).getNextTest()
    }

    fun mockWorkout(
        date: Long = 0,
        workoutId: Long = 0,
        week: Int = 0,
        day: Int = 0,
        level: Int = 0
    ) : Workout {
        val mock = mock<Workout>()
        whenever(mock.completed).thenReturn(CompletedWorkout(date = date, motions = 0))
        whenever(mock.workoutId).thenReturn(workoutId)
        whenever(mock.week).thenReturn(week)
        whenever(mock.day).thenReturn(day)
        whenever(mock.level).thenReturn(level)
        return mock
    }
}
