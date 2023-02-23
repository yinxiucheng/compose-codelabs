package com.yxc.customercomposeview.calander.painter

import android.graphics.*
import android.util.Log
import com.yxc.customercomposeview.calander.model.ThreeCircleModel
import com.yxc.customercomposeview.calander.utils.CalendarAttrs
import com.yxc.customercomposeview.utils.TextUtil
import com.yxc.customercomposeview.utils.dpf
import org.joda.time.LocalDate

class InnerThreeCirclePainter(attrs: CalendarAttrs) : BaseCalendarPainter() {
    protected var mTextPaint: Paint
    protected var mCirclePaint: Paint
    private val mAttrs: CalendarAttrs
    private val noAlphaColor = 255
    private var circlePaint: Paint? = null
    private var transParentValue = 0
    private var mCircleModel: ThreeCircleModel

    init {
        mAttrs = attrs
        mTextPaint = paint
        mCirclePaint = paint
        mCircleModel = ThreeCircleModel.createDefaultModel()
        init()
    }

    protected val paint: Paint
        protected get() {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.textAlign = Paint.Align.CENTER
            return paint
        }

    override fun onDrawToday(canvas: Canvas, rect: RectF, localDate: LocalDate, isSelect: Boolean) {
//        mTextPaint.setColor(mAttrs.todayBgColor);
//        canvas.drawCircle(rect.centerX(), rect.centerY(), mAttrs.selectCircleRadius, mTextPaint);
        drawTodaySolar(canvas, rect, noAlphaColor, localDate, isSelect)
        //        canvas.drawRect(rect, mTextPaint);
    }

    override fun onDrawDisableDate(canvas: Canvas, rect: RectF, localDate: LocalDate) {
        drawSolar(canvas, rect, mAttrs.disabledAlphaColor, localDate, false)
    }

    override fun onDrawCurrentMonthOrWeek(
        canvas: Canvas,
        rect: RectF,
        localDate: LocalDate,
        isSelect: Boolean
    ) {
        if (isSelect) {
            drawSolar(canvas, rect, noAlphaColor, localDate, true)
            drawSelectDayDot(canvas, rect)
        } else {
            drawSolar(canvas, rect, noAlphaColor, localDate, false)
        }
    }

    override fun onDrawNotCurrentMonth(canvas: Canvas, rect: RectF, localDate: LocalDate) {
        drawSolar(canvas, rect, mAttrs.alphaColor, localDate, false)
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
            drawSelectDayDot(canvas, rect)
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
            if (mAttrs.isShowLunar) rect.centerY() else getBaseLineY(rect) + 5.dpf,
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
            drawSelectDayDot(canvas, rect)
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
            if (mAttrs.isShowLunar) rect.centerY() else getBaseLineY(rect) + 5.dpf,
            mTextPaint
        )
    }

    private fun getBaseLineY(rect: RectF): Int {
        val fontMetrics = mTextPaint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        return (rect.centerY() - top / 2 - bottom / 2).toInt()
    }

    private fun drawSelectDayDot(canvas: Canvas, rect: RectF) {
        val originalColor = mTextPaint.color
        mTextPaint.color = mAttrs.solarSelectTextColor
        val txtHeight: Float =
            (mTextPaint.descent() - mTextPaint.ascent()) / 2 + 6.dpf
        canvas.drawCircle(
            rect.centerX(),
            (rect.centerY() + txtHeight + CalendarPainter.DOT_OFFSET).toInt().toFloat(),
            CalendarPainter.DOT_RADIUS.toFloat(),
            mTextPaint
        )
        mTextPaint.color = originalColor
    }

    private fun drawThreeCircle(canvas: Canvas, leftTop: PointF, width: Float) {
        val itemWidth = width / 6.5f
        val padding = itemWidth / 2
        val spaceWidth = itemWidth / 7.5f
        val paintWidth = itemWidth - spaceWidth
        drawCircle(canvas, leftTop, width, padding, paintWidth)
        drawCircle2(canvas, leftTop, width, padding, itemWidth, paintWidth)
        drawCircle3(canvas, leftTop, width, padding, itemWidth, paintWidth)
    }

    private fun init() {
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint!!.style = Paint.Style.STROKE
        circlePaint!!.strokeCap = Paint.Cap.BUTT
        transParentValue = (255 * 0.2).toInt()
    }

    private fun drawCircle(
        canvas: Canvas,
        leftTop: PointF,
        width: Float,
        padding: Float,
        paintWidth: Float
    ) {
        canvas.save()
        val originalColor = circlePaint!!.color
        val path = Path()
        val bgRectF = RectF(
            leftTop.x + padding,
            leftTop.y + padding,
            leftTop.x + width - padding,
            leftTop.y + width - padding
        )
        path.arcTo(bgRectF, 180f, 180f, true)
        circlePaint!!.color = mCircleModel.firstCircleColor
        circlePaint!!.alpha = transParentValue
        circlePaint!!.strokeWidth = paintWidth
        canvas.drawPath(path, circlePaint!!)
        val path2 = Path()
        path2.arcTo(bgRectF, 180f, 180 * mCircleModel.firstPercentage, true)
        circlePaint!!.strokeWidth = paintWidth
        circlePaint!!.alpha = 255
        canvas.drawPath(path2, circlePaint!!)
        circlePaint!!.color = originalColor
        canvas.restore()
    }

    private fun drawCircle2(
        canvas: Canvas,
        leftTop: PointF,
        width: Float,
        padding: Float,
        itemWidth: Float,
        paintWidth: Float
    ) {
        canvas.save()
        val bgRectF = RectF(
            leftTop.x + padding + itemWidth,
            leftTop.y + padding + itemWidth,
            leftTop.x + width - (padding + itemWidth),
            leftTop.y + width - (padding + itemWidth)
        )
        val path = Path()
        path.arcTo(bgRectF, 180f, 180f, true)
        circlePaint!!.color = mCircleModel.secondCircleColor
        circlePaint!!.alpha = transParentValue
        circlePaint!!.strokeWidth = paintWidth
        canvas.drawPath(path, circlePaint!!)
        val path2 = Path()
        path2.arcTo(bgRectF, 180f, 180 * mCircleModel.secondPercentage, true)
        circlePaint!!.strokeWidth = paintWidth
        circlePaint!!.alpha = 255
        canvas.drawPath(path2, circlePaint!!)
        canvas.restore()
    }

    private fun drawCircle3(
        canvas: Canvas,
        leftTop: PointF,
        width: Float,
        padding: Float,
        itemWidth: Float,
        paintWidth: Float
    ) {
        canvas.save()
        val bgRectF = RectF(
            leftTop.x + padding + 2 * itemWidth,
            leftTop.y + padding + 2 * itemWidth,
            leftTop.x + width - (padding + 2 * itemWidth),
            leftTop.y + width - (padding + 2 * itemWidth)
        )
        val path = Path()
        path.arcTo(bgRectF, 180f, 180f, true)
        circlePaint!!.strokeWidth = paintWidth
        circlePaint!!.color = mCircleModel.thirdCircleColor
        circlePaint!!.alpha = transParentValue
        canvas.drawPath(path, circlePaint!!)
        val path2 = Path()
        path2.arcTo(bgRectF, 180f, 180 * mCircleModel.thirdPercentage, true)
        circlePaint!!.strokeWidth = paintWidth
        circlePaint!!.alpha = 255
        canvas.drawPath(path2, circlePaint!!)
        canvas.restore()
    }

    override fun onDrawThreeCircle(canvas: Canvas, rectF: RectF, circleModel: ThreeCircleModel) {
        super.onDrawThreeCircle(canvas, rectF, circleModel)
        Log.d("onDrawThreeCircle", " CircleModel:$circleModel")
        mCircleModel = circleModel
        drawThreeCircle(canvas, PointF(rectF.left, rectF.top), rectF.width())
    }
}