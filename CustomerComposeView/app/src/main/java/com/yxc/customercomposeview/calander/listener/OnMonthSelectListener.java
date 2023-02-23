package com.yxc.customercomposeview.calander.listener;

import org.joda.time.LocalDate;

public interface OnMonthSelectListener {
    void onMonthSelect(LocalDate date, boolean isClick);
}
