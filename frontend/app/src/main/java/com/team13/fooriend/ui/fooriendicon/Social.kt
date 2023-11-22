package com.team13.fooriend.ui.fooriendicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.team13.fooriend.ui.FooriendIcon

public val FooriendIcon.Social: ImageVector
    get() {
        if (_social != null) {
            return _social!!
        }
        _social = Builder(name = "Social", defaultWidth = 46.553307.dp, defaultHeight =
                46.200966.dp, viewportWidth = 46.553307f, viewportHeight = 46.200966f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(40.441f, 45.966f)
                lineToRelative(-10.935f, -10.935f)
                curveToRelative(-2.997f, 1.961f, -6.579f, 3.111f, -10.444f, 3.111f)
                curveToRelative(-10.539f, 0.0f, -19.062f, -8.542f, -19.062f, -19.081f)
                curveToRelative(0.0f, -10.519f, 8.522f, -19.061f, 19.062f, -19.061f)
                curveToRelative(10.521f, 0.0f, 19.06f, 8.542f, 19.06f, 19.061f)
                curveToRelative(0.0f, 3.679f, -1.036f, 7.107f, -2.828f, 10.011f)
                lineToRelative(11.013f, 11.011f)
                curveToRelative(0.583f, 0.567f, 0.094f, 1.981f, -1.076f, 3.148f)
                lineToRelative(-1.64f, 1.644f)
                curveToRelative(-1.17f, 1.167f, -2.584f, 1.656f, -3.15f, 1.091f)
                close()
                moveTo(31.788f, 19.061f)
                curveToRelative(0.0f, -7.033f, -5.695f, -12.727f, -12.727f, -12.727f)
                curveToRelative(-7.033f, 0.0f, -12.745f, 5.694f, -12.745f, 12.727f)
                reflectiveCurveToRelative(5.712f, 12.745f, 12.745f, 12.745f)
                curveToRelative(7.032f, 0.0f, 12.727f, -5.711f, 12.727f, -12.745f)
                close()
            }
        }
        .build()
        return _social!!
    }

private var _social: ImageVector? = null
