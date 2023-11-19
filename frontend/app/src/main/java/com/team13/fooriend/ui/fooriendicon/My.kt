package com.team13.fooriend.ui.fooriendicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.team13.fooriend.ui.FooriendIcon

public val FooriendIcon.My: ImageVector
    get() {
        if (_my != null) {
            return _my!!
        }
        _my = Builder(name = "My", defaultWidth = 600.0.dp, defaultHeight = 600.0.dp, viewportWidth
                = 600.0f, viewportHeight = 600.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 30.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(300.0f, 300.0f)
                moveToRelative(-265.0f, 0.0f)
                arcToRelative(265.0f, 265.0f, 0.0f, true, true, 530.0f, 0.0f)
                arcToRelative(265.0f, 265.0f, 0.0f, true, true, -530.0f, 0.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 30.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(300.0f, 230.0f)
                moveToRelative(-115.0f, 0.0f)
                arcToRelative(115.0f, 115.0f, 0.0f, true, true, 230.0f, 0.0f)
                arcToRelative(115.0f, 115.0f, 0.0f, true, true, -230.0f, 0.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF000000)),
                    strokeLineWidth = 30.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(106.819f, 481.4f)
                arcToRelative(205.0f, 205.0f, 1.0f, false, true, 386.363f, 0.0f)
            }
        }
        .build()
        return _my!!
    }

private var _my: ImageVector? = null
