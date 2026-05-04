package org.patifiner.client.design.icons

import androidx.compose.ui.graphics.vector.ImageVector

object PtfIcons {
    private val all: List<ImageVector> = listOf(
        IcArrowDown, IcArrowLeft, IcArrowRight, IcArrowUp,
        IcEmail, IcPassword,
        IcVisibilityOn, IcVisibilityOff,
        IcExplore, IcGroup, IcPerson
    )

    fun warmup() { // provide only icons important for app start, not all
        all.forEach { it.name }
    }
}
