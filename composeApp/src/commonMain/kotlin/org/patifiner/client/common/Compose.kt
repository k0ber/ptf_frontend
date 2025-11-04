package org.patifiner.client.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier


fun Modifier.scrollableScreen(scrollState: ScrollState) = Modifier
    .fillMaxSize()
    .verticalScroll(scrollState)
    .imePadding()
    .statusBarsPadding()
    .navigationBarsPadding()
