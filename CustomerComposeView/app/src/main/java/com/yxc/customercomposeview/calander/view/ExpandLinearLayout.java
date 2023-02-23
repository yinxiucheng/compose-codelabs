package com.yxc.customercomposeview.calander.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * @author yxc
 * @date 2020-01-14
 */
public class ExpandLinearLayout extends LinearLayout {

    public ExpandLinearLayout(Context context) {
        this(context, null);
    }

    public ExpandLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private View layoutView;
    private int viewHeight;
    private boolean isExpand;
    private long animationDuration;

    private void initView() {
        layoutView = this;
        isExpand = true;
        animationDuration = 300;
        setViewDimensions();
    }

    /**
     * @param isExpand 初始状态是否折叠
     */
    public void initExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {
            animateToggle(100);
        }
    }

    /**
     * 设置动画时间
     *
     * @param animationDuration 动画时间
     */
    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    public void setDefViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    /**
     * 获取 subView 的总高度
     * View.post() 的 runnable 对象中的方法会在 View 的 measure、layout 等事件后触发
     */
    private void setViewDimensions() {
        layoutView.post(new Runnable() {
            @Override
            public void run() {
                if (viewHeight <= 0) {
                    viewHeight = layoutView.getMeasuredHeight();
                }
            }
        });
    }

    public void setViewHeight(View view, int height) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
        view.requestLayout();
//        if (height > 0){
//            return;
//        }
        if (!isExpand && height <= 20) {
            setVisibility(GONE);
        } else if (height >= 20) {
            setVisibility(VISIBLE);
        }
    }

    /**
     * 切换动画实现
     */
    private void animateToggle(long animationDuration) {
        ValueAnimator heightAnimation = isExpand ? ValueAnimator.ofFloat(0f, viewHeight) : ValueAnimator.ofFloat(viewHeight, 0f);
        heightAnimation.setDuration(animationDuration / 2);
        heightAnimation.setStartDelay(animationDuration / 2);
        heightAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        heightAnimation.addUpdateListener(animation -> {
            float val = (float) animation.getAnimatedValue();
            int valInt = (int) val;
            setViewHeight(layoutView, valInt);
        });
        heightAnimation.start();
    }

    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 折叠view
     */
    public void collapse() {
        isExpand = false;
        animateToggle(animationDuration);
    }

    /**
     * 展开view
     */
    public void expand() {
        isExpand = true;
        animateToggle(animationDuration);
    }

    public void toggleExpand() {
        if (isExpand) {
            collapse();
        } else {
            expand();
        }
    }

}
