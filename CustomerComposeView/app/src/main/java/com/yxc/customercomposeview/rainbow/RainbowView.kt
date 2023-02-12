package com.yxc.customercomposeview.rainbow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.yxc.customercomposeview.rainbow.RainbowModel.Companion.createTargetModel
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View

class RainbowView : View {
    private lateinit var circlePaint: Paint
    private lateinit var foundRectPaint: Paint

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    var viewWidth = 0
    var viewHeight = 0
    private var firstPercent = 0f
    private var secondPercent = 0f
    private var thirdPercent = 0f
    private var firstColor = -1
    private var secondColor = -1
    private var thirdColor = -1
    private var firstColors = IntArray(2)
    private var secondColors = IntArray(2)
    private var thirdColors = IntArray(2)
    private var firstBgColors = IntArray(2)
    private var secondBgColors = IntArray(2)
    private var thirdBgColors = IntArray(2)
    var itemWidth = 0f
    var spaceWidth = 0f
    private fun init() {
        foundRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    //固定宽高。
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth = measuredWidth
        setMeasuredDimension(measuredWidth, measuredWidth / 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = width
        viewHeight = 2 * height
        itemWidth = viewWidth / 7.2f
        spaceWidth = itemWidth / 6.5f
        drawThreeCircle(canvas)
    }

    private fun drawThreeCircle(canvas: Canvas) {
        drawThreeCircleBg(canvas)
        drawCircle(canvas, firstPercent, RainbowConstant.TARGET_FIRST_TYPE)
        drawCircle(canvas, secondPercent, RainbowConstant.TARGET_SECOND_TYPE)
        drawCircle(canvas, thirdPercent, RainbowConstant.TARGET_THIRD_TYPE)
    }

    private fun drawThreeCircleBg(canvas: Canvas) {
        drawCircleInner(canvas, RainbowConstant.TARGET_FIRST_TYPE, 180f, spaceWidth, true)
        drawCircleInner(canvas, RainbowConstant.TARGET_SECOND_TYPE, 180f, spaceWidth, true)
        drawCircleInner(canvas, RainbowConstant.TARGET_THIRD_TYPE, 180f, spaceWidth / 2.0f, true)
    }

    private fun checkFractionIsSmall(fraction: Float, type: Int): Boolean {
        return if (type == RainbowConstant.TARGET_FIRST_TYPE) {
            fraction < RainbowConstant.FIRST_FOUND_ANGLE_FRACTION
        } else if (type == RainbowConstant.TARGET_SECOND_TYPE) {
            fraction < RainbowConstant.SECOND_FOUND_ANGLE_FRACTION
        } else {
            fraction < RainbowConstant.THIRD_FOUND_ANGLE_FRACTION
        }
    }

    private fun drawCircle(canvas: Canvas, fraction: Float, type: Int) {
        val sweepAngel = 180 * fraction
        if (sweepAngel == 0f) {
            return
        }
        if (checkFractionIsSmall(fraction, type)) {
            drawFoundRect(canvas, type)
        } else {
            drawCircleInner(canvas, type, sweepAngel, spaceWidth)
        }
    }

    private fun drawFoundRect(canvas: Canvas, type: Int) {
        canvas.save()
        val foundRectF = createFoundRectF(type)
        foundRectPaint.color = getCircleColor(type)
        canvas.drawRoundRect(foundRectF, spaceWidth / 2.0f, spaceWidth / 2.0f, foundRectPaint)
        canvas.restore()
    }

    private fun drawCircleInner(
        canvas: Canvas,
        type: Int,
        sweepAngel: Float,
        spaceWidth: Float,
        isBg: Boolean = false
    ) {
        canvas.save()
        val rectF = createTargetRectF(type)
        canvas.translate(rectF.left, rectF.top)
        val targetModel = createTargetModel(isBg, type, rectF, itemWidth, spaceWidth, sweepAngel)
        val colors = getCircleColors(type)
        val bgColors = getCircleColorsBg(type)
        val sweepGradient: SweepGradient
        val position = FloatArray(2)
        position[0] = 0.0f
        position[1] = 1.0f
        if (isBg) {
            sweepGradient = SweepGradient((viewWidth shr 1).toFloat(), (viewWidth shr 1).toFloat(), bgColors, position)
        } else {
            sweepGradient = SweepGradient((viewWidth shr 1).toFloat(), (viewWidth shr 1).toFloat(), colors, position)
        }
        circlePaint.shader = sweepGradient
        targetModel.drawComponents(canvas, circlePaint)
        canvas.restore()
    }

    private fun getCircleColors(type: Int): IntArray {
        if (type == RainbowConstant.TARGET_FIRST_TYPE) {
            return firstColors
        } else if (type == RainbowConstant.TARGET_SECOND_TYPE) {
            return secondColors
        }
        return thirdColors
    }

    private fun getCircleColorsBg(type: Int): IntArray {
        if (type == RainbowConstant.TARGET_FIRST_TYPE) {
            return firstBgColors
        } else if (type == RainbowConstant.TARGET_SECOND_TYPE) {
            return secondBgColors
        }
        return thirdBgColors
    }

    private fun getCircleColor(type: Int): Int {
        if (type == RainbowConstant.TARGET_FIRST_TYPE) {
            return firstColor
        } else if (type == RainbowConstant.TARGET_SECOND_TYPE) {
            return secondColor
        }
        return thirdColor
    }

    private fun createTargetRectF(type: Int): RectF {
        val times = getTimes(type)
        return RectF(
            times * (itemWidth + spaceWidth), times * (itemWidth + spaceWidth),
            viewWidth - times * (itemWidth + spaceWidth), viewHeight - times * (itemWidth + spaceWidth)
        )
    }

    private fun createFoundRectF(type: Int): RectF {
        val times = getTimes(type)
        val start = times * (itemWidth + spaceWidth)
        return RectF(
            start,
            (viewHeight shr 1) - spaceWidth,
            start + itemWidth,
            (viewHeight shr 1).toFloat()
        )
    }

    private fun getTimes(type: Int): Int {
        return Math.max(type - 1, 0)
    }
}