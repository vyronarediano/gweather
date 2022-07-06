package com.ced.gweather.weather.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ced.gweather.auth.features.AuthenticateViewModel
import com.ced.gweather.weather.features.drawer.NavHeaderViewModel
import com.ced.gweather.weather.features.weatherhome.CurrentWeatherViewModel
import com.ced.gweather.weather.features.weatherhome.WeatherHomeViewModel
import com.ced.gweather.weather.features.weatherhome.WeatherRecordsViewModel
import com.ced.gweather_core.internal.di.viewmodel.ViewModelFactory
import com.ced.gweather_core.internal.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticateViewModel::class)
    abstract fun bindAuthenticateViewModel(authenticateViewModel: AuthenticateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavHeaderViewModel::class)
    abstract fun bindNavHeaderViewViewModel(navHeaderViewModel: NavHeaderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeatherHomeViewModel::class)
    abstract fun bindWeatherHomeViewModel(weatherHomeViewModel: WeatherHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CurrentWeatherViewModel::class)
    abstract fun bindCurrentWeatherViewModel(currentWeatherViewModel: CurrentWeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeatherRecordsViewModel::class)
    abstract fun bindWeatherRecordsViewModel(weatherRecordsViewModel: WeatherRecordsViewModel): ViewModel

}