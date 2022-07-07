package com.ced.gweather.weather.di.module

import com.ced.authentication.domain.repository.SessionRepository
import com.ced.gweather_core.data.memory.SessionMemoryRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 *
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
@Module
abstract class DeviceStorageModule {

    @Singleton
    @Binds
    abstract fun provideSessionRepository(dataSource: SessionMemoryRepository) : SessionRepository

}