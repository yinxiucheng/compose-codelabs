package com.yxc.customercomposeview.calander.painter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.yxc.customercomposeview.calander.utils.CalendarAttrs
import com.yxc.customercomposeview.utils.TextUtil
import org.joda.time.LocalDate

class InnerPainter(attrs: CalendarAttrs) : BaseCalendarPainter() {
    private val mAttrs: CalendarAttrs
    protected var mTextPaint: Paint
    protected var mCirclePaint: Paint
    private val noAlphaColor = 255

    init {
        mAttrs = attrs
        mTextPaint = paint
        mCirclePaint = paint
    }

    protected val paint: Paint
        protected get() {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.textAlign = Paint.Align.CENTER
            return paint
        }

    override fun onDrawToday(canvas: Canvas, rect: RectF, localDate: LocalDate, isSelect: Boolean) {
        mTextPaint.color = mAttrs.todayBgColor
        canvas.drawCircle(rect.centerX(), rect.centerY(), mAttrs.selectCircleRadius, mTextPaint)
        drawTodaySolar(canvas, rect, noAlphaColor, localDate, isSelect)
    }

    override fun onDrawDisableDate(canvas: Canvas, rect: RectF, localDate: LocalDate) {
        drawSolar(canvas, rect, mAttrs.disabledAlphaColor, localDate, false)
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
        if (isSelect) {
            drawSolidCircle(canvas, rect, true)
            drawSolar(canvas, rect, noAlphaColor, localDate, true)
        } else {
            drawSolar(canvas, rect, noAlphaColor, localDate, false)
        }
    }

    override fun onDrawNotCurrentMonth(canvas: Canvas, rect: RectF, localDate: LocalDate) {
        drawSolar(canvas, rect, mAttrs.alphaColor, localDate, false)
    }

    //实心圆
    private fun drawSolidCircle(canvas: Canvas, rect: RectF, isTodaySelect: Boolean) {
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = mAttrs.hollowCircleStroke
        if (isTodaySelect) {
            mCirclePaint.color = mAttrs.selectCircleColor
            canvas.drawCircle(
                rect.centerX(),
                rect.centerY(),
                mAttrs.selectCircleRadius,
                mCirclePaint
            )
        } else {
            val width = mCirclePaint.strokeWidth
            val style = mCirclePaint.style
            val color = mCirclePaint.color
            mCirclePaint.style = Paint.Style.FILL
            mCirclePaint.color = mAttrs.todayWeekBgColor
            canvas.drawCircle(
                rect.centerX(),
                rect.centerY(),
                mAttrs.selectCircleRadius,
                mCirclePaint
            )
            mCirclePaint.style = style
            mCirclePaint.color = color
            mCirclePaint.strokeWidth = width
        }
    }

    //绘制公历
    private fun drawTodaySolar(
        canvas: Canvas,
        rect: RectF,
        alphaColor: Int,
        date: LocalDate,
        isSelect: Boolean
    ) {
        if (isSelect) {
            mTextPaint.color = mAttrs.todaySolarSelectTextColor
        } else {
            mTextPaint.color = mAttrs.todaySolarTextColor
        }
        if (mAttrs.calendarTxtFontRes !== -1) {
            TextUtil.setTypeface(mTextPaint, mAttrs.calendarTxtFontRes)
        }
        mTextPaint.alpha = alphaColor
        mTextPaint.textSize = mAttrs.solarTextSize
        canvas.drawText(
            date.dayOfMonth.toString() + "",
            rect.centerX(),
            (if (mAttrs.isShowLunar) rect.centerY() else getBaseLineY(rect)).toFloat(),
            mTextPaint
        )
    }

    //绘制公历
    private fun drawSolar(
        canvas: Canvas,
        rect: RectF,
        alphaColor: Int,
        date: LocalDate,
        isSelect: Boolean
    ) {
        if (isSelect) {
            mTextPaint.color = mAttrs.solarSelectTextColor
        } else {
            mTextPaint.color = mAttrs.solarTextColor
        }
        if (mAttrs.calendarTxtFontRes !== -1) {
            TextUtil.setTypeface(mTextPaint, mAttrs.calendarTxtFontRes)
        }
        mTextPaint.alpha = alphaColor
        mTextPaint.textSize = mAttrs.solarTextSize
        canvas.drawText(
            date.dayOfMonth.toString() + "",
            rect.centerX(),
            (if (mAttrs.isShowLunar) rect.centerY() else getBaseLineY(rect)).toFloat(),
            mTextPaint
        )
    }

    private fun getBaseLineY(rect: RectF): Int {
        val fontMetrics = mTextPaint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        return (rect.centerY() - top / 2 - bottom / 2).toInt()
    }
}