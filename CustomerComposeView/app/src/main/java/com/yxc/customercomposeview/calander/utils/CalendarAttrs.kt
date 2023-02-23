package com.yxc.customercomposeview.calander.utils

class CalendarAttrs {
    @JvmField
    var solarTextColor = 0
    @JvmField
    var solarSelectTextColor = 0
    @JvmField
    var todaySolarTextColor = 0
    @JvmField
    var todaySolarSelectTextColor = 0
    @JvmField
    var lunarTextColor = 0
    @JvmField
    var solarHolidayTextColor = 0
    @JvmField
    var lunarHolidayTextColor = 0
    @JvmField
    var solarTermTextColor = 0
    @JvmField
    var selectCircleColor = 0
    @JvmField
    var solarTextSize = 0f
    @JvmField
    var lunarTextSize = 0f
    @JvmField
    var lunarDistance //农历到文字中心的距离
            = 0f
    @JvmField
    var selectCircleRadius = 0f
    @JvmField
    var isShowLunar = false
    @JvmField
    var isShowOtherMonthDate //是否显示其他月的日期
            = false
    @JvmField
    var pointSize = 0f
    @JvmField
    var pointDistance //圆点到文字中心的距离
            = 0f
    @JvmField
    var pointColor = 0
    @JvmField
    var pointLocation //0 在上面 1在下面
            = 0
    @JvmField
    var hollowCircleColor = 0
    @JvmField
    var todayWeekBgColor //当天所在Week的背景颜色
            = 0
    @JvmField
    var todayBgColor //当天背景颜色
            = 0
    @JvmField
    var hollowCircleStroke = 0f
    @JvmField
    var firstDayOfWeek = 0
    @JvmField
    var defaultCalendar = 0
    @JvmField
    var monthCalendarHeight = 0
    @JvmField
    var duration = 0
    @JvmField
    var isWeekHold = false
    @JvmField
    var isShowHoliday = false
    @JvmField
    var holidayColor = 0
    @JvmField
    var holidayTextSize = 0f
    @JvmField
    var holidayDistance = 0f
    @JvmField
    var holidayLocation = 0
    @JvmField
    var workdayColor = 0
    @JvmField
    var bgCalendarColor //日历的背景
            = 0
    @JvmField
    var bgChildColor //子view的背景
            = 0
    @JvmField
    var isDefaultSelect //是否默认选中
            = false
    @JvmField
    var startDateString: String? = null
    @JvmField
    var endDateString: String? = null
    @JvmField
    var alphaColor //不在同一月的颜色透明度
            = 0
    @JvmField
    var disabledAlphaColor //不可用的日期颜色透明度
            = 0
    @JvmField
    var disabledString //点击不可用的日期提示语
            : String? = null
    @JvmField
    var showAnimator //是否显示切换时候的动画。
            = false
    @JvmField
    var monthTextSize = 0f
    @JvmField
    var monthUnitTextSize = 0f
    @JvmField
    var weekBarTextColor //weekBar 字的颜色
            = 0
    @JvmField
    var weekBarTextSize //weekBar 字的大小
            = 0f
    @JvmField
    var calendarTxtFontRes //字体 资源。
            = 0

    companion object {
        //日历默认视图
        const val WEEK = 100 //周视图
        const val MONTH = 101 //月视图

        //指示圆点的位置
        const val UP = 200 //再公历日期上面
        const val DOWN = 201 //再公历日期下面

        //周的第一天
        const val SUNDAY = 300 //周的第一天 周日
        const val MONDAY = 301 //周的第一天 周一

        //节假日的位置
        const val TOP_RIGHT = 400 //右上方
        const val TOP_LEFT = 401 //左上方
        const val BOTTOM_RIGHT = 402 //右下方
        const val BOTTOM_LEFT = 403 //左下方
    }
}