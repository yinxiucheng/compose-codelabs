package com.yxc.customercomposeview.calander.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.yxc.customercomposeview.R;
import com.yxc.customercomposeview.calander.utils.CalendarAttrs;
import com.yxc.customercomposeview.calander.utils.CalendarAttrsUtil;
import com.yxc.customercomposeview.utils.AppUtil;
import com.yxc.customercomposeview.utils.TextUtil;


public class WeekBar extends AppCompatTextView {


    public String[] days;

    private int type;//一周的第一天是周几
    private Paint textPaint;
    private CalendarAttrs mAttrs;

    public WeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NCalendar);
        type = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, CalendarAttrs.SUNDAY);
        this.mAttrs = CalendarAttrsUtil.getAttrs(context, attrs);
        ta.recycle();

        days = context.getResources().getStringArray(R.array.date_week_simple);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(mAttrs.weekBarTextSize);
        textPaint.setColor(mAttrs.weekBarTextColor);

        if (mAttrs.calendarTxtFontRes != -1){
            TextUtil.setTypeface(textPaint, mAttrs.calendarTxtFontRes);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingTop = getPaddingTop();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int itemCircleWidth = (int) mAttrs.selectCircleRadius * 2;
        int spaceWidth = (width - itemCircleWidth * 7) / 8;
        for (int i = 0; i < days.length; i++) {
            int start = (i + 1) * spaceWidth + i * itemCircleWidth;
            int end = (i + 1) * (spaceWidth + itemCircleWidth);
            if (AppUtil.isRTLDirection()){
                end = getRight() - ((i + 1) * spaceWidth + i * itemCircleWidth);
                start = end - (spaceWidth + itemCircleWidth);
            }
            Rect rect = new Rect(start, paddingTop, end, paddingTop + height);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float top = fontMetrics.top;
            float bottom = fontMetrics.bottom;
            int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
            String day;
            if (type == CalendarAttrs.MONDAY) {
                int j = i + 1;
                day = days[j > days.length - 1 ? 0 : j];
            } else {
                day = days[i];
            }
            canvas.drawText(day, rect.centerX(), baseLineY, textPaint);
        }
    }
}
