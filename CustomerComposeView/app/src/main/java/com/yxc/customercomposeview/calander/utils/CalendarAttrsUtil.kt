package com.yxc.customercomposeview.calander.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.utils.ColorUtil
import com.yxc.customercomposeview.utils.dpf
import com.yxc.customercomposeview.utils.spf

object CalendarAttrsUtil {
    @JvmStatic
    fun getAttrs(context: Context, attributeSet: AttributeSet?): CalendarAttrs {
        val attrs = CalendarAttrs()
        val ta: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.NCalendar)
        val textColor = ColorUtil.getColor(context, R.color.text_color)
        attrs.solarTextColor = ta.getColor(R.styleable.NCalendar_solarTextColor, textColor)
        attrs.solarSelectTextColor = ta.getColor(
            R.styleable.NCalendar_solarSelectTextColor,
            textColor
        )
        attrs.todaySolarTextColor = ta.getColor(
            R.styleable.NCalendar_todaySolarTextColor,
            ColorUtil.getColor(context,R.color.todaySolarTextColor)
        )
        attrs.todaySolarSelectTextColor = ta.getColor(
            R.styleable.NCalendar_todaySolarSelectTextColor,
            ColorUtil.getColor(context, R.color.calendar_today_selector)
        )
        attrs.solarTermTextColor = ta.getColor(
            R.styleable.NCalendar_solarTermTextColor,
            ColorUtil.getColor(context, R.color.solarTermTextColor)
        )
        attrs.selectCircleColor = ta.getColor(
            R.styleable.NCalendar_selectCircleColor,
            ColorUtil.getColor(context, R.color.selectCircleColor)
        )
        attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, 18.dpf)
        attrs.selectCircleRadius = ta.getDimension(R.styleable.NCalendar_selectCircleRadius, 22.dpf)
        attrs.isDefaultSelect = ta.getBoolean(R.styleable.NCalendar_isDefaultSelect, true)
        attrs.isShowOtherMonthDate = ta.getBoolean(R.styleable.NCalendar_isShowOtherMonthDate, true)
        attrs.pointSize =
            ta.getDimension(R.styleable.NCalendar_pointSize, 2.dpf)
        attrs.pointDistance =
            ta.getDimension(R.styleable.NCalendar_pointDistance, 18.dpf)
        attrs.pointColor = ta.getColor(
            R.styleable.NCalendar_pointColor,
            ColorUtil.getColor(context, R.color.pointColor)
        )
        attrs.todayWeekBgColor = ta.getColor(
            R.styleable.NCalendar_todayWeekBgColor,
            ColorUtil.getColor(context, R.color.todayWeekBgColor)
        )
        attrs.todayBgColor = ta.getColor(
            R.styleable.NCalendar_todayBgColor,
            ColorUtil.getColor(context, R.color.common_transparent)
        )
        attrs.hollowCircleStroke =
            ta.getDimension(R.styleable.NCalendar_hollowCircleStroke, 2.dpf)
        attrs.monthCalendarHeight =
            ta.getDimension(R.styleable.NCalendar_calendarHeight, 300.dpf)
                .toInt()
        attrs.duration = ta.getInt(R.styleable.NCalendar_duration, 240)
        attrs.isShowHoliday = ta.getBoolean(R.styleable.NCalendar_isShowHoliday, false)
        attrs.isWeekHold = ta.getBoolean(R.styleable.NCalendar_isWeekHold, false)
        attrs.holidayColor = ta.getColor(
            R.styleable.NCalendar_holidayColor,
            ColorUtil.getColor(context, R.color.holidayColor)
        )
        attrs.workdayColor = ta.getColor(
            R.styleable.NCalendar_workdayColor,
            ColorUtil.getColor(context, R.color.workdayColor)
        )
        attrs.bgCalendarColor = ta.getColor(
            R.styleable.NCalendar_bgCalendarColor,
            ColorUtil.getColor(context, R.color.common_transparent)
        )
        attrs.bgChildColor = ta.getColor(
            R.styleable.NCalendar_bgChildColor,
            ColorUtil.getColor(context, R.color.white)
        )
        attrs.firstDayOfWeek = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, CalendarAttrs.SUNDAY)
        attrs.pointLocation = ta.getInt(R.styleable.NCalendar_pointLocation, CalendarAttrs.UP)
        attrs.defaultCalendar =
            ta.getInt(R.styleable.NCalendar_defaultCalendar, CalendarAttrs.MONTH)
        attrs.showAnimator = ta.getBoolean(R.styleable.NCalendar_showAnimator, false)
        attrs.holidayLocation =
            ta.getInt(R.styleable.NCalendar_holidayLocation, CalendarAttrs.TOP_RIGHT)
        attrs.lunarTextColor = ta.getColor(
            R.styleable.NCalendar_lunarTextColor,
            ColorUtil.getColor(context, R.color.lunarTextColor)
        )
        attrs.lunarHolidayTextColor = ta.getColor(
            R.styleable.NCalendar_lunarHolidayTextColor,
            ColorUtil.getColor(context, R.color.lunarHolidayTextColor)
        )
        attrs.solarHolidayTextColor = ta.getColor(
            R.styleable.NCalendar_solarHolidayTextColor,
            ColorUtil.getColor(context, R.color.solarHolidayTextColor)
        )
        attrs.lunarTextSize =
            ta.getDimension(R.styleable.NCalendar_lunarTextSize, 10.spf)
        attrs.lunarDistance =
            ta.getDimension(R.styleable.NCalendar_lunarDistance, 15.spf)
        attrs.isShowLunar = ta.getBoolean(R.styleable.NCalendar_isShowLunar, false)
        attrs.holidayDistance =
            ta.getDimension(R.styleable.NCalendar_holidayDistance, 15.spf)
        attrs.holidayTextSize =
            ta.getDimension(R.styleable.NCalendar_holidayTextSize, 10.spf)
        attrs.hollowCircleColor = ta.getColor(
            R.styleable.NCalendar_hollowCircleColor,
            ColorUtil.getColor(context, R.color.hollowCircleColor)
        )
        attrs.alphaColor = ta.getInt(R.styleable.NCalendar_alphaColor, 90)
        attrs.disabledAlphaColor = ta.getInt(R.styleable.NCalendar_disabledAlphaColor, 50)
        attrs.weekBarTextColor = ta.getColor(R.styleable.NCalendar_weekBarTextColor, Color.GRAY)
        attrs.weekBarTextSize =
            ta.getDimension(R.styleable.NCalendar_weekBarTextSize, 12.spf)
        attrs.monthTextSize =
            ta.getDimension(R.styleable.NCalendar_monthTextSize, 23.spf)
        attrs.monthUnitTextSize =
            ta.getDimension(R.styleable.NCalendar_monthUnitTextSize, 20.spf)
        val startString: String? = ta.getString(R.styleable.NCalendar_startDate)
        val endString: String? = ta.getString(R.styleable.NCalendar_endDate)
        attrs.disabledString = ta.getString(R.styleable.NCalendar_disabledString)
        attrs.startDateString = if (TextUtils.isEmpty(startString)) "1901-01-01" else startString
        attrs.endDateString = if (TextUtils.isEmpty(endString)) "2099-12-31" else endString
        attrs.calendarTxtFontRes = ta.getResourceId(R.styleable.NCalendar_calendarTxtFontRes, -1)
        ta.recycle()
        return attrs
    }

}