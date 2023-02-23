package com.yxc.customercomposeview.calander.painter

import android.graphics.Canvas
import android.graphics.RectF
import com.yxc.customercomposeview.calander.model.ThreeCircleModel

abstract class BaseCalendarPainter : CalendarPainter {
    override fun onDrawThreeCircle(canvas: Canvas, rectF: RectF, circleModel: ThreeCircleModel) {}
    override fun onDrawWeekBg(
        canvas: Canvas,
        rectF: RectF,
        roundX: Int,
        roundY: Int,
        isTodayWeek: Boolean
    ) {
    }

    override fun onDrawDot(canvas: Canvas, rect: RectF) {}
}