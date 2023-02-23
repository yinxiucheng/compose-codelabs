package com.yxc.customercomposeview.calander.view;



import static com.yxc.customercomposeview.calander.view.BaseCalendarView.DOTS_STATE_DATA;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;


import com.yxc.customercomposeview.calander.painter.BaseCalendarPainter;
import com.yxc.customercomposeview.calander.painter.InnerMonthPainter;
import com.yxc.customercomposeview.calander.utils.CalendarAttrs;
import com.yxc.customercomposeview.calander.utils.CalendarAttrsUtil;
import com.yxc.customercomposeview.utils.AppUtil;
import com.yxc.customercomposeview.utils.DisplayUtil;
import com.yxc.customercomposeview.utils.TimeDateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeekCalendarView extends View {

    private int mLineNum;//行数
    protected LocalDate mInitialDate;//由mInitialDate和周开始的第一天 算出当前页面的数据
    protected List<RectF> mRectList;//点击用的矩形集合
    protected List<LocalDate> mDateList;//页面的数据集合
    private LocalDate mSelectDate;//点击选中的日期
    private int selectDayWeek;
    private int todayWeek;
    private CalendarAttrs mAttrs;
    private BaseCalendarPainter mCalendarPainter;

    protected List<RectF> mCurrentRectList;//本月Rect集合
    protected List<LocalDate> mCurrentMonthDateList;//本月日期集合

    private @Nullable HashMap<Long, Integer> dots;

    private CalendarViewTouchHelper mTouchHelper;

    public WeekCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = CalendarAttrsUtil.getAttrs(context, attrs);
        LocalDate localDate = LocalDate.now();
        this.mInitialDate = this.mSelectDate = localDate;
        mDateList = TimeDateUtil.getMonthLocalDateCalendar(localDate);
        mRectList = new ArrayList<>();

        mCurrentRectList = new ArrayList<>();
        mCurrentMonthDateList = new ArrayList<>();

        mLineNum = mDateList.size() / 7; //天数/7
        this.mCalendarPainter = new InnerMonthPainter(mAttrs);

        //无障碍虚拟View 委托。
        mTouchHelper = new CalendarViewTouchHelper(this);
        ViewCompat.setAccessibilityDelegate(this, mTouchHelper);
    }

    public void setLocalData(LocalDate localDate, HashMap<Long, Integer> states) {
        this.mInitialDate = localDate;
        this.mDateList = TimeDateUtil.getMonthLocalDateCalendar(localDate);
        dots = states;
        mLineNum = mDateList.size() / 7; //天数/7
        invalidate();
    }

    public void setSelectDate(LocalDate selectDate) {
        this.mSelectDate = selectDate;
        invalidate();
    }

    public void setSelectDateInvalidate(LocalDate selectDate) {
        if (isSameWeekWithSelectDay(selectDate)) {
            return;
        }
        onClick(selectDate);
    }

    public BaseCalendarPainter getCalendarPainter() {
        return mCalendarPainter;
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
                    LocalDate localDate = mDateList.get(i);
                    onClick(localDate);
                    break;
                }
            }
            return true;
        }
    });

    private boolean isSameWeekWithSelectDay(LocalDate localDate) {
        int week = TimeDateUtil.getDayWeek(localDate);
        return week == getSelectDayWeek();
    }

    private void drawSelectWeek(Canvas canvas, BaseCalendarPainter painter, int lineNum) {
        LocalDate firstDayOfWeek = mDateList.get(lineNum * 7);
        int currentLineWeek = TimeDateUtil.getDayWeek(firstDayOfWeek);
        boolean isThisYear = firstDayOfWeek.getYear() == LocalDate.now().getYear();
        int roundX = DisplayUtil.dip2px(25);
        int roundY = roundX;

        RectF rectF = getWeekRect(lineNum);
//        Log.d("WeekCalendar", "selectDayWeek = " + selectDayWeek + "; todayWeek = " + todayWeek);
        if (currentLineWeek == selectDayWeek && currentLineWeek != todayWeek) {
            painter.onDrawWeekBg(canvas, rectF, roundX, roundY, false);
        } else if (currentLineWeek == todayWeek && isThisYear) {//要求年份一样。
            painter.onDrawWeekBg(canvas, rectF, roundX, roundY, true);
        }
    }

    private RectF getWeekRect(int lineNum) {
        int type = 1;
        RectF rectF;
        if (lineNum == 0) {//第一行
            type = 0;
            List<LocalDate> localDateList = mDateList.subList(0, 7);
            rectF = getWeekRectInner(lineNum, localDateList, type);
        } else if (lineNum == mLineNum - 1) {//最后一行
            type = 2;
            int lastIndex = mDateList.size();
            List<LocalDate> localDateList = mDateList.subList(lastIndex - 7, lastIndex);
            rectF = getWeekRectInner(lineNum, localDateList, type);
        } else {
            List<LocalDate> localDateList = new ArrayList<>();//这里subList不需要用到
            rectF = getWeekRectInner(lineNum, localDateList, type);
        }
        return rectF;
    }


    //获取每个元素矩形
    private RectF getWeekRectInner(int i, List<LocalDate> subDataList, int type) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float itemCircleWidth =  (mAttrs.selectCircleRadius * 2);
        float spaceWidth = (width - itemCircleWidth * 7) / 8;
        RectF rect = new RectF();
        int rectTop;
        int rectBottom;
        int rectHeight5 = height / 5;
        int rectHeight6 = (height / 5) * 4 / 5;
        int heightValue = (rectHeight5 - rectHeight6) / 2;
        if (mLineNum == 5) {
            rectTop = i * rectHeight5 + heightValue + DisplayUtil.dip2px(3);
            rectBottom = i * rectHeight5 + rectHeight5 - heightValue - DisplayUtil.dip2px(3);
        } else {
            rectTop = i * rectHeight6 + heightValue + DisplayUtil.dip2px(3);
            rectBottom = i * rectHeight6 + rectHeight6 + heightValue - DisplayUtil.dip2px(3);
        }
        if (type == 0) {
            int monthEnd = subDataList.get(6).getMonthOfYear();
            int rectBegin = 0;
            for (int j = 0; j < subDataList.size(); j++) {
                LocalDate localDate = subDataList.get(j);
                int month = localDate.getMonthOfYear();
                if (month == monthEnd) {
                    rectBegin = j;
                    break;
                }
            }
            float rectLeft = spaceWidth * (rectBegin + 1) + rectBegin * itemCircleWidth;
            float rectRight = width - spaceWidth;
            if (AppUtil.isRTLDirection()){
                rectRight = getRight() - (spaceWidth * (rectBegin + 1) + rectBegin * itemCircleWidth);
                rectLeft = spaceWidth;
            }
            rect.set(rectLeft, rectTop, rectRight, rectBottom);
        } else if (type == 2) {
            int monthFirst = subDataList.get(0).getMonthOfYear();
            int endIndex = subDataList.size() - 1;
            int rectEnd = endIndex;
            for (int j = endIndex; j >= 0; j--) {
                LocalDate localDate = subDataList.get(j);
                int month = localDate.getMonthOfYear();
                Log.d("WeekCalendarView", "month is:" + month);
                if (month == monthFirst) {
                    rectEnd = j;
                    break;
                }
            }
            float rectLeft = spaceWidth;
            float rectRight = width - ((7 - rectEnd) * spaceWidth + (6 - rectEnd) * itemCircleWidth);
            if (AppUtil.isRTLDirection()){
                rectRight = getRight() - spaceWidth;
                rectLeft = (6 - rectEnd) * (spaceWidth + itemCircleWidth);
            }
            rect.set(rectLeft, rectTop, rectRight, rectBottom);
        } else {
            float rectLeft = spaceWidth;
            float rectRight = width - spaceWidth;
            rect.set(rectLeft, rectTop, rectRight, rectBottom);
        }
        return rect;
    }

    //选中的week
    private int getSelectDayWeek() {
        return TimeDateUtil.getDayWeek(mSelectDate);
    }

    public List<LocalDate> getMonthDateList() {
        return mDateList;
    }

    public LocalDate getSelectDate() {
        return mSelectDate;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制时获取区间开始结束日期和绘制类Painter
        BaseCalendarPainter painter = getCalendarPainter();
        mRectList.clear();
        selectDayWeek = getSelectDayWeek();
        todayWeek = TimeDateUtil.getTodayWeek();
        for (int i = 0; i < mLineNum; i++) {
            drawSelectWeek(canvas, painter, i);
            for (int j = 0; j < 7; j++) {
                RectF rect = getRect(i, j);
                mRectList.add(rect);
                LocalDate date = mDateList.get(i * 7 + j);
                if (!TimeDateUtil.isSameMonth(mInitialDate, date) && !mAttrs.isShowOtherMonthDate) {
                    continue;
                }
                mCurrentRectList.add(rect);
                mCurrentMonthDateList.add(date);
                boolean afterToday = TimeDateUtil.isAfterToday(date);
                if (afterToday) {
                    painter.onDrawNotCurrentMonth(canvas, rect, date);
                } else if (TimeDateUtil.isToday(date)){
                    painter.onDrawToday(canvas, rect, date, true);
                }else if (TimeDateUtil.isSameWeekWithToday(date) || isSameWeekWithSelectDay(date)) {
                    painter.onDrawCurrentMonthOrWeek(canvas, rect, date, false);
                } else {
                    painter.onDrawCurrentMonthOrWeek(canvas, rect, date, false);
                }
                if (!afterToday && dots != null) {
                    Integer state = dots.get(date.toDate().getTime() / DateUtils.SECOND_IN_MILLIS);
                    if (state != null && state == DOTS_STATE_DATA) {
                        painter.onDrawDot(canvas, rect);
                    }
                }
            }
        }
    }

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
        if (AppUtil.isRTLDirection()) {
            float end = getRight() - startLTR;
            float start = end - (spaceWidth + itemCircleWidth);
            if (mLineNum == 5) {
                int rectHeight = height / mLineNum;
                rect = new RectF(start, i * rectHeight, end, i * rectHeight + rectHeight);
            } else {
                //6行的月份，要第一行和最后一行矩形的中心分别和和5行月份第一行和最后一行矩形的中心对齐
                //5行一个矩形高度 mHeight/5, 画图可知,4个5行矩形的高度等于5个6行矩形的高度  故：6行的每一个矩形高度是  (mHeight/5)*4/5
                int rectHeight5 = height / 5;
                int rectHeight6 = (height / 5) * 4 / 5;
                rect = new RectF(start, i * rectHeight6 + (rectHeight5 - rectHeight6) / 2, end, i * rectHeight6 + rectHeight6 + (rectHeight5 - rectHeight6) / 2);
            }
            return rect;
        } else {
            float start = startLTR;
            float end = (j + 1) * (spaceWidth + itemCircleWidth);
            if (mLineNum == 5) {
                int rectHeight = height / mLineNum;
                rect = new RectF(start, i * rectHeight, end, i * rectHeight + rectHeight);
            } else {
                //6行的月份，要第一行和最后一行矩形的中心分别和和5行月份第一行和最后一行矩形的中心对齐
                //5行一个矩形高度 mHeight/5, 画图可知,4个5行矩形的高度等于5个6行矩形的高度  故：6行的每一个矩形高度是  (mHeight/5)*4/5
                int rectHeight5 = height / 5;
                int rectHeight6 = (height / 5) * 4 / 5;
                rect = new RectF(start, i * rectHeight6 + (rectHeight5 - rectHeight6) / 2, end, i * rectHeight6 + rectHeight6 + (rectHeight5 - rectHeight6) / 2);
            }
            return rect;
        }
    }


    private void calculateData(LocalDate localDate) {
        mDateList = TimeDateUtil.getMonthLocalDateCalendar(localDate);
        mLineNum = mDateList.size() / 7;
    }

    protected void onClick(LocalDate localDate) {
        if (!TimeDateUtil.isSameMonth(mInitialDate, localDate) && !mAttrs.isShowOtherMonthDate) {
            return;
        }
        if (TimeDateUtil.isLastMonth(mSelectDate, localDate)) {
            mSelectDate = localDate;
            animatorChange(localDate, 0, -DisplayUtil.getScreenWidth(getContext()), 300);
        } else if (!TimeDateUtil.isAfterToday(localDate)) {
            if (TimeDateUtil.isNextMonth(localDate, mSelectDate)) {
                mSelectDate = localDate;
                animatorChange(localDate, 0, DisplayUtil.getScreenWidth(getContext()), 300);
            } else {
                mSelectDate = localDate;
                invalidateViewAndData(localDate);
            }
        } else if (TimeDateUtil.isSameWeekWithToday(localDate)) {//未来但是跟今天是同一周的情况。
            if (TimeDateUtil.isNextMonth(localDate, mSelectDate)) {
                mSelectDate = localDate;
                animatorChange(localDate, 0, DisplayUtil.getScreenWidth(getContext()), 300);
            } else {
                mSelectDate = localDate;
                invalidateViewAndData(localDate);
            }
        }
    }

    //换月的时候添加动画切换
    private void animatorChange(final LocalDate localDate, final float begin, final float end, final long duration) {
        if (mAttrs.showAnimator) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(
                    this, "translationX", begin, end);
            animator.setDuration(duration * 2 / 3);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();
            animator.addListener(new MyAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    calculateData(localDate);
                    invalidateViewAndData(localDate);
                    ObjectAnimator animatorInner = ObjectAnimator.ofFloat(
                            WeekCalendarView.this, "translationX", end, begin);
                    animatorInner.setDuration(duration * 4 / 3);
                    animatorInner.setInterpolator(new DecelerateInterpolator());
                    animatorInner.start();
                }
            });
        } else {
            calculateData(localDate);
            invalidateViewAndData(localDate);
        }
    }

    private void invalidateViewAndData(LocalDate localDate) {
        if (null != mWeekCalendarItemSelectListener) {
            mWeekCalendarItemSelectListener.onWeekItemSelect(localDate);
        }
        invalidate();
    }

    private OnWeekCalendarItemSelectListener mWeekCalendarItemSelectListener;

    public interface OnWeekCalendarItemSelectListener {
        void onWeekItemSelect(LocalDate localDate);
    }

    public void setOnWeekCalendarItemSelectListener(OnWeekCalendarItemSelectListener listener) {
        this.mWeekCalendarItemSelectListener = listener;
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        if (mTouchHelper.dispatchHoverEvent(event)){
            return true;
        }
        return super.dispatchHoverEvent(event);
    }


    private class CalendarViewTouchHelper extends ExploreByTouchHelper{

        private final Rect mRectBounds = new Rect();

        CalendarViewTouchHelper(View forView){
            super(forView);
        }

        @Override
        protected int getVirtualViewAt(float x, float y) {
            for (int i = 0; i < mCurrentRectList.size(); i++) {
                RectF rectF = mCurrentRectList.get(i);
                if (rectF.contains(x, y)){
                    return i;
                }
            }
            return -1;
        }

        @Override
        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
            for (int i = 0; i < mCurrentRectList.size(); i++) {
                virtualViewIds.add(i);
            }
        }

        @Override
        protected void onPopulateNodeForVirtualView(int virtualViewId, @NonNull AccessibilityNodeInfoCompat node) {
            if (virtualViewId >= 0
                    && virtualViewId < mCurrentRectList.size()
                    && virtualViewId < mCurrentMonthDateList.size()) {
                RectF rect = mCurrentRectList.get(virtualViewId);
                LocalDate localDate = mCurrentMonthDateList.get(virtualViewId);
                mRectBounds.set((int) rect.left, (int) rect.top, (int) rect.right, (int) rect.bottom);
                node.setBoundsInParent(mRectBounds);
                String contentDescription = TimeDateUtil.getDateSimpleLocalFormat(localDate);
                node.setClickable(true);
                node.setContentDescription(contentDescription);
            } else {
                mRectBounds.setEmpty();
                node.setBoundsInParent(mRectBounds);
                node.setContentDescription("");
                node.setClickable(false);
            }
        }

        @Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, @Nullable Bundle arguments) {
            if (action == AccessibilityNodeInfoCompat.ACTION_CLICK
                    && virtualViewId >= 0
                    && virtualViewId < mCurrentRectList.size()
                    && virtualViewId < mCurrentMonthDateList.size()) {
                onClick(mCurrentMonthDateList.get(virtualViewId));
                return true;
            }
            return false;
        }
    }

}
