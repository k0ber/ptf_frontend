package org.patifiner.client.core

fun String.takeIfOrEmpty(predicate: Boolean): String = if (predicate) this else " "
