package com.yxc.customercomposeview.calander.recycler

import android.content.Context
import com.yxc.customercomposeview.calander.listener.OnClickWeekViewListener
import com.yxc.customercomposeview.calander.utils.CalendarAttrs
import com.yxc.customercomposeview.calander.view.BaseCalendarView
import com.yxc.customercomposeview.calander.view.WeekView
import com.yxc.customercomposeview.utils.TimeDateUtil
import org.joda.time.LocalDate

class WeekCalendarAdapter(
    context: Context?,
    attrs: CalendarAttrs?,
    initializeDate: LocalDate?,
    onClickWeekViewListener: OnClickWeekViewListener
) : BaseCalendarAdapter(context, attrs, initializeDate) {
    private val mOnClickWeekViewListener: OnClickWeekViewListener
    private val mAttrs: CalendarAttrs? = null

    init {
        this.mAttrs = attrs
        mOnClickWeekViewListener = onClickWeekViewListener
    }

    override fun getView(
        context: Context,
        weekFirstDayType: Int,
        initializeDate: LocalDate,
        curr: Int,
        position: Int
    ): BaseCalendarView {
        return WeekView(
            context,
            initializeDate.plusDays((position - curr) * 7),
            mAttrs,
            weekFirstDayType,
            mOnClickWeekViewListener
        )
    }

    override fun getIntervalCount(startDate: LocalDate, endDate: LocalDate, type: Int): Int {
        return TimeDateUtil.getIntervalWeek(startDate, endDate, type)
    }
}