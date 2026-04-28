package org.patifiner.client.root.main.intro.events

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.patifiner.client.root.main.intro.IntroViewModel

@Stable
class EventsIntroViewModel(
    private val introViewModel: IntroViewModel
) : ViewModel(), ContainerHost<EventsIntroState, EventsIntroSideEffect> {

    override val container: Container<EventsIntroState, EventsIntroSideEffect> =
        container(EventsIntroState())

    fun changeSearch(q: String) = intent { reduce { state.copy(q = q) } }

    fun onNext() {
        introViewModel.nextStep()
    }
}
