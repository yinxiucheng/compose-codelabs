package com.yxc.customercomposeview.calander.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.customercomposeview.R;
import com.yxc.customercomposeview.common.ColorUtil;
import com.yxc.customercomposeview.utils.DisplayUtil;


/**
 * @author yxc
 * @since 2019-05-07
 */
public class StatusTextView extends View {

    public static final int TYPE_SELECTED = 1;
    public static final int TYPE_UNSELECTED = 0;

    public int selectType = TYPE_UNSELECTED;
    public int selectColor;
    public int unSelectColor;

    public int selectImg;
    public int unSelectImg;

    private Context mContext;
    public String mText;

    private Paint textPaint;

    public StatusTextView(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public StatusTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public StatusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StatusTextView);
        selectType = ta.getInteger(R.styleable.StatusTextView_status_textView_style, 0);
        selectColor = ta.getColor(R.styleable.StatusTextView_status_select_textColor, ColorUtil.getResourcesColor(R.color.text_color));
        unSelectColor = ta.getColor(R.styleable.StatusTextView_status_unSelect_textColor, ColorUtil.getResourcesColor(R.color.black_60));
        selectImg = ta.getResourceId(R.styleable.StatusTextView_status_select_img, R.drawable.data_current_select);
        unSelectImg = ta.getResourceId(R.styleable.StatusTextView_status_unSelect_img, R.drawable.data_current_unselect);
        mText = ta.getString(R.styleable.StatusTextView_status_txt);
        ta.recycle();
        initPaint();
    }

    private void initPaint() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(DisplayUtil.dip2px(12));
        textPaint.setColor(selectType == TYPE_SELECTED ? selectColor : unSelectColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap drawBitmap = null;
        if (selectType == TYPE_SELECTED) {
            drawBitmap = BitmapFactory.decodeResource(getResources(), selectImg);
            textPaint.setColor(selectColor);
        } else {
            drawBitmap = BitmapFactory.decodeResource(getResources(), unSelectImg);
            textPaint.setColor(unSelectColor);
        }
        int imgStart = (getWidth() - drawBitmap.getWidth()) / 2;
        canvas.drawBitmap(drawBitmap, imgStart, 0, textPaint);

        if (!TextUtils.isEmpty(mText)){
            float txtStart = getWidth() / 2;
            float txtTop = drawBitmap.getHeight() + DisplayUtil.dip2px(12);
            canvas.drawText(mText, txtStart, txtTop, textPaint);
        }
    }

    public void setSelectColor(int color) {
        this.selectColor = color;
        invalidate();
    }

    public void setSelectType(int selectType) {
        this.selectType = selectType;
        invalidate();
    }
}
