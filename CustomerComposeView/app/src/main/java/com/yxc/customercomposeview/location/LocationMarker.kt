package com.yxc.customercomposeview.location
import  android.graphics.Path
import android.graphics.RectF

class LocationMarker {
    private var mPath: Path
     var p2: VPoint
     var p4: VPoint
      var p1: HPoint
      var p3: HPoint

    private var c = 0f
    private val blackMagic = 0.551915024494f
    private var markerParams: MarkerParams

    constructor(markerParams: MarkerParams){
        this.markerParams = markerParams
        mPath = Path()
        p2 = VPoint()
        p4 = VPoint()
        p1 = HPoint()
        p3 = HPoint()
        c = markerParams.radius.value * blackMagic
    }

    /**
     * 画圆
     */
    fun getPath(radius: Float): Path{
        circleModel(radius)
        val path = Path()
        p1.setYValue(p1.y + radius * 0.2f * 1.05f) //设置 p1 底部左右两个点的y值
        p1.y += radius * 0.2f * 1.05f //设置 p1 自己的y值
        path.moveTo(p1.x, p1.y)
        path.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y)
        path.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y)
        path.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y)
        path.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y)
        path.close()
        val circle = Path()
        circle.addCircle(p3.x, p3.y + radius, markerParams.circleRadius.value, Path.Direction.CCW)
        path.op(circle, Path.Op.DIFFERENCE)
        return path
    }

    fun getCenterCircle(radius: Float): Path{
        val circle = Path()
        circle.addCircle(p3.x, p3.y + radius, markerParams.circleRadius.value, Path.Direction.CCW)
        return circle
    }

    fun getBottomOval():Path{
        val rectF = RectF()
        val width: Float = markerParams.radius.value
        val height: Float = markerParams.radius.value / 4
        val offsetY: Float = height / 2 - 3f
        rectF[p1.x - width / 2, p1.y - offsetY , p1.x + width / 2] = p1.y + offsetY
        val path = Path()
        path.addOval(rectF, Path.Direction.CCW)
        return path
    }

    private fun circleModel(radius: Float) {
        c = radius * blackMagic
        p1.setYValue(radius) //右边
        p3.setYValue(-radius) // 左边
        p1.x = 0f
        p3.x = p1.x //圆心
        p3.left.x = -c
        p3.right.x = c
        p1.left.x = -c * 0.36f
        p1.right.x = c * 0.36f
        //p1.p3属于圆的上下两点
        p2.setXValue(radius) // 下边
        p4.setXValue(-radius) // 上边
        p4.y = 0f
        p2.y = p4.y //  圆心
        p4.top.y = -c
        p2.top.y = p4.top.y
        p4.bottom.y = c
        p2.bottom.y = p4.bottom.y
    }

}