package com.yxc.customercomposeview.calander.listener;


import com.yxc.customercomposeview.calander.entity.NDate;

public interface OnCalendarChangedListener {
    void onCalendarDateChanged(NDate date, boolean isClick);

    void onCalendarStateChanged(boolean isMonthSate);

}
