package com.ced.gweather_core.internal.di.module

import com.ced.commons.clean.executor.JobExecutor
import com.ced.commons.clean.executor.PostExecutionThread
import com.ced.commons.clean.executor.ThreadExecutor
import com.ced.commons.ui.UIThread
import dagger.Binds
import dagger.Module

/**
 * @since 1.0.0
 */
@Module
abstract class ExecutorModule {

    @Binds
    abstract fun bindThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor

    @Binds
    abstract fun bindPostExecutionThread(uiThread: UIThread): PostExecutionThread
}