package com.ced.commons.util.log

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
enum class LogLevel(val code: String, val level: Int) {

    DEBUG("D", 0),
    INFO("I", 1),
    WARNING("W", 2),
    ERROR("E", 3);

    companion object {

        fun getLogLevel(level: Int): LogLevel? {
            for (logLevel in values()) {
                if (logLevel.level == level) {
                    return logLevel
                }
            }

            return null
        }

        fun getMaxLevel(): LogLevel = values().apply { sortBy(
            LogLevel::level) }.last()
    }
}
