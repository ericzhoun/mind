package com.digitalascetic.app.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.digitalascetic.app.data.local.dao.ProgramDao
import com.digitalascetic.app.data.local.dao.TaskDao
import com.digitalascetic.app.data.local.entity.ProgramDayEntity
import com.digitalascetic.app.data.local.entity.ProgramEntity
import com.digitalascetic.app.data.local.entity.TaskEntity
import com.digitalascetic.app.domain.model.ProgramType
import com.digitalascetic.app.domain.model.TaskType
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PolymorphicTaskTest {
    private lateinit var programDao: ProgramDao
    private lateinit var taskDao: TaskDao
    private lateinit var db: AppDatabase

    @BeforeEach
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        programDao = db.programDao()
        taskDao = db.taskDao()
    }

    @AfterEach
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadPolymorphicTasks() = runBlocking {
        // 1. Create Program
        val program = ProgramEntity(
            id = "prog_1",
            title = "Test Program",
            description = "Desc",
            type = ProgramType.LINEAR,
            durationDays = 21
        )
        programDao.insertProgram(program)

        // 2. Create Day
        val day = ProgramDayEntity(
            id = "day_1",
            programId = "prog_1",
            dayIndex = 1,
            theme = "Stoic",
            instructionText = "Test"
        )
        programDao.insertProgramDay(day)

        // 3. Create Tasks with different payloads
        val stoicTask = TaskEntity(
            id = "task_stoic",
            dayId = "day_1",
            type = TaskType.CHECKLIST,
            title = "Cold Shower",
            payloadJson = "{\"duration_min\": 2}",
            appBlockRulesJson = null
        )

        val vipassanaTask = TaskEntity(
            id = "task_vip",
            dayId = "day_1",
            type = TaskType.TIMED,
            title = "Meditation",
            payloadJson = "{\"start\": \"04:30\", \"end\": \"06:30\"}",
            appBlockRulesJson = "{\"strict\": true}"
        )

        taskDao.insertTask(stoicTask)
        taskDao.insertTask(vipassanaTask)

        // 4. Verify retrieval
        val tasks = taskDao.getTasksForDay("day_1")
        assertEquals(2, tasks.size)
        
        val retrievedStoic = tasks.find { it.id == "task_stoic" }
        assertEquals(TaskType.CHECKLIST, retrievedStoic?.type)
        assertEquals("{\"duration_min\": 2}", retrievedStoic?.payloadJson)

        val retrievedVip = tasks.find { it.id == "task_vip" }
        assertEquals(TaskType.TIMED, retrievedVip?.type)
        assertEquals("{\"strict\": true}", retrievedVip?.appBlockRulesJson)
    }
}
