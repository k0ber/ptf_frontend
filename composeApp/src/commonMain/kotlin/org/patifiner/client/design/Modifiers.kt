package org.patifiner.client.design

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.ptfScrollable(scrollState: ScrollState) = this
    .fillMaxSize()
    .verticalScroll(scrollState)
    .imePadding()

fun Modifier.centeredField() = this
    .fillMaxWidth()
    .requiredWidthIn(max = 400.dp)
    .wrapContentWidth(Alignment.CenterHorizontally)
