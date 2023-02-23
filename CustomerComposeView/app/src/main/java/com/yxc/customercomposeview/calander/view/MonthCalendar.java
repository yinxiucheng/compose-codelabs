package com.yxc.customercomposeview.calander.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.yxc.customercomposeview.calander.listener.OnClickMonthViewListener;
import com.yxc.customercomposeview.calander.listener.OnMonthAnimatorListener;
import com.yxc.customercomposeview.calander.listener.OnMonthSelectListener;
import com.yxc.customercomposeview.calander.painter.BaseCalendarPainter;
import com.yxc.customercomposeview.calander.recycler.BaseCalendarAdapter;
import com.yxc.customercomposeview.calander.recycler.MonthCalendarAdapter;
import com.yxc.customercomposeview.calander.utils.CalendarAttrs;
import com.yxc.customercomposeview.utils.TimeDateUtil;

import org.joda.time.LocalDate;

public class MonthCalendar extends BaseCalendar implements OnClickMonthViewListener, ValueAnimator.AnimatorUpdateListener {
    protected ValueAnimator monthValueAnimator;//月日历动画
    private OnMonthSelectListener onMonthSelectListener;
    private OnMonthAnimatorListener onMonthAnimatorListener;


    public MonthCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, CalendarAttrs attrs, LocalDate initializeDate) {
        return new MonthCalendarAdapter(context, attrs, initializeDate,this);
    }

    public MonthCalendar(Context context, CalendarAttrs attrs, BaseCalendarPainter calendarPainter, int duration,
                         OnMonthAnimatorListener onMonthAnimatorListener) {
        super(context, attrs, calendarPainter);
        this.onMonthAnimatorListener = onMonthAnimatorListener;
        monthValueAnimator = new ValueAnimator();
        monthValueAnimator.setDuration(duration);
        monthValueAnimator.addUpdateListener(this);
    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return TimeDateUtil.getIntervalMonths(startDate, endDate);
    }

    @Override
    protected LocalDate getDate(LocalDate localDate, int count) {
        LocalDate date = localDate.plusMonths(count);
        return date;
    }

    @Override
    protected LocalDate getLastSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusMonths(-1);
    }

    @Override
    protected LocalDate getNextSelectDate(LocalDate currectSelectDate) {
        return currectSelectDate.plusMonths(1);
    }

    @Override
    protected void onSelcetDate(LocalDate localDate, boolean isClick) {
        if (onMonthSelectListener != null) {
            onMonthSelectListener.onMonthSelect(localDate,isClick);
        }
    }


    @Override
    public void onClickCurrentMonth(LocalDate localDate) {
        if (isClickDateEnable(localDate)) {
            onClickDate(localDate,0);
        } else {
            onClickDisableDate(localDate);
        }

    }

    @Override
    public void onClickLastMonth(LocalDate localDate) {
        if (isClickDateEnable(localDate)) {
            onClickDate(localDate,-1);
        } else {
            onClickDisableDate(localDate);
        }
    }

    @Override
    public void onClickNextMonth(LocalDate localDate) {
        if (isClickDateEnable(localDate)) {
            onClickDate(localDate,1);
        } else {
            onClickDisableDate(localDate);
        }
    }

    public void setOnMonthSelectListener(OnMonthSelectListener onMonthSelectListener) {
        this.onMonthSelectListener = onMonthSelectListener;
    }


    public int getMonthCalendarOffset() {
        if (mCurrView != null) {
            return mCurrView.getMonthCalendarOffset();
        }
        return 0;
    }

    public void autoToMonth() {
        float top = getY();//起始位置
        int end = 0;
        monthValueAnimator.setFloatValues(top, end);
        monthValueAnimator.start();
    }


    public void autoToMIUIWeek() {
        float top = getY();//起始位置
        int end = -getMonthCalendarOffset(); //结束位置
        monthValueAnimator.setFloatValues(top, end);
        monthValueAnimator.start();
    }

    public void autoToEMUIWeek() {
        float top = getY();//起始位置
        int end = -getHeight() * 4 / 5; //结束位置
        monthValueAnimator.setFloatValues(top, end);
        monthValueAnimator.start();
    }


    public boolean isMonthState() {
        return getY() >= 0;
    }

    public boolean isWeekState() {
        return getY() <= -getMonthCalendarOffset();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        float top = getY();
        float i = animatedValue - top;
        float y = getY();
        setY(i + y);

        if (onMonthAnimatorListener != null) {
            //回调遵循>0向上，<0向下
            onMonthAnimatorListener.onMonthAnimatorChanged((int) -i);
        }
    }

}
