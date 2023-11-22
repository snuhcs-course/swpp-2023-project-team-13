package com.team13.fooriend.ui.fooriendicon

import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.team13.fooriend.ui.FooriendIcon

public val FooriendIcon.Fooriendicon: ImageVector
    get() {
        if (_fooriendicon != null) {
            return _fooriendicon!!
        }
        _fooriendicon = Builder(name = "Fooriendicon", defaultWidth = 68.0.dp, defaultHeight =
                85.0.dp, viewportWidth = 68.0f, viewportHeight = 85.0f).apply {
            path(fill = null, stroke = null, strokeLineWidth = 0.0f, strokeLineCap = Butt,
                    strokeLineJoin = Miter, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(0.0f, 0.0f)
                horizontalLineToRelative(68.0f)
                verticalLineToRelative(85.0f)
                horizontalLineToRelative(-68.0f)
                close()
            }
        }
        .build()
        return _fooriendicon!!
    }

private var _fooriendicon: ImageVector? = null
