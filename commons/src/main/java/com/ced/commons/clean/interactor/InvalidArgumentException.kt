package com.ced.commons.clean.interactor

import java.lang.Exception
import java.lang.StringBuilder

/**
 * To be thrown when there is invalid parameter passed.
 *
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class InvalidArgumentException : Exception {

    var messages: Map<String, String>? = null

    constructor(message : String): super(message)

    constructor(messages : Map<String, String>) : super() {
        this.messages = messages
    }

    override val message: String?
        get() {
            return if (!messages.isNullOrEmpty()) {
                val message = StringBuilder()
                messages!!.forEach {
                    message.append(it.value).append("\r\n")
                }

                message.toString()
            } else {
               super.message
            }
        }
}