package com.yxc.customercomposeview.location

import android.graphics.PointF

class VPoint {
    var x = 0f
    var y = 0f
    var top = PointF()
    var bottom = PointF()

    fun setXValue(x: Float) {
        this.x = x
        top.x = x
        bottom.x = x
    }

    fun adjustY(offset: Float) {
        top.y -= offset
        bottom.y += offset
    }

    fun adjustAllX(offset: Float) {
        x += offset
        top.x += offset
        bottom.x += offset
    }

    fun adjustAllY(offset: Float) {
        y += offset
        top.y += offset
        bottom.y += offset
    }

    fun adjustAllXY(x: Float, y: Float) {
        adjustAllX(x)
        adjustAllY(y)
    }
}