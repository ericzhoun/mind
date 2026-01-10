package com.digitalascetic.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.digitalascetic.app.data.local.dao.DayNoteDao
import com.digitalascetic.app.data.local.dao.ProgramDao
import com.digitalascetic.app.data.local.dao.TaskDao
import com.digitalascetic.app.data.local.dao.UserProgressDao
import com.digitalascetic.app.data.local.entity.DayNoteEntity
import com.digitalascetic.app.data.local.entity.ProgramDayEntity
import com.digitalascetic.app.data.local.entity.ProgramEntity
import com.digitalascetic.app.data.local.entity.TaskEntity
import com.digitalascetic.app.data.local.entity.UserProgressEntity

@Database(
    entities = [
        ProgramEntity::class,
        ProgramDayEntity::class,
        TaskEntity::class,
        UserProgressEntity::class,
        DayNoteEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun programDao(): ProgramDao
    abstract fun taskDao(): TaskDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun dayNoteDao(): DayNoteDao
}
