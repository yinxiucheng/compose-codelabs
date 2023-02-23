package com.yxc.customercomposeview.calander.recycler

import android.content.Context
import com.yxc.customercomposeview.calander.listener.OnClickMonthViewListener
import com.yxc.customercomposeview.calander.utils.CalendarAttrs
import com.yxc.customercomposeview.calander.view.BaseCalendarView
import com.yxc.customercomposeview.calander.view.MonthView
import com.yxc.customercomposeview.utils.TimeDateUtil.getIntervalMonths
import org.joda.time.LocalDate

class MonthCalendarAdapter(
    context: Context?,
    attrs: CalendarAttrs?,
    initializeDate: LocalDate?,
    onClickMonthViewListener: OnClickMonthViewListener
) : BaseCalendarAdapter(context, attrs, initializeDate) {
    private val mOnClickMonthViewListener: OnClickMonthViewListener
    private val mAttrs: CalendarAttrs? = null

    init {
        this.mAttrs = attrs
        mOnClickMonthViewListener = onClickMonthViewListener
    }

    override fun getView(
        context: Context,
        weekFirstDayType: Int,
        initializeDate: LocalDate,
        curr: Int,
        position: Int
    ): BaseCalendarView {
        val date = initializeDate.plusMonths(position - curr)
        return MonthView(context, date, mAttrs, weekFirstDayType, mOnClickMonthViewListener)
    }

    override fun getIntervalCount(startDate: LocalDate, endDate: LocalDate, type: Int): Int {
        return getIntervalMonths(startDate, endDate)
    }
}