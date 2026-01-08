package com.digitalascetic.app.di

import com.digitalascetic.app.domain.service.GeminiReflectionEngine
import com.digitalascetic.app.domain.service.InterventionManager
import com.digitalascetic.app.domain.service.InterventionManagerImpl
import com.digitalascetic.app.domain.service.ReflectionEngine
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindReflectionEngine(
        geminiReflectionEngine: GeminiReflectionEngine
    ): ReflectionEngine

    @Binds
    @Singleton
    abstract fun bindInterventionManager(
        interventionManagerImpl: InterventionManagerImpl
    ): InterventionManager
}
