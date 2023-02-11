package com.yxc.customercomposeview.waterdrop

import android.graphics.Path
import com.yxc.customercomposeview.common.HPoint
import com.yxc.customercomposeview.common.VPoint

class ComposeBezierCircle {
    private var mPath: Path
    private var p2: VPoint
    private var p4: VPoint
    private var p1: HPoint
    private var p3: HPoint
    private var c = 0f
    private val blackMagic = 0.551915024494f
    private var number = 0
    var colorResource = 0
    private var radius = 0f

    constructor(radius: Float, number: Int, colorResource: Int){
        mPath = Path()
        p2 = VPoint()
        p4 = VPoint()
        p1 = HPoint()
        p3 = HPoint()
        this.radius = radius
        this.number = number
        this.colorResource = colorResource
        c = radius * blackMagic
    }

    fun getPath():Path {
        val path = getPathInner(number * radius)
        if (number > 1) {
            val radiusCircle = (number - 1) * radius
            val clipPath = getPathInner(radiusCircle)
            path.op(clipPath, Path.Op.DIFFERENCE)
        }
        return path
    }

    private fun getPathInner(radius: Float): Path {
        circleModel(radius)
        val path = Path()
        path.moveTo(p1.x, p1.y)
        p3.setYValue(p3.y - radius * 0.2f * 1.5f)
        p3.y -= radius * 0.2f * 1.8f
        path.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y)
        path.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y)
        path.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y)
        path.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y)
        path.close()
        return path
    }

    private fun circleModel(radius: Float) {
        c = radius * blackMagic
        // p2.p4属于圆左右两点
        p1.setYValue(radius) //右边
        p3.setYValue(-radius) // 左边
        p1.x = 0f
        p3.x = p1.x //圆心
        p3.left.x = -c * 0.36f
        p3.right.x = c * 0.36f
        p1.left.x = -c
        p1.right.x = c
        //p1.p3属于圆的上下两点
        p2.setXValue(radius) // 下边
        p4.setXValue(-radius) // 上边
        p4.y = 0f
        p2.y = p4.y //圆心
        p4.top.y = -c
        p2.top.y = p4.top.y
        p4.bottom.y = c
        p2.bottom.y = p4.bottom.y
    }
}