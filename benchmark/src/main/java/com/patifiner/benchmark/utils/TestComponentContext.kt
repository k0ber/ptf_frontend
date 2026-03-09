package com.patifiner.benchmark.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ComponentContextFactory
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher

class TestComponentContext(
    override val lifecycle: LifecycleRegistry = LifecycleRegistry(Lifecycle.State.RESUMED),
    override val stateKeeper: StateKeeperDispatcher = StateKeeperDispatcher(),
    override val instanceKeeper: InstanceKeeperDispatcher = InstanceKeeperDispatcher(),
    override val backHandler: BackDispatcher = BackDispatcher(),
) : ComponentContext {

    override val componentContextFactory: ComponentContextFactory<ComponentContext> =
        ComponentContextFactory(::DefaultComponentContext)
}

// todo: unused
fun TestComponentContext.recreate(isConfigurationChange: Boolean = false): TestComponentContext {
    val savedState = stateKeeper.save()
    if (!isConfigurationChange) {
        instanceKeeper.destroy()
    }

    lifecycle.destroy()

    return TestComponentContext(
        lifecycle = LifecycleRegistry(Lifecycle.State.INITIALIZED), // Сбрасываем в начальное
        stateKeeper = StateKeeperDispatcher(savedState),
        instanceKeeper = if (isConfigurationChange) instanceKeeper else InstanceKeeperDispatcher()
    )
}
