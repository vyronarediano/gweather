package com.ced.gweather.weather.di.module

import com.ced.authentication.data.firestore.UserFireStoreRepository
import com.ced.authentication.domain.repository.UserRepository
import com.ced.gweather_core.data.firestore.WeatherFireStoreRepository
import com.ced.gweather_core.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
@Module
abstract class FirebaseRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(repository: UserFireStoreRepository): UserRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(repository: WeatherFireStoreRepository): WeatherRepository

}