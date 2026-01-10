package com.digitalascetic.app.di

import android.content.Context
import androidx.room.Room
import com.digitalascetic.app.data.local.AppDatabase
import com.digitalascetic.app.data.local.dao.DayNoteDao
import com.digitalascetic.app.data.local.dao.ProgramDao
import com.digitalascetic.app.data.local.dao.TaskDao
import com.digitalascetic.app.data.local.dao.UserProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "digital_ascetic_db"
        )
        .fallbackToDestructiveMigration() // Recreate DB on schema changes (dev mode)
        .build()
    }

    @Provides
    fun provideProgramDao(database: AppDatabase): ProgramDao = database.programDao()

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    @Provides
    fun provideUserProgressDao(database: AppDatabase): UserProgressDao = database.userProgressDao()
    
    @Provides
    fun provideDayNoteDao(database: AppDatabase): DayNoteDao = database.dayNoteDao()
}
