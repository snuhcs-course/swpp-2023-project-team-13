package com.team13.fooriend.ui.fooriendicon

import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.team13.fooriend.ui.FooriendIcon

public val FooriendIcon.Verified: ImageVector
    get() {
        if (_verified != null) {
            return _verified!!
        }
        _verified = Builder(name = "Verified", defaultWidth = 92.0.dp, defaultHeight = 92.0.dp,
                viewportWidth = 92.0f, viewportHeight = 92.0f).apply {
            path(fill = null, stroke = null, strokeLineWidth = 0.0f, strokeLineCap = Butt,
                    strokeLineJoin = Miter, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(0.0f, 0.0f)
                horizontalLineToRelative(92.0f)
                verticalLineToRelative(92.0f)
                horizontalLineToRelative(-92.0f)
                close()
            }
        }
        .build()
        return _verified!!
    }

private var _verified: ImageVector? = null
