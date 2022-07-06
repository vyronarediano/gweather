package com.ced.gweather_core.internal.di.component

import com.ced.commons.clean.executor.PostExecutionThread
import com.ced.commons.clean.executor.ThreadExecutor
import com.ced.gweather_core.internal.di.module.ExecutorModule
import dagger.Component
import javax.inject.Singleton

/**
 * @since 1.0.0
 */
@Singleton
@Component(modules = [ExecutorModule::class])
interface ExecutorComponent {

    fun threadExecutor(): ThreadExecutor

    fun postExecutionThread(): PostExecutionThread
}