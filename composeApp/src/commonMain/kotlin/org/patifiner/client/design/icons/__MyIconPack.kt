package org.patifiner.client.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import org.patifiner.client.design.icons.myiconpack.IcArrowDown
import org.patifiner.client.design.icons.myiconpack.IcArrowLeft
import org.patifiner.client.design.icons.myiconpack.IcArrowRight
import org.patifiner.client.design.icons.myiconpack.IcArrowUp
import org.patifiner.client.design.icons.myiconpack.IcEmail
import org.patifiner.client.design.icons.myiconpack.IcPassword
import org.patifiner.client.design.icons.myiconpack.IcVisibilityOff
import org.patifiner.client.design.icons.myiconpack.IcVisibilityOn
import kotlin.collections.List as ____KtList

public object MyIconPack

private var __AllIcons: ____KtList<ImageVector>? = null

public val MyIconPack.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(IcArrowDown, IcArrowLeft, IcArrowRight, IcArrowUp, IcEmail, IcPassword,
        IcVisibilityOff, IcVisibilityOn)
    return __AllIcons!!
  }
