package com.yxc.customercomposeview

import android.graphics.Paint
import android.graphics.RectF

fun getTxtHeight(paint: Paint): Float {
    val fontMetrics = paint.fontMetrics
    return fontMetrics.bottom - fontMetrics.top
}

fun getTextBaseY(rectF: RectF, paint: Paint): Float {
    val fontMetrics = paint.fontMetrics
    return rectF.centerY() - fontMetrics.top / 2 - fontMetrics.bottom / 2
}