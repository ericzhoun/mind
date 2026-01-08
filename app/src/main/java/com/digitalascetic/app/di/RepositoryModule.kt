package com.digitalascetic.app.di

import com.digitalascetic.app.data.repository.ProgramRepositoryImpl
import com.digitalascetic.app.data.repository.UserProgressRepositoryImpl
import com.digitalascetic.app.domain.repository.ProgramRepository
import com.digitalascetic.app.domain.repository.UserProgressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProgramRepository(
        programRepositoryImpl: ProgramRepositoryImpl
    ): ProgramRepository

    @Binds
    @Singleton
    abstract fun bindUserProgressRepository(
        userProgressRepositoryImpl: UserProgressRepositoryImpl
    ): UserProgressRepository
}
