package com.ced.gweather.weather.di.module

import android.app.Application
import com.ced.gweather.AndroidApplication
import dagger.Binds
import dagger.Module

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
@Module(includes = [FirebaseRepositoryModule::class, DeviceStorageModule::class])
abstract class ApplicationModule {

    @Binds
    abstract fun bindApplication(app: AndroidApplication): Application
}
