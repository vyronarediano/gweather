package com.ced.commons.util.log

/**
 * @author Cedierick Vyron Arediano
 *  @since 1.0.0
*/
object LoggerBuilder {

    fun addLogger(logger: LoggerContract) {
        if (!Logger.loggers.contains(logger)) {
            // we only want one instance per logger
            Logger.loggers.add(logger)
        }
    }

    fun remove(logger : LoggerContract) : Boolean {
        return Logger.loggers.remove(logger)
    }

    /**
     * Remove logger by type. Can be used if there is no reference to the logger
     */
    inline fun <reified T : LoggerContract> remove() : Boolean {
        return Logger.loggers.removeAll { loggerContract -> loggerContract is T }
    }

}