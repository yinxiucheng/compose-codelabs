package com.yxc.customercomposeview.common

import android.graphics.PointF

class HPoint {
    var x = 0f
    var y = 0f

    var left = PointF()
    var right = PointF()

    fun setYValue(y: Float) {
        this.y = y
        left.y = y
        right.y = y
    }

    fun adjustAllX(offset: Float) {
        x += offset
        left.x += offset
        right.x += offset
    }

    fun adjustAllY(offset: Float) {
        y += offset
        left.y += offset
        right.y += offset
    }

    fun adjustAllXY(x: Float, y: Float) {
        adjustAllX(x)
        adjustAllY(y)
    }
}