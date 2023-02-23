package com.yxc.customercomposeview.calander.view;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.yxc.customercomposeview.R;
import com.yxc.customercomposeview.common.ColorUtil;
import com.yxc.customercomposeview.utils.DisplayUtil;
import com.yxc.customercomposeview.utils.TimeDateUtil;
import com.yxc.customercomposeview.utils.ViewUtil;

import org.joda.time.LocalDate;

/**
 * @author yxc
 * @since 2019-05-27
 */
public class DataTitleBarView extends RelativeLayout {

    public ImageView imgBackWhite;
    public TextView txtDateTitle;
    public TextView txtDateDesc;
    public ImageView imgClickMore;
    public ImageView imgCalendarExpand;
    public StatusTableLayout mTabLayout;

    public StatusTextView imgCurrent;
    public StatusTextView imgMonth;
    public StatusTextView imgWeek;
    public StatusTextView imgDay;

    private Context mContext;
    private ExpandLinearLayout mCalendarContainer;
    public boolean flagExpand = false;

    public DataTitleBarView(Context context) {
        this(context, null);
    }

    public DataTitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public DataTitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_data_title_bar, this);
    }
    private OnClickMoreListener onClickMoreListener;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imgBackWhite = findViewById(R.id.img_back_white);
        txtDateTitle = findViewById(R.id.txt_date_title);
        txtDateDesc = findViewById(R.id.txt_date_desc);
        mTabLayout = findViewById(R.id.st_container);
        imgClickMore = findViewById(R.id.img_click_more);
        imgCalendarExpand = findViewById(R.id.img_calendar_expand);
        mCalendarContainer = findViewById(R.id.ll_calendar_expand);
        imgCurrent = findViewById(R.id.img_current);
        imgMonth = findViewById(R.id.img_month);
        imgWeek = findViewById(R.id.img_week);
        imgDay = findViewById(R.id.img_day);
        mCalendarContainer.initExpand(true);
        mCalendarContainer.setAnimationDuration(200);
        mCalendarContainer.setDefViewHeight(DisplayUtil.dip2px(40));
    }

    public void showCalendarExpand(boolean show) {
        imgCalendarExpand.setVisibility(show ? View.VISIBLE : View.GONE);
        mCalendarContainer.initExpand(show);
    }

    public void setTxtDescDrawableDisable() {
        txtDateDesc.setCompoundDrawables(null, null, null, null);
    }

    public void displayCalendarTitleView(LocalDate localDate, int position) {
        if (position == DataConstants.POSITION_DAY) {
            displayDayTitleView(localDate, false);
        } else if (position == DataConstants.POSITION_WEEK) {
            LocalDate monday = TimeDateUtil.getWeekMonday(localDate);
            displayWeekTitleView(monday, false);
        } else {
            displayMonthTitleView(localDate, false);
        }
    }

    private void displayDayTitleView(LocalDate localDate, boolean controlCurrentBtn) {
        String dateDescParent = TimeDateUtil.getDateYYYYMMddLocalFormat(localDate);
        setDesc(dateDescParent);
        //设置"今"是否显示
        if (controlCurrentBtn) {
            setCurrentDisable(TimeDateUtil.isSameLocalDate(localDate, LocalDate.now()));
        } else {
            setCurrentDisable(false);
        }
    }

    private void displayWeekTitleView(LocalDate localDate, boolean controlCurrentBtn) {
        setDesc(TimeDateUtil.getDateRangeFormat(localDate, localDate.plusDays(6)));

        //设置"今"是否显示
        if (controlCurrentBtn) {
            setCurrentDisable(TimeDateUtil.isSameWeekWithToday(localDate));
        } else {
            setCurrentDisable(false);
        }
    }

    private void displayMonthTitleView(LocalDate localDate, boolean controlCurrentBtn) {
        String dateDescParent = TimeDateUtil.getDateYYYYMMLocalFormat(localDate);
        setDesc(dateDescParent);
        //设置"今"是否显示
        if (controlCurrentBtn) {
            setCurrentDisable(TimeDateUtil.isSameMonth(localDate, LocalDate.now()));
        } else {
            setCurrentDisable(false);
        }
    }

    public void setBtnSelectColor() {
        int selectColor = ColorUtil.getResourcesColor(R.color.text_color);
        imgMonth.setSelectColor(selectColor);
        imgWeek.setSelectColor(selectColor);
        imgDay.setSelectColor(selectColor);
    }

    public void setTitle(String title) {
        txtDateTitle.setText(title);
    }

    public void setTitle(SpannableStringBuilder builder) {
        txtDateTitle.setText(builder.toString().trim());
    }

    public void setDesc(String desc) {
        txtDateDesc.setText(desc);
    }

    private void setCurrentDisable(boolean disable) {
        imgCurrent.setSelectType(disable ? StatusTextView.TYPE_SELECTED : StatusTextView.TYPE_UNSELECTED);
    }

    private void setListener() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onTitleBarClickListener) {
                    onTitleBarClickListener.onShowCalendar();
                }
            }
        };

        txtDateTitle.setOnClickListener(listener);
        txtDateDesc.setOnClickListener(listener);

        imgBackWhite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTitleBarClickListener != null) {
                    onTitleBarClickListener.onTitleKeyBack();
                }
            }
        });

        imgCurrent.setOnClickListener( new OnClickListener(){
            @Override
            public void onClick(View view) {
                if (imgCurrent.selectType == StatusTextView.TYPE_SELECTED) {
                    return;
                }
                if (onTitleBarClickListener != null) {
                    onTitleBarClickListener.scrollToCurrent();
                }
            }
        });

        OnClickListener clickMoreListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickMoreListener != null) {
                    onClickMoreListener.onClickMore();
                }
            }
        };

        ViewUtil.setTouchDelegate(imgClickMore, 50);
        imgClickMore.setOnClickListener(clickMoreListener);

        OnClickListener expandListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                flagExpand = !flagExpand;
                mCalendarContainer.toggleExpand();
            }
        };

        ViewUtil.setTouchDelegate(imgCalendarExpand, 50);
        imgCalendarExpand.setOnClickListener(expandListener);
    }

    public interface OnTitleBarClickListener {
        void onTitleKeyBack();
        void onShowCalendar();
        void scrollToCurrent();
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener) {
        this.onTitleBarClickListener = onTitleBarClickListener;
        setListener();
    }


    public interface  OnClickMoreListener{
        void onClickMore();
    }
    private OnTitleBarClickListener onTitleBarClickListener;

}
