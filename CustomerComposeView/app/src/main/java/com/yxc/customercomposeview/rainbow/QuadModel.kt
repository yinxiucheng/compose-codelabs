package com.yxc.customercomposeview.rainbow

import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.RectF

class QuadModel {
    lateinit var startPointF: PointF
    lateinit var endPointF: PointF
    lateinit var ctrlPointF: PointF
    lateinit var centerPointF: PointF
    lateinit var quadPath: Path

    fun createQuadPath(): Path {
        quadPath = Path()
        quadPath.apply {
            moveTo(startPointF.x, startPointF.y)
            quadTo(ctrlPointF.x, ctrlPointF.y, endPointF.x, endPointF.y)
            lineTo(centerPointF.x, centerPointF.y)
            close()
        }
        return quadPath
    }

    fun createCommonPoint(rectF: RectF, sweepAngel: Float): PointF {
        val radius = rectF.width() / 2
        val halfCircleLength = (Math.PI * radius).toFloat()
        val pathOriginal = Path()
        pathOriginal.moveTo(rectF.left, (rectF.top + rectF.bottom) / 2)
        pathOriginal.arcTo(rectF, 180f, 180f, false)
        val pathMeasure = PathMeasure(pathOriginal, false)
        val points = FloatArray(2)
        val pointLength = halfCircleLength * sweepAngel / 180f
        pathMeasure.getPosTan(pointLength, points, null)
        return PointF(points[0], points[1])
    }

    fun createEndPoint(rectF: RectF, sweepAngel: Float): PointF {
        val radius = rectF.width() / 2
        val halfCircleLength = (Math.PI * radius).toFloat()
        val pathOriginal = Path()
        pathOriginal.moveTo(rectF.right, (rectF.top + rectF.bottom) / 2)
        pathOriginal.arcTo(rectF, 0f, -180f, false)
        val pathMeasure = PathMeasure(pathOriginal, false)
        val points = FloatArray(2)
        val pointLength = halfCircleLength * sweepAngel / 180f
        pathMeasure.getPosTan(pointLength, points, null)
        return PointF(points[0], points[1])
    }
}