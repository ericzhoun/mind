package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.TaskStatus
import com.digitalascetic.app.domain.model.UserProgress
import com.digitalascetic.app.domain.repository.UserProgressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

class MarkTaskCompleteUseCaseTest {

    private val userProgressRepository = mockk<UserProgressRepository>(relaxed = true)
    private val useCase = MarkTaskCompleteUseCase(userProgressRepository)

    @Test
    fun `invoke updates progress with COMPLETED status`() = runTest {
        val taskId = "task_1"
        val slot = slot<UserProgress>()

        coEvery { userProgressRepository.updateProgress(capture(slot)) } returns Unit

        useCase(taskId)

        coVerify { userProgressRepository.updateProgress(any()) }
        assertEquals(taskId, slot.captured.taskId)
        assertEquals(TaskStatus.COMPLETED, slot.captured.status)
        assert(slot.captured.completedAt != null)
    }
}
