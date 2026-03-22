package org.patifiner.client.root.main.intro.events

import androidx.lifecycle.ViewModel
import org.patifiner.client.core.PtfViewModel
import org.patifiner.client.root.main.intro.IntroViewModel

class EventsIntroViewModel(
    store: EventsIntroStore,
    private val introViewModel: IntroViewModel
) : PtfViewModel<EventsIntroIntent, EventsIntroState, EventsIntroLabel>(store) {
    fun onNext() {
        introViewModel.nextStep()
    }
}
