package com.yxc.customercomposeview.calander.listener;


import com.yxc.customercomposeview.calander.view.BaseCalendar;

public interface OnYearMonthChangedListener {
    void onYearMonthChanged(BaseCalendar baseCalendar, int year, int month, boolean isClick);
}
