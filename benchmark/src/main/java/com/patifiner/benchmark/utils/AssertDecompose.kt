package com.patifiner.benchmark.utils

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.value.Value
import kotlin.test.assertIs


internal inline fun <reified T : Any> Value<ChildStack<*, *>>.assertActiveInstance() {
    value.assertActiveInstance<T>()
}

internal inline fun <reified T : Any> ChildStack<*, *>.assertActiveInstance() {
    assertIs<T>(active.instance)
}

internal inline fun <reified T : Any> Value<ChildStack<*, *>>.activeInstance(): T =
    active.instance as T
