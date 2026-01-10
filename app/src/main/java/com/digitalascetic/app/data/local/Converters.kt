package com.digitalascetic.app.data.local

import androidx.room.TypeConverter
import com.digitalascetic.app.domain.model.ProgramType
import com.digitalascetic.app.domain.model.TaskStatus
import com.digitalascetic.app.domain.model.TaskType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun fromDate(value: String?): java.time.LocalDate? {
        return value?.let { java.time.LocalDate.parse(it, dateFormatter) }
    }

    @TypeConverter
    fun dateToString(date: java.time.LocalDate?): String? {
        return date?.format(dateFormatter)
    }

    @TypeConverter
    fun fromProgramType(value: ProgramType): String = value.name

    @TypeConverter
    fun toProgramType(value: String): ProgramType = ProgramType.valueOf(value)

    @TypeConverter
    fun fromTaskType(value: TaskType): String = value.name

    @TypeConverter
    fun toTaskType(value: String): TaskType = TaskType.valueOf(value)

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus = TaskStatus.valueOf(value)
}
