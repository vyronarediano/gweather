package com.ced.gweather.weather.ui

import androidx.lifecycle.ViewModelProvider
import com.ced.gweather.AndroidApplication
import com.ced.gweather.commons.ui.BaseFragment
import com.ced.gweather.weather.di.component.ApplicationComponent
import javax.inject.Inject

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
abstract class BaseFragmentDI : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as AndroidApplication).appComponent!!
    }
}