package com.yxc.customercomposeview.calander.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.calander.model.MonthCalendarModel
import com.yxc.customercomposeview.calander.painter.CalendarPainter.Companion.DOT_OFFSET
import com.yxc.customercomposeview.calander.painter.CalendarPainter.Companion.DOT_RADIUS
import com.yxc.customercomposeview.calander.utils.CalendarAttrs
import com.yxc.customercomposeview.calander.utils.CalendarAttrsUtil.getAttrs
import com.yxc.customercomposeview.common.ColorUtil.getResourcesColor
import com.yxc.customercomposeview.utils.AppUtil.isRTLDirection
import com.yxc.customercomposeview.utils.TimeDateUtil.getDateMMSimpleFormat
import com.yxc.customercomposeview.utils.TimeDateUtil.isSameMonthLocalDate
import org.joda.time.LocalDate

/**
 * @author yxc
 * @date 2019/3/18
 */
class MonthCalendarView(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {
    private var mDateList //月数
            : MutableList<MonthCalendarModel>? = null
    protected var mRectList: MutableList<Rect> = mutableListOf()
    private val mTextPaint: Paint
    private val mCirclePaint: Paint
    private val mAttrs: CalendarAttrs
    private var mSelectYearCalendar: MonthCalendarModel? = null
    private var mListener: OnYearCalendarItemClickListener? = null
    private var dots: HashMap<Long, Int>? = null
    private val mTouchHelper: CalendarViewTouchHelper
    fun bindData(localDate: LocalDate, selectLocalDate: LocalDate, states: HashMap<Long, Int>?) {
        mDateList = createYearCalendarList(localDate)
        dots = states
        if (localDate.year != selectLocalDate.year) {
            mSelectYearCalendar = null
        } else {
            mSelectYearCalendar = findMonthCalendar(selectLocalDate)
            if (null != mSelectYearCalendar) {
                mSelectYearCalendar!!.isSelected = true
            }
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until LINE_NUMBER) {
            for (j in 0 until COLUMN_MONTH) {
                val rect = getRect(i, j)
                mRectList.add(rect)
                val yearCalendar = mDateList!![i * COLUMN_MONTH + j]
                //在可用区间内的正常绘制，
                mTextPaint.textSize = mAttrs.monthTextSize
                if (yearCalendar.isFuture) {
                    mTextPaint.color =
                        getResourcesColor(R.color.cpb_unclick_color)
                } else if (yearCalendar.isCurrent) {
                    mTextPaint.color =
                        getResourcesColor(R.color.text_color)
                    drawTodayBg(canvas, rect)
                } else if (yearCalendar.isSelected) {
                    mTextPaint.color =
                        getResourcesColor(R.color.text_color)
                    drawSelectCircle(canvas, rect, false)
                } else {
                    mTextPaint.color =
                        getResourcesColor(R.color.text_color)
                }
                val month = yearCalendar.localDate!!.toDate().time / DateUtils.SECOND_IN_MILLIS
                val dateStr = getDateMMSimpleFormat(yearCalendar.localDate!!)
                canvas.drawText(
                    dateStr,
                    rect.centerX().toFloat(),
                    getBaseLineY(rect).toFloat(),
                    mTextPaint
                )
                if (dots != null) {
                    val state = dots!![month]
                    if (state != null && state == BaseCalendarView.DOTS_STATE_DATA) {
                        val txtHeight = (mTextPaint.descent() - mTextPaint.ascent()) / 2
                        canvas.drawCircle(
                            rect.centerX().toFloat(),
                            (rect.centerY() + txtHeight + DOT_OFFSET * 2),
                            DOT_RADIUS.toFloat(),
                            mTextPaint
                        )
                    }
                }
            }
        }
    }

    private fun drawTodayBg(canvas: Canvas, rect: Rect) {
        mCirclePaint.style = Paint.Style.FILL
        mCirclePaint.color = mAttrs.todayWeekBgColor
        canvas.drawCircle(
            rect.centerX().toFloat(),
            rect.centerY().toFloat(),
            mAttrs.selectCircleRadius,
            mCirclePaint
        )
    }

    //实心圆
    private fun drawSelectCircle(canvas: Canvas, rect: Rect, isTodaySelect: Boolean) {
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = mAttrs.hollowCircleStroke
        mCirclePaint.color = mAttrs.selectCircleColor
        canvas.drawCircle(
            rect.centerX().toFloat(),
            rect.centerY().toFloat(),
            mAttrs.selectCircleRadius,
            mCirclePaint
        )
    }

    //获取每个元素矩形
    private fun getRect(i: Int, j: Int): Rect {
        val width = measuredWidth
        val height = measuredHeight
        return if (isRTLDirection()) {
            val rectHeight = height / LINE_NUMBER
            val end = right - j * width / COLUMN_MONTH
            val start = end - width / COLUMN_MONTH
            Rect(start, i * rectHeight, end, i * rectHeight + rectHeight)
        } else {
            val rectHeight = height / LINE_NUMBER
            val start = j * width / COLUMN_MONTH
            val end =
                j * width / COLUMN_MONTH + width / COLUMN_MONTH
            Rect(start, i * rectHeight, end, i * rectHeight + rectHeight)
        }
    }

    private fun getBaseLineY(rect: Rect): Int {
        val fontMetrics = mTextPaint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        return (rect.centerY() - top / 2 - bottom / 2).toInt()
    }

    private val paint: Paint
        private get() {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.textAlign = Paint.Align.CENTER
            return paint
        }

    private fun createYearCalendarList(selectDate: LocalDate): MutableList<MonthCalendarModel> {
        if (mDateList == null) {
            mDateList = ArrayList()
        } else {
            mDateList!!.clear()
        }
        val currentMonth = LocalDate.now().monthOfYear
        val selectMonth = selectDate.monthOfYear
        for (i in 1..MONTH_OF_YEAR) {
            val monthCalendar = MonthCalendarModel()
            val monthDistance = i - selectMonth
            val localDate = selectDate.plusMonths(monthDistance)
            val month = localDate.monthOfYear
            monthCalendar.localDate = localDate
            if (isFuture(localDate)) {
                monthCalendar.isFuture = true
            } else if (isCurrentYear(selectDate) && month == currentMonth) {
                monthCalendar.isCurrent = true
            } else {
                monthCalendar.isCurrent = false
            }
            mDateList!!.add(monthCalendar)
        }
        return mDateList!!
    }

    fun isCurrentYear(localDate: LocalDate): Boolean {
        return if (localDate.year == LocalDate.now().year) {
            true
        } else false
    }

    fun isFuture(localDate: LocalDate): Boolean {
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year
        val currentMonth = currentDate.monthOfYear
        val selectMonth = localDate.monthOfYear
        val selectYear = localDate.year
        if (currentYear < selectYear) {
            return true
        } else if (selectYear < currentYear) {
            return false
        } else if (selectYear == currentYear && selectMonth > currentMonth) {
            return true
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mGestureDetector.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private val mGestureDetector =
        GestureDetector(getContext(), object : SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                for (i in mRectList.indices) {
                    val rect = mRectList[i]
                    if (rect.contains(e.x.toInt(), e.y.toInt())) {
                        val yearCalendar = mDateList!![i] //去掉点击效果
                        if (onClick(yearCalendar)) {
                            invalidate()
                        }
                        break
                    }
                }
                return true
            }
        })

    init {
        mAttrs = getAttrs(context!!, attributeSet)
        mTextPaint = paint
        mTextPaint.typeface = Typeface.DEFAULT_BOLD
        mCirclePaint = paint
        mTouchHelper = CalendarViewTouchHelper(this)
        ViewCompat.setAccessibilityDelegate(this, mTouchHelper)
    }

    fun setOnYearCalendarItemClickListener(listener: OnYearCalendarItemClickListener?) {
        mListener = listener
    }

    private fun onClick(yearCalendar: MonthCalendarModel): Boolean {
        if (yearCalendar.isFuture || yearCalendar.isSelected) {
            return false
        }
        if (null != mSelectYearCalendar) {
            mSelectYearCalendar!!.isSelected = false
        }
        yearCalendar.isSelected = true
        mSelectYearCalendar = yearCalendar
        if (null != mListener) {
            mListener!!.onYearItemClick(yearCalendar.localDate!!)
        }
        return true
    }

    private fun findMonthCalendar(selectDate: LocalDate): MonthCalendarModel? {
        for (i in mDateList!!.indices) {
            val monthCalendar = mDateList!![i]
            if (isSameMonthCalendar(selectDate, monthCalendar)) {
                return monthCalendar
            } else {
                monthCalendar.isSelected = false
            }
        }
        return null
    }

    private fun isSameMonthCalendar(
        localDate: LocalDate,
        mSelectYearCalendar: MonthCalendarModel?
    ): Boolean {
        if (mSelectYearCalendar == null) {
            return false
        }
        val mSelectDate = mSelectYearCalendar.localDate
        return if (isSameMonthLocalDate(localDate, mSelectDate!!)) {
            true
        } else false
    }

    interface OnYearCalendarItemClickListener {
        fun onYearItemClick(localDate: LocalDate)
    }

    override fun dispatchGenericFocusedEvent(event: MotionEvent): Boolean {
        return if (mTouchHelper.dispatchHoverEvent(event)) {
            true
        } else super.dispatchGenericFocusedEvent(
            event
        )
    }

    private inner class CalendarViewTouchHelper internal constructor(forView: View?) :
        ExploreByTouchHelper(
            forView!!
        ) {
        private val mRectBounds = Rect()
        override fun getVirtualViewAt(x: Float, y: Float): Int {
            for (i in mRectList.indices) {
                val rect = mRectList[i]
                if (rect.contains(x.toInt(), y.toInt())) {
                    return i
                }
            }
            return -1
        }

        override fun getVisibleVirtualViews(virtualViewIds: MutableList<Int>) {
            for (i in mRectList.indices) {
                virtualViewIds.add(i)
            }
        }

        override fun onPopulateNodeForVirtualView(
            virtualViewId: Int,
            node: AccessibilityNodeInfoCompat
        ) {
            if (virtualViewId >= 0 && virtualViewId < mRectList.size && virtualViewId < mDateList!!.size) {
                val rect = mRectList[virtualViewId]
                mRectBounds[rect.left, rect.top, rect.right] = rect.bottom
                node.setBoundsInParent(mRectBounds)
                val model = mDateList!![virtualViewId]
                val contentDescription = getDateMMSimpleFormat(model.localDate!!)
                node.isClickable = true
                node.contentDescription = contentDescription
            } else {
                mRectBounds.setEmpty()
                node.setBoundsInParent(mRectBounds)
                node.contentDescription = ""
                node.isClickable = false
            }
        }

        override fun onPerformActionForVirtualView(
            virtualViewId: Int,
            action: Int,
            arguments: Bundle?
        ): Boolean {
            if (action == AccessibilityNodeInfoCompat.ACTION_CLICK && virtualViewId >= 0 && virtualViewId < mRectList.size && virtualViewId < mDateList!!.size) {
                onClick(mDateList!![virtualViewId])
            }
            return false
        }
    }

    companion object {
        const val COLUMN_MONTH = 4
        const val MONTH_OF_YEAR = 12
        const val LINE_NUMBER = 3
    }
}