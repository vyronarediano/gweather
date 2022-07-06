package com.ced.gweather

import com.ced.commons.util.log.DefaultLogger
import com.ced.commons.util.log.LogLevel
import com.ced.commons.util.log.LoggerBuilder
import com.ced.gweather.weather.di.component.ApplicationComponent
import com.ced.gweather.weather.di.component.DaggerApplicationComponent
import com.ced.gweather_core.data.DBUtil
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import net.danlew.android.joda.JodaTimeAndroid

/**n
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class AndroidApplication : DaggerApplication() {

    var appComponent: ApplicationComponent? = null

    override fun applicationInjector(): AndroidInjector<out AndroidApplication> {
        val component = DaggerApplicationComponent.factory().create(this)
        appComponent = component

        val context = this.applicationContext
        appComponent?.inject(context)

        return component
    }

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)

        if (BuildConfig.DEBUG) LoggerBuilder.addLogger(
            DefaultLogger(
                LogLevel.DEBUG
            )
        )

        DBUtil.init(this)
    }
}