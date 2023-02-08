package com.yxc.customercomposeview.waterdrop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.yxc.customercomposeview.common.VPoint
import com.yxc.customercomposeview.common.HPoint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.yxc.customercomposeview.common.getResourcesColor

class BezierCircle(
    /**
     * 半径
     */
    private var mContext: Context, attrs: AttributeSet?, defStyleAttr: Int
) : View(
    mContext, attrs, defStyleAttr
) {
    /**
     * 路径
     */
    private lateinit var mPath: Path

    /**
     * 画笔
     */
    private lateinit var mFillCirclePaint: Paint

    /**
     * 四个点
     */
    private lateinit var p2: VPoint
    private lateinit var p4: VPoint
    private lateinit var p1: HPoint
    private lateinit var p3: HPoint
    private var c = 0f
    private val blackMagic = 0.551915024494f
    private var number = 0
    private var colorResource = 0
    private var radius = 0

    constructor(context: Context, radius: Int, number: Int, colorResource: Int) : this(
        context,
        null
    ) {
        mContext = context
        this.radius = radius
        this.number = number
        this.colorResource = colorResource
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        mContext = context
        init()
    }

    init {
        init()
    }

    /**
     * 初始化操作
     */
    private fun init() {
        mFillCirclePaint = Paint()
        mFillCirclePaint.color = -0x1
        mFillCirclePaint.style = Paint.Style.FILL
        mFillCirclePaint.strokeWidth = 1f
        mFillCirclePaint.isAntiAlias = true
        mPath = Path()
        p2 = VPoint()
        p4 = VPoint()
        p1 = HPoint()
        p3 = HPoint()
        c = radius * blackMagic
    }

    override fun onDraw(canvas: Canvas) {
        mPath.reset()
        canvas.translate((width / 2).toFloat(), height * 0.6f)
        drawWaterDrop(canvas, number, radius, colorResource)
    }

    private fun drawWaterDrop(canvas: Canvas, number: Int, radius: Int, colorResource: Int) {
        canvas.save()
        val path = getPath(number * radius)
        if (number > 1) {
            val radiusCircle = (number - 1) * radius
            val clipPath = getPath(radiusCircle)
            path.op(clipPath, Path.Op.DIFFERENCE)
        }
        //去锯齿
        canvas.drawFilter = PaintFlagsDrawFilter(
            0,
            Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        )
        drawBezierPath(canvas, getResourcesColor(mContext, colorResource), path)
        canvas.restore()
    }

    /**
     * 画圆
     */
    private fun getPath(radius: Int): Path {
        circleModel(radius)
        val path = Path()
        path.moveTo(p1.x, p1.y)
        p3.y = p3.y - radius * 0.2f * 1.5f
        p3.y -= radius * 0.2f * 1.8f
        path.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y)
        path.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y)
        path.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y)
        path.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y)
        path.close()
        return path
    }

    private fun drawBezierPath(canvas: Canvas, color: Int, path: Path) {
        val colorOrigin = mFillCirclePaint.color
        mFillCirclePaint.color = color
        canvas.drawPath(path, mFillCirclePaint)
        mFillCirclePaint.color = colorOrigin
    }

    private fun circleModel(radius: Int) {
        c = radius * blackMagic
        // p2.p4属于圆左右两点
        p1.y = radius.toFloat() //右边
        p3.y = (-radius).toFloat() // 左边
        p1.x = 0f
        p3.x = p1.x //圆心
        p3.left.x = -c * 0.36f
        p3.right.x = c * 0.36f
        p1.left.x = -c
        p1.right.x = c
        //p1.p3属于圆的上下两点
        p2.x = radius.toFloat() // 下边
        p4.x = (-radius).toFloat() // 上边
        p4.y = 0f
        p2.y = p4.y //圆心
        p4.top.y = -c
        p2.top.y = p4.top.y
        p4.bottom.y = c
        p2.bottom.y = p4.bottom.y
    }
}