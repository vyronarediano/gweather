package com.ced.commons.util.log

import java.util.*

/**
 * Facade for logger
 *
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
object Logger {

    @PublishedApi
    internal val loggers = ArrayList<LoggerContract>()

    fun d(tag: String, msg: String, tr: Throwable? = null) {
        for (logger in loggers) {
            logger.d(tag, msg, tr)
        }
    }

    fun i(tag: String, msg: String, tr: Throwable? = null) {
        for (logger in loggers) {
            logger.i(tag, msg, tr)
        }
    }

    fun w(tag: String, msg: String, tr: Throwable? = null) {
        for (logger in loggers) {
            logger.w(tag, msg, tr)
        }
    }

    fun e(tag: String, msg: String, tr: Throwable? = null) {
        for (logger in loggers) {
            logger.e(tag, msg, tr)
        }
    }

    inline fun <reified T : LoggerContract> get(): T? {
        return loggers.findLast { loggerContract -> loggerContract is T } as? T
    }
}
