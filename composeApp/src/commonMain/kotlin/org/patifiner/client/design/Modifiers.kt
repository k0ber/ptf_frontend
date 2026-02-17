package org.patifiner.client.design

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


fun Modifier.scrollableScreen(scrollState: ScrollState) = this.fillMaxSize()
    .statusBarsPadding()
    .navigationBarsPadding()
    .imePadding()
    .verticalScroll(scrollState)

fun Modifier.screen() = this.fillMaxSize()
    .imePadding()
    .statusBarsPadding()
    .navigationBarsPadding()

fun ColumnScope.centeredField() = Modifier.align(Alignment.CenterHorizontally)
    .requiredWidthIn(max = 400.dp)
    .wrapContentWidth(Alignment.CenterHorizontally)
