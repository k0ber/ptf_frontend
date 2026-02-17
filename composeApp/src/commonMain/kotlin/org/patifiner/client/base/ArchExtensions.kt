package org.patifiner.client.base

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

val ComponentContext.componentScope: CoroutineScope
    get() = instanceKeeper.getOrCreate(::CoroutineScopeInstance).scope

private class CoroutineScopeInstance : InstanceKeeper.Instance {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    override fun onDestroy() = scope.cancel()
}

fun <T : Any> Store<*, T, *>.asValue(): Value<T> = object : Value<T>() {
    override val value: T get() = state
    override fun subscribe(observer: (T) -> Unit): Cancellation {
        val disposable = states(observer(onNext = observer))
        return Cancellation { disposable.dispose() }
    }
}
