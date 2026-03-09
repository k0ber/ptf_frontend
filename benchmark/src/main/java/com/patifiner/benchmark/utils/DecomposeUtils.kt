package com.patifiner.benchmark.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import kotlin.test.assertIs

internal fun <T : Any> createComponent(factory: (ComponentContext) -> T): T {
    val lifecycle = LifecycleRegistry()
    val component = factory(DefaultComponentContext(lifecycle = lifecycle))
    lifecycle.resume()

    return component
}
