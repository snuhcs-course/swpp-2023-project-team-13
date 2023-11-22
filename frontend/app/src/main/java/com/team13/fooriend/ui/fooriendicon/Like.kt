package com.team13.fooriend.ui.fooriendicon

import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.team13.fooriend.ui.FooriendIcon

public val FooriendIcon.Like: ImageVector
    get() {
        if (_like != null) {
            return _like!!
        }
        _like = Builder(name = "Like", defaultWidth = 91.0.dp, defaultHeight = 91.0.dp,
                viewportWidth = 91.0f, viewportHeight = 91.0f).apply {
            path(fill = null, stroke = null, strokeLineWidth = 0.0f, strokeLineCap = Butt,
                    strokeLineJoin = Miter, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(0.0f, 0.0f)
                horizontalLineToRelative(91.0f)
                verticalLineToRelative(91.0f)
                horizontalLineToRelative(-91.0f)
                close()
            }
        }
        .build()
        return _like!!
    }

private var _like: ImageVector? = null
