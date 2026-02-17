package org.patifiner.client.base

fun String.takeIfOrEmpty(predicate: Boolean): String = if (predicate) this else " "
