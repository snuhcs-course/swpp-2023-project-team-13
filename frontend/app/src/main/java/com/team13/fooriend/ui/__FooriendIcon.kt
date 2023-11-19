package com.team13.fooriend.ui

import androidx.compose.ui.graphics.vector.ImageVector
import com.team13.fooriend.ui.fooriendicon.Dislike
import com.team13.fooriend.ui.fooriendicon.Fooriendicon
import com.team13.fooriend.ui.fooriendicon.Home
import com.team13.fooriend.ui.fooriendicon.Like
import com.team13.fooriend.ui.fooriendicon.My
import com.team13.fooriend.ui.fooriendicon.Social
import com.team13.fooriend.ui.fooriendicon.Verified
import kotlin.collections.List as ____KtList

public object FooriendIcon

private var __AllIcons: ____KtList<ImageVector>? = null

public val FooriendIcon.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(Like, Home, Verified, Dislike, Fooriendicon, Social, My)
    return __AllIcons!!
  }
