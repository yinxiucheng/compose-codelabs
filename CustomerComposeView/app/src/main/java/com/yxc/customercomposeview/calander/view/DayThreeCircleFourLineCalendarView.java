package com.yxc.customercomposeview.calander.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class DayThreeCircleFourLineCalendarView extends DayThreeCircleCalendarView {

    public DayThreeCircleFourLineCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        //5行的月份，5行矩形平分view的高度
        int itemCircleWidth = (int) (mAttrs.selectCircleRadius * 2);
        int height = (int)(4 * (itemCircleWidth * ITEM_HEIGHT_WIDTH_RATIO));
        setMeasuredDimension(width, height);
    }

}
