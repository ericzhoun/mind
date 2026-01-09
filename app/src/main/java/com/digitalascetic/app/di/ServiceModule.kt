package com.digitalascetic.app.di

import com.digitalascetic.app.data.service.MockReflectionAI
import com.digitalascetic.app.domain.service.InterventionManager
import com.digitalascetic.app.domain.service.InterventionManagerImpl
import com.digitalascetic.app.domain.service.ReflectionAI
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
    abstract fun bindReflectionAI(
        mockReflectionAI: MockReflectionAI
    ): ReflectionAI

    @Binds
    @Singleton
    abstract fun bindInterventionManager(
        interventionManagerImpl: InterventionManagerImpl
    ): InterventionManager
}
