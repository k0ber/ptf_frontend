package org.patifiner.client.design.icons

import androidx.compose.ui.graphics.vector.ImageVector
import org.patifiner.client.design.icons.ptficons.IcVisibilityOn
import org.patifiner.client.design.icons.ptficons.AllIcons
import org.patifiner.client.design.icons.ptficons.IcEmail
import org.patifiner.client.design.icons.ptficons.IcPassword
import org.patifiner.client.design.icons.ptficons.IcVisibilityOff
import org.patifiner.client.design.icons.ptficons.Ptficons
import kotlin.collections.List as ____KtList

public object PtfIcons

private var __AllIcons: ____KtList<ImageVector>? = null

public val PtfIcons.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= Ptficons.AllIcons + listOf(IcEmail, IcPassword, IcVisibilityOff, IcVisibilityOn)
    return __AllIcons!!
  }
