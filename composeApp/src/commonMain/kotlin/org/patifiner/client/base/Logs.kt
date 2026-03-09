package org.patifiner.client.base

import io.github.aakira.napier.Napier

const val LOG_TAG_DEFAULT = "PTF"

object PtfLog {
    fun e(e: Throwable, tag: String = LOG_TAG_DEFAULT, message: () -> String) =
        Napier.e(tag = tag, message = message, throwable = e)

    fun w(tag: String = LOG_TAG_DEFAULT, message: () -> String) = Napier.d(tag = tag, message = message)
    fun d(tag: String = LOG_TAG_DEFAULT, message: () -> String) = Napier.d(tag = tag, message = message)
    fun i(tag: String = LOG_TAG_DEFAULT, message: () -> String) = Napier.i(tag = tag, message = message)
}
