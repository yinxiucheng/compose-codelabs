package com.yxc.customercomposeview.calander.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.yxc.customercomposeview.calander.painter.CalendarPainter;
import com.yxc.customercomposeview.calander.utils.CalendarAttrs;
import com.yxc.customercomposeview.utils.TimeDateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCalendarView extends View {
    public static final int DOTS_STATE_DATA = 1;

    private int mLineNum;//行数
    protected LocalDate mInitialDate;//由mInitialDate和周开始的第一天 算出当前页面的数据
    protected List<RectF> mRectList;//点击用的矩形集合
    protected List<LocalDate> mDateList;//页面的数据集合
    private LocalDate mSelectDate;//点击选中的日期
    private boolean isDraw;//是否绘制这个选中的日期

    private CalendarAttrs mAttrs;

    public BaseCalendarView(Context context, LocalDate localDate, CalendarAttrs attrs, int weekFirstDayType) {
        super(context);
        this.mInitialDate = this.mSelectDate = localDate;
        this.mAttrs = attrs;
        mDateList = TimeDateUtil.getMonthLocalDateCalendar(localDate);
        mRectList = new ArrayList<>();
        mLineNum = mDateList.size() / 7;//天数/7
    }
    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < mRectList.size(); i++) {
                RectF rect = mRectList.get(i);
                if (rect.contains((int) e.getX(), (int) e.getY())) {
                    LocalDate clickDate = mDateList.get(i);
                    onClick(clickDate, mInitialDate);
                    break;
                }
            }
            return true;
        }
    });

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制时获取区间开始结束日期和绘制类Painter
        BaseCalendar calendar = (BaseCalendar) getParent();
        LocalDate startDate = calendar.getStartDate();
        LocalDate endDate = calendar.getEndDate();
        CalendarPainter painter = calendar.getCalendarPainter();

        mRectList.clear();

        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                RectF rect = getRect(i, j);
                mRectList.add(rect);
                LocalDate date = mDateList.get(i * 7 + j);

                //在可用区间内的正常绘制，
                if (!(date.isBefore(startDate) || date.isAfter(endDate))) {
                    if (!TimeDateUtil.isSameMonth(mInitialDate, date) && !mAttrs.isShowOtherMonthDate) {
                        continue;
                    }
                    if (TimeDateUtil.isAfterToday(date)) {
                        painter.onDrawNotCurrentMonth(canvas, rect, date);
                    } else if (TimeDateUtil.isToday(date) && date.equals(mSelectDate)) {
                        painter.onDrawToday(canvas, rect, date, true);
                    } else if (TimeDateUtil.isToday(date) && !date.equals(mSelectDate)) {
                        painter.onDrawToday(canvas, rect, date, false);
                    } else if (date.equals(mSelectDate)) {//如果默认选择，就绘制，如果默认不选择且不是点击，就不绘制
                        painter.onDrawCurrentMonthOrWeek(canvas, rect, date, true);
                    } else {
                        painter.onDrawCurrentMonthOrWeek(canvas, rect, date, false);
                    }
                } else { //日期区间之外的日期
                    painter.onDrawDisableDate(canvas, rect, date);
                }
            }
        }
    }

    /**
     * 得到当前页面的数据，周和月
     *
     * @param initialDate 初始化当前页面数据的date
     * @param type        一周开始是周日还是周一
     * @return
     */
    protected abstract List<LocalDate> getNCalendar(LocalDate initialDate, int type);


    /**
     * 点击事件
     *
     * @param clickNData  点击的date
     * @param initialDate 初始化当前页面的date
     */
    protected abstract void onClick(LocalDate clickNData, LocalDate initialDate);


    //初始化的日期和绘制的日期是否是同月，周都相同
    public abstract boolean isEqualsMonthOrWeek(LocalDate date, LocalDate initialDate);


    //获取当前页面的初始日期
    public LocalDate getInitialDate() {
        return mInitialDate;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    //获取每个元素矩形
    private RectF getRect(int i, int j) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        RectF rect;
        //5行的月份，5行矩形平分view的高度
        int itemCircleWidth = (int) (mAttrs.selectCircleRadius * 2);
        int spaceWidth = (width - itemCircleWidth * 7) / 8;
        float startLTR = spaceWidth * (j + 1) + j * itemCircleWidth;
        if (mLineNum == 5) {
            int rectHeight = height / mLineNum;
            rect = new RectF(startLTR, i * rectHeight,
                    (j + 1) * (spaceWidth + itemCircleWidth), i * rectHeight + rectHeight);
        } else {
            //6行的月份，要第一行和最后一行矩形的中心分别和和5行月份第一行和最后一行矩形的中心对齐
            //5行一个矩形高度 mHeight/5, 画图可知,4个5行矩形的高度等于5个6行矩形的高度  故：6行的每一个矩形高度是  (mHeight/5)*4/5
            int rectHeight5 = height / 5;
            int rectHeight6 = (height / 5) * 4 / 5;
            rect = new RectF(startLTR, i * rectHeight6 + (rectHeight5 - rectHeight6) / 2,
                    (j + 1) * (spaceWidth + itemCircleWidth),
                    i * rectHeight6 + rectHeight6 + (rectHeight5 - rectHeight6) / 2);
        }
        return rect;
    }


    //设置选中的日期 并绘制
    public void setSelectDate(LocalDate localDate, boolean isDraw) {
        //默认选中和isDraw满足其一就绘制
        this.isDraw = isDraw;
        this.mSelectDate = localDate;
        invalidate();
    }

    //选中的日期到顶部的距离
    public int getMonthCalendarOffset() {
        int monthCalendarOffset;
        //选中的是第几行
        int selectIndex = mDateList.indexOf(mSelectDate) / 7;
        if (mLineNum == 5) {
            //5行的月份
            monthCalendarOffset = getMeasuredHeight() / 5 * selectIndex;
        } else {
            // int rectHeight5 = getMeasuredHeight() / 5;
            int rectHeight6 = (getMeasuredHeight() / 5) * 4 / 5;
            monthCalendarOffset = rectHeight6 * selectIndex;
        }
        return monthCalendarOffset;
    }


    //是否是当月日期
    public boolean contains(LocalDate localDate) {
        if (localDate == null) {
            return false;
        } else if (isEqualsMonthOrWeek(localDate, mInitialDate)) {
            return true;
        } else {
            return false;
        }
    }

}
