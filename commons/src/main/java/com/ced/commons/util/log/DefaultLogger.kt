package com.ced.commons.util.log

import com.ced.commons.util.log.LogLevel.*

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class DefaultLogger(private val currentLogLevel: LogLevel) : LoggerContract(currentLogLevel) {

    override fun log(logLevel: LogLevel, tag: String, msg: String?, tr: Throwable?) {
        when (logLevel) {
            DEBUG -> android.util.Log.d(tag, msg, tr)
            INFO -> android.util.Log.i(tag, msg, tr)
            WARNING -> android.util.Log.w(tag, msg, tr)
            ERROR -> android.util.Log.e(tag, msg, tr)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is DefaultLogger
    }

    override fun hashCode(): Int {
        return DefaultLogger::class.java.simpleName.hashCode()
    }

}
