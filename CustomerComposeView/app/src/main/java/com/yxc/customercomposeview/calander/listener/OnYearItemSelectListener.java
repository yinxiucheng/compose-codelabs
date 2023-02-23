package com.yxc.customercomposeview.calander.listener;


import com.yxc.customercomposeview.calander.model.MonthCalendarModel;

/**
 * @author yxc
 * @date 2019/3/18
 */
public interface OnYearItemSelectListener {
    void onSelect(MonthCalendarModel yearCalendar, int oldPosition, int newPosition);

}
