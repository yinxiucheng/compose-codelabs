package com.yxc.customercomposeview.rainbow

import android.graphics.Path
import android.graphics.RectF
import androidx.compose.ui.graphics.AndroidPath
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

class RainbowModel private constructor(
    isBG: Boolean, rectF: RectF, itemWidth: Float, spaceWidth: Float,type: Int,
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
    var colorResource: Int = -1
    var type:Int = RainbowConstant.TARGET_FIRST_TYPE

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
        reSize = if (isBG) 0f else RainbowConstant.RESIZE
        this.rectF = rectF
        width = rectF.width()
        height = rectF.height()
        this.itemWidth = itemWidth
        this.spaceWidth = spaceWidth
        this.type = type
        this.sweepAngel = sweepAngel
        this.colorResource = getColorResource(type)
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
    }

    fun createComponents() {
        createWrapperCircle()
        createCenterCircle()
        createInnerCircle()
        createWrapperPath()
        createInnerPath()
    }

    fun drawComponents(drawScope: DrawScope, color:Color, isBG: Boolean) {
        val alpha = if (isBG) 0.4f else 1.0f
        drawScope.apply {
            drawPath(AndroidPath(wrapperCircle), color, alpha)
            drawPath(AndroidPath(centerCircle), color, alpha)
            drawPath(AndroidPath(innerCircle), color, alpha)
            drawPath(AndroidPath(wrapperStartPath), color, alpha)
            drawPath(AndroidPath(wrapperEndPath), color, alpha)
            drawPath(AndroidPath(innerStartPath), color, alpha)
            drawPath(AndroidPath(innerEndPath), color, alpha)
        }
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
            innerStartRectF, innerEndRectF, innerStartAngel - 0.3f,
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
            return RainbowModel(
                isBg,
                rectF,
                itemWidth,
                spaceWidth,
                type,
                sweepAngel
            )
        }
    }
}