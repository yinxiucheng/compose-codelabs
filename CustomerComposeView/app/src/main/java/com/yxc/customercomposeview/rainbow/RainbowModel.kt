package com.yxc.customercomposeview.rainbow

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF

class RainbowModel private constructor(
    isBG: Boolean, rectF: RectF, itemWidth: Float, spaceWidth: Float,
    wrapperFixAngel: Float, innerFixAngel: Float,
    sweepAngel: Float
) {
    var reSize: Float
    var rectF: RectF
    var itemWidth: Float
    var spaceWidth: Float
    var width: Float
    var height: Float
    var centerStartAngel = 180f
    var sweepAngel: Float
    var wrapperFixAngel: Float
    var innerFixAngel: Float
    private lateinit var centerCircle: Path
    private lateinit var wrapperCircle: Path
    private lateinit var innerCircle: Path
    private lateinit var wrapperStartPath: Path
    private lateinit var wrapperEndPath: Path
    private lateinit var innerStartPath: Path
    private lateinit var innerEndPath: Path
    private lateinit var wrapperStartRectF: RectF
    private lateinit var wrapperEndRectF: RectF
    private lateinit var innerStartRectF: RectF
    private lateinit var innerEndRectF: RectF

    init {
        reSize = if (isBG) RainbowConstant.RESIZE_BG.toFloat() else RainbowConstant.RESIZE.toFloat()
        this.rectF = rectF
        width = rectF.width()
        height = rectF.height()
        this.itemWidth = itemWidth
        this.spaceWidth = spaceWidth
        this.wrapperFixAngel = wrapperFixAngel
        this.innerFixAngel = innerFixAngel
        this.sweepAngel = sweepAngel
    }

    private fun createComponents() {
        createWrapperCircle()
        createCenterCircle()
        createInnerCircle()
        createWrapperPath()
        createInnerPath()
    }

    fun drawComponents(canvas: Canvas, paint: Paint) {
        createComponents()
        canvas.drawPath(wrapperCircle, paint)
        canvas.drawPath(centerCircle, paint)
        canvas.drawPath(innerCircle, paint)
        canvas.drawPath(wrapperStartPath, paint)
        canvas.drawPath(wrapperEndPath, paint)
        canvas.drawPath(innerStartPath, paint)
        canvas.drawPath(innerEndPath, paint)
    }

    private fun createWrapperCircle() {
        wrapperStartRectF = RectF(0f, 0f, width, height)
        wrapperEndRectF = RectF(
            spaceWidth + reSize, spaceWidth + reSize,
            width - spaceWidth - reSize, height - spaceWidth - reSize
        )
        val wrapperStartAngel = 180 + wrapperFixAngel
        wrapperCircle = createCircle(
            wrapperStartRectF, wrapperEndRectF, wrapperStartAngel - 0.2f,
            sweepAngel - 2 * wrapperFixAngel + 0.4f, width / 2, height / 2
        )
    }

    private fun createCenterCircle() {
        val rectFWrapper =
            RectF(spaceWidth - 1, spaceWidth - 1, width - spaceWidth + 1, height - spaceWidth + 1)
        val rectFInner = RectF(
            itemWidth - spaceWidth + 1,
            itemWidth - spaceWidth + 1,
            width - itemWidth + spaceWidth - 1,
            height - itemWidth + spaceWidth - 1
        )
        centerCircle = createCircle(
            rectFWrapper,
            rectFInner,
            centerStartAngel,
            sweepAngel,
            width / 2,
            height / 2
        )
    }

    private fun createInnerCircle() {
        innerStartRectF = RectF(
            itemWidth - spaceWidth - reSize, itemWidth - spaceWidth - reSize,
            width - itemWidth + spaceWidth + reSize, height - itemWidth + spaceWidth + reSize
        )
        innerEndRectF = RectF(itemWidth, itemWidth, width - itemWidth, height - itemWidth)
        val innerStartAngel = 180 + innerFixAngel
        innerCircle = createCircle(
            innerStartRectF!!, innerEndRectF!!, innerStartAngel - 0.3f,
            sweepAngel - 2 * innerFixAngel + 0.6f, width / 2, height / 2
        )
    }

    private fun createCircle(
        rectFWrapper: RectF,
        rectFInner: RectF,
        startAngle: Float,
        sweepAngle: Float,
        centerX: Float,
        centerY: Float
    ): Path {
        val path = Path()
        path.addArc(rectFWrapper, startAngle, sweepAngle)
        path.lineTo(centerX, centerY)
        path.close()
        val clipPath = Path()
        clipPath.addArc(rectFInner, startAngle, sweepAngle)
        clipPath.lineTo(centerX, centerY)
        clipPath.close()
        path.op(clipPath, Path.Op.DIFFERENCE)
        return path
    }

    private fun createInnerPath() {
        innerStartPath = Path()
        val startQuadModel = QuadModel()
        startQuadModel.centerPointF =
            startQuadModel.createCommonPoint(innerStartRectF, innerFixAngel)
        startQuadModel.ctrlPointF = startQuadModel.createCommonPoint(innerEndRectF, 0f)
        startQuadModel.startPointF =
            startQuadModel.createCommonPoint(innerEndRectF, innerFixAngel)
        startQuadModel.endPointF = startQuadModel.createCommonPoint(innerStartRectF, 0f)
        innerStartPath = startQuadModel.createQuadPath()
        val endQuadModel = QuadModel()
        endQuadModel.centerPointF =
            endQuadModel.createEndPoint(innerStartRectF, 180 - sweepAngel + innerFixAngel)
        endQuadModel.ctrlPointF = endQuadModel.createCommonPoint(innerEndRectF, sweepAngel)
        endQuadModel.startPointF = endQuadModel.createCommonPoint(innerStartRectF, sweepAngel)
        endQuadModel.endPointF =
            endQuadModel.createEndPoint(innerEndRectF, 180 - sweepAngel + innerFixAngel)
        innerEndPath = endQuadModel.createQuadPath()
    }

    private fun createWrapperPath() {
        val startQuadModel = QuadModel()
        startQuadModel.centerPointF =
            startQuadModel.createCommonPoint(wrapperEndRectF, wrapperFixAngel)
        startQuadModel.ctrlPointF = startQuadModel.createCommonPoint(wrapperStartRectF, 0f)
        startQuadModel.startPointF = startQuadModel.createCommonPoint(wrapperEndRectF, 0f)
        startQuadModel.endPointF =
            startQuadModel.createCommonPoint(wrapperStartRectF, wrapperFixAngel)
        wrapperStartPath = startQuadModel.createQuadPath()
        val endQuadModel = QuadModel()
        endQuadModel.centerPointF =
            endQuadModel.createEndPoint(wrapperEndRectF, 180 - sweepAngel + wrapperFixAngel)
        endQuadModel.ctrlPointF = endQuadModel.createCommonPoint(wrapperStartRectF, sweepAngel)
        endQuadModel.startPointF =
            endQuadModel.createEndPoint(wrapperStartRectF, 180 - sweepAngel + wrapperFixAngel)
        endQuadModel.endPointF = endQuadModel.createCommonPoint(wrapperEndRectF, sweepAngel)
        wrapperEndPath = endQuadModel.createQuadPath()
    }

    companion object {
        @JvmStatic
        fun createTargetModel(
            isBg: Boolean,
            type: Int,
            rectF: RectF,
            itemWidth: Float,
            spaceWidth: Float,
            sweepAngel: Float
        ): RainbowModel {
            val wrapperFixAngel: Float
            val innerFixAngel: Float
            if (type == RainbowConstant.TARGET_FIRST_TYPE) {
                wrapperFixAngel = RainbowConstant.FIRST_WRAPPER_FIX_ANGLE
                innerFixAngel = RainbowConstant.FIRST_INNER_FIX_ANGLE
            } else if (type == RainbowConstant.TARGET_SECOND_TYPE) {
                wrapperFixAngel = RainbowConstant.SECOND_WRAPPER_FIX_ANGLE
                innerFixAngel = RainbowConstant.SECOND_INNER_FIX_ANGLE
            } else {
                wrapperFixAngel = RainbowConstant.THIRD_WRAPPER_FIX_ANGLE
                innerFixAngel = RainbowConstant.THIRD_INNER_FIX_ANGLE
            }
            return RainbowModel(
                isBg,
                rectF,
                itemWidth,
                spaceWidth,
                wrapperFixAngel,
                innerFixAngel,
                sweepAngel
            )
        }
    }
}