package com.yxc.customercomposeview.calander.view;

import android.content.Context;


import com.yxc.customercomposeview.calander.listener.OnClickMonthViewListener;
import com.yxc.customercomposeview.calander.utils.CalendarAttrs;
import com.yxc.customercomposeview.calander.utils.CalendarUtil;
import com.yxc.customercomposeview.utils.TimeDateUtil;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class MonthView extends BaseCalendarView {

    private OnClickMonthViewListener mOnClickMonthViewListener;


    public MonthView(Context context, LocalDate localDate, CalendarAttrs attrs, int weekFirstDayType, OnClickMonthViewListener onClickMonthViewListener) {
        super(context, localDate, attrs, weekFirstDayType);
        this.mOnClickMonthViewListener = onClickMonthViewListener;
    }


    @Override
    protected List<LocalDate> getNCalendar(LocalDate localDate, int type) {
        return CalendarUtil.getMonthCalendar(localDate,type);
    }

    @Override
    protected void onClick(LocalDate localDate, LocalDate initialDate) {
        if (TimeDateUtil.isLastMonth(initialDate, localDate)) {
            mOnClickMonthViewListener.onClickLastMonth(localDate);
        } else if (TimeDateUtil.isNextMonth(initialDate, localDate)) {
            mOnClickMonthViewListener.onClickNextMonth(localDate);
        } else {
            mOnClickMonthViewListener.onClickCurrentMonth(localDate);
        }
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return TimeDateUtil.isEqualsMonth(date, initialDate);
    }
}
