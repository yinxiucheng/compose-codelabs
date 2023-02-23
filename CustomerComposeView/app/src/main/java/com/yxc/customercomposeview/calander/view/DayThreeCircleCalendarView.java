package com.yxc.customercomposeview.calander.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.AttributeSet;
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

import com.yxc.customercomposeview.calander.model.ThreeCircleModel;
import com.yxc.customercomposeview.calander.painter.CalendarPainter;
import com.yxc.customercomposeview.calander.painter.InnerThreeCirclePainter;
import com.yxc.customercomposeview.calander.utils.CalendarAttrs;
import com.yxc.customercomposeview.calander.utils.CalendarAttrsUtil;
import com.yxc.customercomposeview.utils.AppUtil;
import com.yxc.customercomposeview.utils.DisplayUtil;
import com.yxc.customercomposeview.utils.TimeDateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DayThreeCircleCalendarView extends View {
    protected static final float ITEM_HEIGHT_WIDTH_RATIO = 1.5f;
    protected LocalDate mInitialDate;//由mInitialDate和周开始的第一天 算出当前页面的数据
    protected List<RectF> mRectList;//点击用的矩形集合
    protected List<LocalDate> mDateList;//页面的数据集合

    protected List<RectF> mCurrentRectList;//本月Rect集合
    protected List<LocalDate> mCurrentMonthDateList;//本月日期集合

    protected int mLineNum;//行数
    protected LocalDate mSelectDate;//点击选中的日期
    protected CalendarAttrs mAttrs;
    protected CalendarPainter mCalendarPainter;
    protected @Nullable
    Map<Long, ThreeCircleModel> threeCircleModelMap;
    protected OnDayCalendarItemSelectListener mWeekCalendarItemSelectListener;
    protected ThreeCircleModel userInfoCircleModel;

    private CalendarViewTouchHelper mTouchHelper;

    protected GestureDetector mGestureDetector = new GestureDetector(getContext(),
            new GestureDetector.SimpleOnGestureListener() {
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
                            if (!isSameDay(localDate)) {
                                // 点击事件去掉
                                onClick(localDate);
                            }
                            break;
                        }
                    }
                    return true;
                }
            });

    public DayThreeCircleCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = CalendarAttrsUtil.getAttrs(context, attrs);
        LocalDate localDate = LocalDate.now();

        this.mInitialDate = this.mSelectDate = localDate;
        mRectList = new ArrayList<>();

        mCurrentRectList = new ArrayList<>();
        mCurrentMonthDateList = new ArrayList<>();

        mDateList = TimeDateUtil.getMonthLocalDateCalendar(localDate);
        mLineNum = mDateList.size() / 7; //天数/7
        this.mCalendarPainter = new InnerThreeCirclePainter(mAttrs);
        mTouchHelper = new CalendarViewTouchHelper(this);
        ViewCompat.setAccessibilityDelegate(this, mTouchHelper);
    }

    public void setLocalData(LocalDate localDate,
                             @Nullable Map<Long, ThreeCircleModel> modelMap,
                             ThreeCircleModel threeCircleModel) {
        this.mInitialDate = localDate;
        this.threeCircleModelMap = modelMap;
        this.userInfoCircleModel = threeCircleModel;
        this.mDateList = TimeDateUtil.getMonthLocalDateCalendar(localDate);
        mLineNum = mDateList.size() / 7; //天数/7
        invalidate();
    }

    public void setSelectDate(LocalDate selectDate) {
        this.mSelectDate = selectDate;
        invalidate();
    }

    public CalendarPainter getCalendarPainter() {
        return mCalendarPainter;
    }

    public void setSelectDateInvalidate(LocalDate selectDate) {
        if (isSameDay(selectDate)) {
            return;
        }
        onClick(selectDate);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制时获取区间开始结束日期和绘制类Painter
        CalendarPainter painter = getCalendarPainter();
        mRectList.clear();
        for (int i = 0; i < mLineNum; i++) {
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
                } else if (TimeDateUtil.isToday(date)) {
                    painter.onDrawToday(canvas, rect, date, date.equals(mSelectDate));
                } else {
                    painter.onDrawCurrentMonthOrWeek(canvas, rect, date, date.equals(mSelectDate));
                }
                if (threeCircleModelMap != null) {
                    ThreeCircleModel model = threeCircleModelMap.get(date.toDate().getTime() / DateUtils.SECOND_IN_MILLIS);
                    if (model == null) {
                        model = userInfoCircleModel;
                    }
                    painter.onDrawThreeCircle(canvas, rect, model);
                }
            }
        }
    }

    //获取每个元素矩形
    private RectF getRect(int i, int j) {
        int width = getMeasuredWidth();
//        int height = getMeasuredHeight();
        int viewTop = DisplayUtil.dip2px(10);
        RectF rect;
        //5行的月份，5行矩形平分view的高度
        int itemCircleWidth = (int) (mAttrs.selectCircleRadius * 2);
        float itemHeight = itemCircleWidth * 1.5f;
        int spaceWidth = (width - itemCircleWidth * 7) / 8;
        int startLTR = spaceWidth * (j + 1) + j * itemCircleWidth;
        float top = viewTop + i * itemHeight;
        float bottom = top + itemHeight;
        if (AppUtil.isRTLDirection()) {
            int end = getRight() - startLTR;
            int start = end - (spaceWidth + itemCircleWidth);
            rect = new RectF(start, top, end, bottom);
            return rect;
        } else {
            int start = startLTR;
            int end = (j + 1) * (spaceWidth + itemCircleWidth);
            rect = new RectF(start, top, end, bottom);
            return rect;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private boolean isSameDay(LocalDate localDate) {
        return localDate.getMonthOfYear() == mSelectDate.getMonthOfYear()
                && localDate.getDayOfMonth() == mSelectDate.getDayOfMonth();
    }

    /**
     * 点击事件
     *
     * @param localDate 当前点击的页面
     */
    protected void onClick(final LocalDate localDate) {
        if (!TimeDateUtil.isSameMonth(mInitialDate, localDate) && !mAttrs.isShowOtherMonthDate) {
            return;
        }
        if (TimeDateUtil.isLastMonth(mSelectDate, localDate)) {
            mSelectDate = localDate;
            animatorChange(localDate, 0, -DisplayUtil.getScreenWidth(getContext()), 300);
        } else if (!TimeDateUtil.isAfterToday(localDate)) {//不是未来，有数据
            if (TimeDateUtil.isNextMonth(localDate, mSelectDate)) {//下一个月
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
                            DayThreeCircleCalendarView.this, "translationX", end, begin);
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

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    //需要重新计算数据，用来刷新页面。
    private void calculateData(LocalDate localDate) {
        mDateList = TimeDateUtil.getMonthLocalDateCalendar(localDate);
        mLineNum = mDateList.size() / 7;
    }

    private void invalidateViewAndData(LocalDate localDate) {
        if (null != mWeekCalendarItemSelectListener) {
            mWeekCalendarItemSelectListener.onDayItemSelect(localDate);
        }
        invalidate();
    }

    public void setOnDayCalendarItemSelectListener(OnDayCalendarItemSelectListener listener) {
        this.mWeekCalendarItemSelectListener = listener;
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        if (mTouchHelper.dispatchHoverEvent(event)){
            return true;
        }
        return super.dispatchHoverEvent(event);
    }

    private class CalendarViewTouchHelper extends ExploreByTouchHelper {

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
