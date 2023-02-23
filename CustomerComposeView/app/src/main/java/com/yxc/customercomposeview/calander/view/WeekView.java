package com.yxc.customercomposeview.calander.view;

import android.content.Context;


import com.yxc.customercomposeview.calander.entity.NDate;
import com.yxc.customercomposeview.calander.listener.OnClickWeekViewListener;
import com.yxc.customercomposeview.calander.utils.CalendarAttrs;
import com.yxc.customercomposeview.calander.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by necer on 2018/9/11.
 * qq群：127278900
 */
public class WeekView extends BaseCalendarView {

    private OnClickWeekViewListener mOnClickWeekViewListener;

    public WeekView(Context context, LocalDate localDate, CalendarAttrs attrs, int weekFirstDayType, OnClickWeekViewListener onClickWeekViewListener) {
        super(context, localDate, attrs, weekFirstDayType);
        this.mOnClickWeekViewListener = onClickWeekViewListener;
    }

    @Override
    protected List<LocalDate> getNCalendar(LocalDate localDate, int type) {
        return CalendarUtil.getWeekCalendar(localDate, type);
    }

    @Override
    protected void onClick(LocalDate localDate, LocalDate initialDate) {
        mOnClickWeekViewListener.onClickCurrentWeek(localDate);
    }

    @Override
    public boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate) {
        return mDateList.contains(new NDate(date));
    }
}
