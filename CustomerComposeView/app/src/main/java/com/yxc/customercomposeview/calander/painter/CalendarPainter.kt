package com.yxc.customercomposeview.calander.painter

import android.graphics.Canvas
import android.graphics.RectF
import com.yxc.customercomposeview.calander.model.ThreeCircleModel
import com.yxc.customercomposeview.utils.DisplayUtil
import org.joda.time.LocalDate

interface CalendarPainter {
    /**
     * 绘制今天的日期
     *
     * @param canvas
     * @param rect
     * @param nDate
     * @param isSelect 今天是否被选中
     */
    fun onDrawToday(canvas: Canvas, rect: RectF, nDate: LocalDate, isSelect: Boolean)

    /**
     * 绘制当前月或周的日期
     *
     * @param canvas
     * @param rect
     * @param nDate
     * @param isSelect 是否选中
     */
    fun onDrawCurrentMonthOrWeek(
        canvas: Canvas,
        rect: RectF,
        nDate: LocalDate,
        isSelect: Boolean
    )

    /**
     * 绘制上一月，下一月的日期，周日历不须实现
     *
     * @param canvas
     * @param rect
     * @param nDate
     */
    fun onDrawNotCurrentMonth(canvas: Canvas, rect: RectF, nDate: LocalDate)

    /**
     * 绘制不可用的日期，和方法setDateInterval(startFormatDate, endFormatDate)对应
     *
     * @param canvas
     * @param rect
     * @param nDate
     */
    fun onDrawDisableDate(canvas: Canvas, rect: RectF, nDate: LocalDate)
    fun onDrawDot(canvas: Canvas, rect: RectF)
    fun onDrawThreeCircle(canvas: Canvas, rectF: RectF, circleModel: ThreeCircleModel)
    fun onDrawWeekBg(canvas: Canvas, rectF: RectF, roundX: Int, roundY: Int, isTodayWeek: Boolean)

    companion object {
        val DOT_RADIUS: Int = DisplayUtil.dip2px(1.65f)
        val DOT_OFFSET: Int = DisplayUtil.dip2px(1f)
    }
}