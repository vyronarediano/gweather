package com.ced.commons.util.log

/**
 * Don't use the logger for things that should be done in the Analytics.
 * In production logLevel can be modified depending on the need.
 *
 *  @author Cedierick Vyron Arediano
 *  @since 1.0.0
 */
abstract class LoggerContract(private val currentLogLevel: LogLevel) {

    /**
     * Use this to debug expected errors that occurs often (like network error or validation errors).
     *
     * Should not be used in production since debug logs may contain sensitive data.
     */
    fun d(tag: String, msg: String, tr: Throwable? = null) {
        if (shouldLog(LogLevel.DEBUG)) {
            log(LogLevel.DEBUG, tag, msg, tr)
        }
    }

    /**
     * Use this to log errors that is expected to happen (i.e. network error or validation errors) and occurs often
     * (oppose to warning which rarely happens)
     *
     * Logs should not contain sensitive information, thus, this `LogLevel` is safe in production.
     * Use debug if you need to log user information such us email or password.
     */
    fun i(tag: String, msg: String, tr: Throwable? = null) {
        if (shouldLog(LogLevel.INFO)) {
            log(LogLevel.INFO, tag, msg, tr)
        }
    }

    /**
     * Use this to log errors that is expected to happen but should not happen often (oppose to info which can happen
     * often).
     * This is preferred than the other error logger since it includes a stacktrace.
     */
    fun w(tag: String, msg: String, tr: Throwable? = null) {
        if (shouldLog(LogLevel.WARNING)) {
            log(LogLevel.WARNING, tag, msg, tr)
        }
    }

    /**
     * Use this to log errors that is not expected to happen (SQLException, IOException etc)
     * This is preferred than the other error logger since it includes a stacktrace.
     */
    fun e(tag: String, msg: String, tr: Throwable? = null) {
        if (shouldLog(LogLevel.ERROR)) {
            log(LogLevel.ERROR, tag, msg, tr)
        }
    }

    abstract fun log(logLevel: LogLevel,
                     tag: String,
                     msg: String?,
                     tr: Throwable? = null)

    private fun shouldLog(logLevel: LogLevel) : Boolean {
        return logLevel.level >= currentLogLevel.level
    }
}
