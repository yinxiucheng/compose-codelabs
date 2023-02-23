package com.yxc.customercomposeview.calander.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


/**
 * @author yxc
 * @since 2019-05-07
 */
public class StatusTableLayout extends LinearLayout {

    public int selectedPosition = -1;

    public StatusTableLayout(Context context) {
        super(context);
    }

    public StatusTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusTableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof StatusTextView) {
                StatusTextView statusView = (StatusTextView) childView;
                if (mSelectorColor != -1) {
                    statusView.selectColor = mSelectorColor;
                }
                if (selectedPosition == -1) {//xml中设定的
                    if (statusView.selectType == StatusTextView.TYPE_SELECTED) {
                        selectedPosition = i;
                    }
                } else {//外部传来的，动态代码改变
                    if (selectedPosition == i) {
                        statusView.setSelectType(StatusTextView.TYPE_SELECTED);
                    } else {
                        statusView.setSelectType(StatusTextView.TYPE_UNSELECTED);
                    }
                }
            }
        }

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof StatusTextView) {
                StatusTextView statusView = (StatusTextView) childView;
                if (i != selectedPosition) {
                    statusView.setSelectType(StatusTextView.TYPE_UNSELECTED);
                }
                final int position = i;
                statusView.setOnClickListener(v -> {
                    if (statusView.selectType == StatusTextView.TYPE_UNSELECTED) {
                        statusView.setSelectType(StatusTextView.TYPE_SELECTED);
                        StatusTextView lastSelectedView = (StatusTextView) getChildAt(selectedPosition);
                        lastSelectedView.setSelectType(StatusTextView.TYPE_UNSELECTED);
                        selectedPosition = position;
                        if (onTableSelectListener != null) {
                            onTableSelectListener.onTabSelect(position);
                        }
                    } else {
                        if (onTableSelectListener != null) {
                            onTableSelectListener.onTabReselect(position);
                        }
                    }
                });
            }
        }
    }

    public int mSelectorColor = -1;

    public void setSelectColor(int selectColor) {
        this.mSelectorColor = selectColor;
        invalidate();
    }

    public void setCurrentTab(int position) {
        selectedPosition = position;
        requestLayout();
//        invalidate();
    }


    public void setOnTabSelectListener(OnTabSelectListener onTableSelectListener) {
        this.onTableSelectListener = onTableSelectListener;
    }

    OnTabSelectListener onTableSelectListener;

    public interface OnTabSelectListener {
        void onTabSelect(int position);
        void onTabReselect(int position);
    }


}

