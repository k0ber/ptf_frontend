package org.patifiner.client

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JvmNetworkObserver : NetworkObserver {
    override val isOnline: StateFlow<Boolean> = MutableStateFlow(true)
}
