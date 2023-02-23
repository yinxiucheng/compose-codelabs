package com.yxc.customercomposeview.calander.painter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.yxc.customercomposeview.calander.utils.CalendarAttrs
import com.yxc.customercomposeview.utils.TextUtil
import org.joda.time.LocalDate

class InnerMonthPainter(attrs: CalendarAttrs) : BaseCalendarPainter() {
    private val mAttrs: CalendarAttrs
    protected var mTextPaint: Paint
    protected var mCirclePaint: Paint
    private val noAlphaColor = 255

    init {
        mAttrs = attrs
        mTextPaint = paint
        mCirclePaint = paint
    }

    private val paint: Paint
        private get() {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.textAlign = Paint.Align.CENTER
            return paint
        }

    override fun onDrawToday(canvas: Canvas, rect: RectF, localDate: LocalDate, isSelect: Boolean) {
        drawTodaySolar(canvas, rect, localDate, isSelect)
    }

    override fun onDrawWeekBg(
        canvas: Canvas,
        rectF: RectF,
        roundX: Int,
        roundY: Int,
        isTodayWeek: Boolean
    ) {
        drawWeekBgRectF(canvas, rectF, roundX, roundY, isTodayWeek)
    }

    override fun onDrawDisableDate(canvas: Canvas, rect: RectF, localDate: LocalDate) {
        drawOtherSolar(canvas, rect, mAttrs.disabledAlphaColor, localDate)
    }

    override fun onDrawDot(canvas: Canvas, rect: RectF) {
        val txtHeight = (mTextPaint.descent() - mTextPaint.ascent()) / 2
        canvas.drawCircle(
            rect.centerX(),
            (rect.centerY() + txtHeight + CalendarPainter.DOT_OFFSET).toInt().toFloat(),
            CalendarPainter.DOT_RADIUS.toFloat(),
            mTextPaint
        )
    }

    override fun onDrawCurrentMonthOrWeek(
        canvas: Canvas,
        rect: RectF,
        localDate: LocalDate,
        isSelect: Boolean
    ) {
        drawOtherSolar(canvas, rect, noAlphaColor, localDate)
    }

    override fun onDrawNotCurrentMonth(canvas: Canvas, rect: RectF, localDate: LocalDate) {
        drawOtherSolar(canvas, rect, mAttrs.alphaColor, localDate)
    }

    //今天的公历
    private fun drawTodaySolar(canvas: Canvas, rect: RectF, date: LocalDate, isSelected: Boolean) {
        if (isSelected) {
            mTextPaint.color = mAttrs.todaySolarSelectTextColor
        } else {
            mTextPaint.color = mAttrs.solarTextColor
        }
        if (mAttrs.calendarTxtFontRes !== -1) {
            TextUtil.setTypeface(mTextPaint, mAttrs.calendarTxtFontRes)
        }
        mTextPaint.textSize = mAttrs.solarTextSize
        canvas.drawText(
            date.dayOfMonth.toString() + "",
            rect.centerX(),
            (if (mAttrs.isShowLunar) rect.centerY() else getBaseLineY(rect)).toFloat(),
            mTextPaint
        )
    }

    //绘制公历1
    private fun drawOtherSolar(canvas: Canvas, rect: RectF, alphaColor: Int, date: LocalDate) {
        mTextPaint.color = mAttrs.solarTextColor
        mTextPaint.alpha = alphaColor
        mTextPaint.textSize = mAttrs.solarTextSize
        if (mAttrs.calendarTxtFontRes !== -1) {
            TextUtil.setTypeface(mTextPaint, mAttrs.calendarTxtFontRes)
        }
        canvas.drawText(
            date.dayOfMonth.toString() + "",
            rect.centerX(),
            (if (mAttrs.isShowLunar) rect.centerY() else getBaseLineY(rect)).toFloat(),
            mTextPaint
        )
    }

    fun drawWeekBgRectF(
        canvas: Canvas,
        rectF: RectF,
        roundX: Int,
        roundY: Int,
        isTodayWeek: Boolean
    ) {
//        Paint.Style style = mCirclePaint.getStyle();
        if (isTodayWeek) {
            mCirclePaint.style = Paint.Style.FILL
            mCirclePaint.color = mAttrs.todayWeekBgColor
        } else {
            mCirclePaint.style = Paint.Style.STROKE
            mCirclePaint.strokeWidth = 3f
            mCirclePaint.color = mAttrs.hollowCircleColor
        }
        canvas.drawRoundRect(rectF, roundX.toFloat(), roundY.toFloat(), mCirclePaint)
        //        mCirclePaint.setStyle(style);
    }

    private fun getBaseLineY(rect: RectF): Int {
        val fontMetrics = mTextPaint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        return (rect.centerY() - top / 2 - bottom / 2).toInt()
    }
}