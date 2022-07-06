/*
 * Copyright (C) 2018 StepStone Services Sp. z o.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ced.gweather.weather.di.component

import android.content.Context
import com.ced.commons.clean.executor.PostExecutionThread
import com.ced.commons.clean.executor.ThreadExecutor
import com.ced.gweather.AndroidApplication
import com.ced.gweather.auth.ui.LoginFragment
import com.ced.gweather.weather.ui.MainActivity
import com.ced.gweather.weather.di.module.ApplicationModule
import com.ced.gweather.weather.di.module.ViewModelModule
import com.ced.gweather.weather.ui.weatherhome.CurrentWeatherFragment
import com.ced.gweather.weather.ui.weatherhome.WeatherHomeFragment
import com.ced.gweather.weather.ui.weatherhome.WeatherRecordsFragment
import com.ced.gweather_core.internal.di.module.ExecutorModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 *
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ExecutorModule::class, ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent : AndroidInjector<AndroidApplication> {

    fun threadExecutor(): ThreadExecutor
    fun postExecutionThread(): PostExecutionThread

    fun inject(context: Context)
    fun inject(mainActivity: MainActivity)

    fun inject(loginFragment: LoginFragment)

    fun inject(weatherHomeFragment: WeatherHomeFragment)
    fun inject(currentWeatherFragment: CurrentWeatherFragment)
    fun inject(weatherRecordsFragment: WeatherRecordsFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}