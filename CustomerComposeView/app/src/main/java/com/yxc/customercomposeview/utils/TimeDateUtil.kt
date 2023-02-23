package com.yxc.customercomposeview.utils

import android.content.Context
import android.text.TextUtils
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.yxc.customercomposeview.R
import com.yxc.customercomposeview.utils.AppUtil.app
import org.joda.time.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * @author yxc
 * @since 2019/4/7
 */
object TimeDateUtil {
    const val TIME_DAY = (24 * 60 * 60).toLong()
    const val TIME_HOUR = (60 * 60).toLong()
    const val TIME_HOUR_INT = 60 * 60
    const val TIME_MIN_INT = 60
    const val TIME_HOUR_MIN = 60 //一个小时60分钟
    const val NUM_DAY_OF_WEEK = 7
    const val NUM_HOUR_OF_DAY = 24
    const val NUM_MONTH_OF_YEAR = 12

    //周的第一天
    //    private static final int SUNDAY = 300;//周的第一天 周日
    private const val MONDAY = 301 //周的第一天 周一

    fun getDurationStr(mills: Long): String {
        val second = mills / 1000 % 60
        val minute = mills / (1000 * 60) % 60
        val hour = mills / (1000 * 60 * 60) % 24
        return if (hour > 0) String.format("%02d:%02d:%02d", hour, minute, second) else String.format("%02d:%02d", minute, second)
    }

    /**
     * 根据 patternStr显示不同的 时间样式
     *
     * @param timestamp
     * @param patternStr
     */
    @JvmStatic
    fun getDateStr(timestamp: Long, patternStr: String?): String {
        return getDateStr(timestamp, patternStr, Locale.getDefault())
    }

    @JvmStatic
    fun getDateStr(timeZone: TimeZone?, patternStr: String?): String {
        val simpleDateFormat = SimpleDateFormat(patternStr)
        val date = Date()
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.format(date)
    }

    @JvmStatic
    fun getDateStr(timestamp: Long, patternStr: String?, locale: Locale?): String {
        val simpleDateFormat = SimpleDateFormat(patternStr, locale)
        val date = Date(timestamp * 1000)
        return simpleDateFormat.format(date)
    }

    @JvmStatic
    fun getDateStr(timestamp: Long, patternStr: String?, timeZone: TimeZone?): String {
        val simpleDateFormat = SimpleDateFormat(patternStr, Locale.getDefault())
        val date = Date(timestamp * 1000)
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.format(date)
    }

    /**
     * 根据 patternStr显示不同的 时间样式
     *
     * @param localDate
     * @param patternStr
     */
    @JvmStatic
    fun getDateStr(localDate: LocalDate, patternStr: String?): String {
        return getDateStr(localDateToTimestamp(localDate), patternStr)
    }

    /**
     * 显示年份，英文中显示 2019、2020这样的样式
     * 中文、香港、台湾显示 2019年、2020年等
     *
     * @param localDate
     */
    @JvmStatic
    fun getDateYYYYFormat(localDate: LocalDate): String {
        //获取不同国家的样式
        val formatStr = DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyy")
        return localDate.toString(formatStr)
    }

    @JvmStatic
    fun getDateYYYYFormat(mills: Long): String {
        return getDateYYYYFormat(LocalDate(mills))
    }

    /**
     * 显示日本地时间 历中的日的格式
     * 英文中显示 1st、2nd、3rd、4th....11th、12th、13th、14th....21st、22nd、23rd、24th...31st
     * 中文、香港、台湾显示 1日、2日、3日...31日
     *
     * @param localDate
     */
    @JvmStatic
    fun getDayFormat(localDate: LocalDate): String {
        return if (isLanguageEn) {
            //todo 英文要添加th
            val day = localDate.dayOfMonth
            day.toString() + getDayOfMonthSuffix(day)
        } else {
            val formatPattern = getDayFormatPetternStr("d")
            localDate.toString(formatPattern)
        }
    }

    @JvmStatic
    fun getDayFormatPetternStr(skeleton: String?): String {
        return DateFormat.getBestDateTimePattern(Locale.getDefault(), skeleton)
    }

    /**
     * 判断Language 是否是 en
     */
    @JvmStatic
    val isLanguageEn: Boolean
        get() {
            val context: Context = app
            val locale = context.resources.configuration.locale
            val language = locale.language.toLowerCase()
            return language.contains("en")
        }

    /**
     * 显示本地时间 年/月/日 日期格式
     * 英文 eg： February, 26th 2020
     * 中文、香港、台湾 eg：  2020年2月26日
     *
     * @param localDate
     * @return
     */
    @JvmStatic
    fun getDateYYYYMMddLocalFormat(localDate: LocalDate?): String {
        val context: Context = app
        return if (localDate != null){
            DateUtils.formatDateTime(context,
                localDateToTimestampInner(localDate),
                DateUtils.FORMAT_SHOW_YEAR
            )
        }else {
            ""
        }
    }

    @JvmStatic
    fun getDateYYYYMMddLocalFormat(millis: Long): String {
        return getDateYYYYMMddLocalFormat(LocalDate(millis))
    }

    fun getDateMMDDLocalFormat(mills: Long): String {
        val context: Context = app
        return DateUtils.formatDateTime(context, mills,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_NO_YEAR
        )
    }

    @JvmStatic
    fun getDateYYYYMMdd(millis: Long): String {
        return getDateYYYYMMdd(millis, "yyyy-MM-dd")
    }

    @JvmStatic
    fun getDateYYYYMMdd(millis: Long, format: String = "yyyy-MM-dd"): String {
        val format = SimpleDateFormat(format, Locale.US)
        return format.format(Date(millis))
    }

    @JvmStatic
    fun getDateMdFormat(millis: Long): String {
        return getDateFormated(
            millis,
            app.getString(R.string.date_pattern_mm_dd_simple)
        )
    }

    @JvmStatic
    fun getDateFormated(millis: Long, formatStr: String?): String {
        val format = SimpleDateFormat(formatStr, Locale.US)
        return format.format(millis)
    }

    /**
     * 显示本地时间 年/月 日期格式
     * 英文 eg： February 2020
     * 中文、香港、台湾 eg：  2020年2月
     *
     * @param localDate
     */
    @JvmStatic
    fun getDateYYYYMMLocalFormat(localDate: LocalDate?): String {
        val context: Context = app
        localDate?.let { return DateUtils.formatDateTime(
            context,
            localDateToTimestampInner(it),
            DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NO_MONTH_DAY
        ) }
        return ""
    }

    /**
     * 显示本地时间 月/日 日期格式
     * 英文 eg： February, 26
     * 中文、香港、台湾 eg：  2月26日
     *
     * @param localDate
     */
    @JvmStatic
    fun getDateMMddFormat(localDate: LocalDate): String {
//        String formatPattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMMd");
//        return localDate.toString(formatPattern);
        val context: Context = app
        return DateUtils.formatDateTime(
            context, localDateToTimestampInner(localDate),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR
        )
    }

    @JvmStatic
    fun getDateMMddFormat(time: Long): String {
        val localDate = timestampToLocalDate(time)
        return getDateMMddFormat(localDate)
    }

    @JvmStatic
    fun getTimeStrWithTimeZone(
        time: Long,
        formatPattern: String?,
        dateTimeZone: DateTimeZone?
    ): String {
        val localDateTime = LocalDateTime(time, dateTimeZone)
        return localDateTime.toString(formatPattern)
    }

    @JvmStatic
    val dateMMDDFormatPattern: String
        get() = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMMd")

    /**
     * 显示本地时间 月/日 日期格式 显示"MM/dd"样式的月日
     * 英文 eg: 2/26
     * 中文、香港、台湾 eg： 2/26
     *
     * @param localDate
     */
    @JvmStatic
    fun getDateMMddNumberFormat(localDate: LocalDate): String {
        val context: Context = app
        return DateUtils.formatDateTime(
            context, localDateToTimestampInner(localDate),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_NUMERIC_DATE
        )
    }

    @JvmStatic
    fun getDateMMddNumberFormat(mills: Long): String {
        return getDateMMddNumberFormat(LocalDate(mills))
    }

    /**
     * 显示本地时间 月 日 日期简写格式
     * 英文 eg： Feb 26,日期不带后缀,英文显示月日时要用空格隔开
     * 中文、香港、台湾 eg：  2月26日 2月9日
     *
     * @param localDate
     */
    @JvmStatic
    fun getDateMMddSimpleFormat(localDate: LocalDate): String {
        val context: Context = app
        return getDateStr(localDate, context.getString(R.string.date_pattern_mm_dd_simple))
    }

    /**
     *
     * @param mills  毫秒
     * @return
     */
    @JvmStatic
    fun getDateMMddSimpleFormatMills(mills: Long): String {
        return getDateMMddSimpleFormat(LocalDate(mills))
    }

    /**
     *
     * @param seconds 秒
     * @return
     */
    @JvmStatic
    fun getDateMMddSimpleFormat(seconds: Long): String {
        return getDateMMddSimpleFormat(LocalDate(seconds * 1000))
    }

    /**
     * 显示本地时间 月 日 周的简写格式
     * 星期天、日期放到一行显示时,按照国际标准，顺序会不一样。
     * 英文 eg： Feb 26,日期不带后缀,月日用空格隔开
     * 中文、香港、台湾 eg：  3月9日 周一、3月10日 周二、Tue,Mar 10
     *
     * @param localDate
     */
    @JvmStatic
    fun getDateMMddAndWeekSimpleFormat(localDate: LocalDate): String {
        val context: Context = app
        return getDateStr(localDate, context.getString(R.string.date_pattern_mm_dd_week_simple))
    }

    /**
     * 显示本地时间 月的 日期格式 简写
     * 英文 eg： Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec
     * 中文、香港、台湾 eg：  01月 2月...12月
     *
     * @param localDate
     */
    @JvmStatic
    fun getDateMMSimpleFormat(localDate: LocalDate): String {
        return getDateStr(localDate, "MMM")
    }

    @JvmStatic
    fun getDateMMSimpleFormat(mills: Long): String {
        return getDateMMSimpleFormat(LocalDate(mills))
    }

    /**
     * 显示本地时间 月的日期格式
     * 英文 eg： January February March April May June July August September October November December
     * 中文、香港、台湾 eg：  1月 2月...12月
     *
     * @param localDate
     */
    @JvmStatic
    fun getDateMMFormat(localDate: LocalDate): String {
        val context: Context = app
        return if (isLanguageEn) {
            getDateStr(localDate, "MMMM")
        } else {
            DateUtils.formatDateTime(context, localDateToTimestampInner(localDate), DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_NO_MONTH_DAY
                    or DateUtils.FORMAT_NO_YEAR)
//            getDateMMSimpleFormat(localDate)
        }
    }

    /**
     * 显示本地时间 年/月/日 HH:mm
     * 英文 eg： 03/05/2020 10:08
     * 中文/台湾/香港 2020/03/05 10:08
     *
     * @param timeStamp 单位 秒
     */
    @JvmStatic
    fun getDateYYYYMMDDHHmmLocalFormat(timeStamp: Long): String {
        val context: Context = app
        return DateUtils.formatDateTime(
            context, timeStamp * 1000,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NUMERIC_DATE
                    or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_24HOUR
        )
    }

    /**
     * 显示本地时间  HH:mm
     * 英文 eg： 10:08
     * 中文/台湾/香港 10:08
     *
     * @param timeStamp 单位 秒
     */
    @JvmStatic
    fun getHHmmLocalFormat(timeStamp: Long): String {
        val context: Context = app
        return DateUtils.formatDateTime(context, timeStamp * 1000,  DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_24HOUR)
    }

    /**
     * 格式化时间字符串转换为时间戳，指定要转化的时间字符串的格式
     *
     * @param pattern
     * @param _date
     * @return
     */
    fun dateToMs(_date: String?, pattern: String?): Long {
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return try {
            val date = format.parse(_date)
            date.time
        } catch (e: java.lang.Exception) {
            0
        }
    }

    @JvmStatic
    fun getDateYYYYMMDDLocalFormat(timeStamp: Long): String {
        val context: Context = app
        return DateUtils.formatDateTime(
            context, timeStamp * 1000,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NUMERIC_DATE
        )
    }

    /**
     * 获取 时间日期跨度 显示
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return
     */
    @JvmStatic
    fun getDateRangeFormat(start: LocalDate, end: LocalDate): String {
        val context: Context = app
        return DateUtils.formatDateRange(
            context,
            localDateToTimestampInner(start),
            localDateToTimestampInner(end),
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
        )
    }

    @JvmStatic
    fun getDateRangeMMddFormat(start: Long, end: Long): String {
        val context: Context = app
        return DateUtils.formatDateRange(
            context, localDateToTimestampInner(LocalDate(start)), localDateToTimestampInner(
                LocalDate(end)
            ), DateUtils.FORMAT_SHOW_DATE
        )
    }

    /**
     * 显示本地时间 年/月/日 HH:mm:ss
     * 英文 eg： 03/05/2020 10:08:45
     * 中文/台湾/香港 2020/03/05 10:08:45
     *
     * @param timeStamp 单位 秒
     */
    @JvmStatic
    fun getDateYYYYMMDDHHmmssLocalFormat(timeStamp: Long): String {
        return getDateStr(timeStamp, "yyyy-MM-dd HH:mm:ss")
    }

    @JvmStatic
    fun getDateYYYYMMDDHHmmssLocalFormat(timeStamp: Long, timeZone: TimeZone?): String {
        return getDateStr(timeStamp, "yyyy-MM-dd HH:mm:ss", timeZone)
    }

    /**
     * 显示本地时间 HH:mm
     * 区分12进制、24进制时间的显示
     * 12进制时间 08：20PM   08：20AM
     *
     * @param timeStamp 单位 秒
     */
    @JvmStatic
    fun getDateHHmmFormat(timeStamp: Long): String {
        val context: Context = app
        val is24Hour = DateFormat.is24HourFormat(context)
        return if (is24Hour) {
            getDateStr(timeStamp, "HH:mm")
        } else {
            getDateStr(timeStamp, "hh:mm aa")
        }
    }

    @JvmStatic
    val dateHHmmWithoutAMFFormatPattern: String
        get() {
            val context: Context = app
            val is24Hour = DateFormat.is24HourFormat(context)
            return if (is24Hour) {
                "HH:mm"
            } else {
                "hh:mm"
            }
        }

    /**
     * 显示本地时间 HH:mm
     * 区分12进制、24进制时间的显示
     * 12进制时间 08：20
     *
     * @param timeStamp 单位 秒
     */
    @JvmStatic
    fun getDate12HHmmFormatWithoutAM(timeStamp: Long): String {
        return getDateStr(timeStamp, dateHHmmWithoutAMFFormatPattern)
    }

    /**
     * 显示本地时间 HH:mm
     * 24进制时间的显示
     *
     * @param timeStamp 单位 秒
     */
    @JvmStatic
    fun getDateHHmm24Format(timeStamp: Long): String {
        return getDateStr(timeStamp, "HH:mm")
    }

    /**
     * 显示本地时间 hh:mm
     * 12进制 eg：08：20PM   08：20AM
     *
     * @param timeStamp 单位 秒
     */
    @JvmStatic
    fun getDateHHmm12Format(timeStamp: Long): String {
        return getDateStr(timeStamp, "hh:mm aa")
    }

    /**
     * 显示时间 eg： 16:39:10
     *
     * @param timeStamp 单位 秒
     */
    @JvmStatic
    fun getDateHHmmssFormat(timeStamp: Long): String {
        return getDateStr(timeStamp, "HH:mm:ss")
    }

    /**
     * 显示本地时间 年/月/日
     * 英文 eg：03/05/2020
     * 中文/台湾/香港 2020/03/05
     *
     * @param localDate
     */
    @JvmStatic
    fun getDateSimpleLocalFormat(localDate: LocalDate): String {
        val context: Context = app
        return DateUtils.formatDateTime(
            context,
            localDateToTimestampInner(localDate),
            DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NUMERIC_DATE
        )
    }

    /**
     * 显示本地时间 年/月/日
     * 英文 eg：03/05/2020
     * 中文/台湾/香港 2020/03/05
     *
     * @param timeStamp 单位 秒
     */
    @JvmStatic
    fun getDateSimpleLocalFormat(timeStamp: Long): String {
        val context: Context = app
        return DateUtils.formatDateTime(
            context,
            timeStamp * 1000,
            DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NUMERIC_DATE
        )
    }

    //@param timeStamp 单位 ms 毫秒 13位
    fun getDateSimpleLocalFormatS(timeStamp: Long): String {
        val context: Context = app
        return DateUtils.formatDateTime(
            context,
            timeStamp,
            DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NUMERIC_DATE
        )
    }

    /**
     * 根据 日的数值 添加不同的 Suffix
     *
     * @param day
     */
    fun getDayOfMonthSuffix(day: Int): String {
        val context: Context = app
        if (day < 1 || day > 31) {
            return ""
        }
        return if (day >= 11 && day <= 13) {
            context.getString(R.string.date_day_suffix_other)
        } else when (day % 10) {
            1 -> context.getString(R.string.date_day_suffix1)
            2 -> context.getString(R.string.date_day_suffix2)
            3 -> context.getString(R.string.date_day_suffix3)
            else -> context.getString(R.string.date_day_suffix_other)
        }
    }


    /**
     * 获得两个日期距离几个月
     */
    @JvmStatic
    fun getIntervalMonths(date1: LocalDate, date2: LocalDate): Int {
        var date1 = date1
        var date2 = date2
        date1 = date1.withDayOfMonth(1)
        date2 = date2.withDayOfMonth(1)
        return Months.monthsBetween(date1, date2).months
    }

    /**
     * 获取 星期 Week 完整版
     * 英文 eg: Mon Tues Wednesday Thursday Friday Saturday Sunday
     * 中文/香港 eg: 星期一 星期二 星期三 星期四 星期五 星期六 星期日
     * 台湾 eg：週一 週二 週三 週四 週五 週六 週日
     *
     * @param timestamp 13位的 ms
     */
    fun getWeekStr(timestamp: Long): String {
        val context: Context = app
        return DateUtils.formatDateTime(context, timestamp, DateUtils.FORMAT_SHOW_WEEKDAY)
    }

    @JvmStatic
    fun getWeekStr(localDate: LocalDate): String {
        val timestamp = localDateToTimestampInner(localDate)
        return getWeekStr(timestamp)
    }

    /**
     * 获取 星期 Week Simple版
     * 英文 eg: Monday Tuesday Wed Thurs Fri Sat Sun
     * 中文 eg: 周一 周二 周三 周四 周五 周六 周日
     * 台湾/香港 eg：週一 週二 週三 週四 週五 週六 週日
     *
     * @param timestamp 13位的 ms
     */
    fun getWeekSimpleStr(timestamp: Long): String {
        val context: Context = app
        return DateUtils.formatDateTime(
            context, timestamp,
            DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_WEEKDAY
        )
    }

    @JvmStatic
    fun getWeekSimpleStr(localDate: LocalDate): String {
        val timestamp = localDateToTimestampInner(localDate)
        return getWeekSimpleStr(timestamp)
    }

    fun yearsBetween(millis1: Long, mills2: Long) = abs(Years.yearsBetween(LocalDate(millis1).withDayOfYear(1), LocalDate(mills2).withDayOfYear(1)).years)

    /**
     * 获得两个日期距离几个月
     */
    fun monthsBetween(date1: LocalDate, date2: LocalDate) = abs(Months.monthsBetween(date1.withDayOfMonth(1), date2.withDayOfMonth(1)).months)
    fun monthsBetween(millis1: Long, mills2: Long) = monthsBetween(LocalDate(millis1), LocalDate(mills2))

    /**
     * @return 0 if in the same week；
     */
    fun weeksBetween(mills1: Long, mills2: Long): Int {
        val d1 = LocalDate(mills1).withDayOfWeek(1)
        val d2 = LocalDate(mills2).withDayOfWeek(1)
        return abs(Weeks.weeksBetween(d1, d2).weeks)
    }

    /**
     * @return mills 当天的0点
     */
    fun atStartOfDay(mills: Long) = LocalDate(mills).toDateTimeAtStartOfDay().millis
    fun atStartOfDay(date: LocalDate) = date.toDateTimeAtStartOfDay().millis

    /**
     * @return mills 当月的0点
     */
    fun atStartOfMonth(mills: Long) = atStartOfDay(LocalDate(mills).withDayOfMonth(1))
    fun atStartOfMonth(date: LocalDate) = atStartOfDay(date.withDayOfMonth(1))

    /**
     * @return 下个月1号的0点
     */
    fun atEndOfMonth(mills: Long) = atStartOfMonth(LocalDate(mills).plusMonths(1))

    fun atStartOfWeek(mills: Long) = atStartOfDay(LocalDate(mills).withDayOfWeek(1))

    /**
     * @return 下周一的0点
     */
    fun atEndOfWeek(mills: Long) = atStartOfWeek(mills) + DateUtils.WEEK_IN_MILLIS

    fun atStartOfYear(mills: Long) = atStartOfDay(LocalDate(mills).withDayOfYear(1))
    fun atStartOfYear(date: LocalDate) = atStartOfDay(date.withDayOfYear(1))
    fun atEndOfYear(mills: Long) = atStartOfYear(LocalDate(mills).plusYears(1))

    @JvmStatic
    fun getIntervalDay(date1: LocalDate?, date2: LocalDate?): Int {
        return Days.daysBetween(date1, date2).days
    }

    @JvmStatic
    fun getIntervalDay(mills1: Long, mills2: Long): Int {
        return getIntervalDay(LocalDate(mills1), LocalDate(mills2))
    }

    /**
     * 是否是今天
     *
     * @param date
     */
    @JvmStatic
    fun isToday(date: LocalDate): Boolean {
        return LocalDate() == date
    }

    @JvmStatic
    fun isToday(time: Long): Boolean {
        return LocalDate() == timestampToLocalDate(time)
    }

    //未来，明后天等
    @JvmStatic
    fun isAfterToday(localDate: LocalDate): Boolean {
        val today = LocalDate.now()
        return localDate.isAfter(today)
    }

    /**
     * 第一个是不是第二个的下一个月，只在此处有效
     *
     * @param currentMonth
     * @param nextMonth
     */
    @JvmStatic
    fun isNextMonth(currentMonth: LocalDate, nextMonth: LocalDate): Boolean {
        val date = nextMonth.minusMonths(1)
        return date.monthOfYear == currentMonth.monthOfYear
    }

    @JvmStatic
    fun isSameMonthLocalDate(date: LocalDate, compareDate: LocalDate): Boolean {
        return date.year == compareDate.year && date.monthOfYear == compareDate.monthOfYear
    }

    //是否同月
    @JvmStatic
    fun isEqualsMonth(date1: LocalDate, date2: LocalDate): Boolean {
        return date1.year == date2.year && date1.monthOfYear == date2.monthOfYear
    }

    /**
     * 第一个是不是第二个的上一个月,只在此处有效
     *
     * @param currentMonth
     * @param lastMonth
     */
    @JvmStatic
    fun isLastMonth(currentMonth: LocalDate, lastMonth: LocalDate): Boolean {
        val date = lastMonth.plusMonths(1)
        return date.monthOfYear == currentMonth.monthOfYear
    }

    @JvmStatic
    fun getMonthLocalDateCalendar(localDate: LocalDate): List<LocalDate> {
        return getMonthLocalDateCalendar(localDate, MONDAY)
    }

    fun getMonthLocalDateCalendar(localDate: LocalDate, type: Int): List<LocalDate> {
        val lastMonthDate = localDate.plusMonths(-1) //上个月
        val nextMonthDate = localDate.plusMonths(1) //下个月
        val days = localDate.dayOfMonth().maximumValue //当月天数
        val lastMonthDays = lastMonthDate.dayOfMonth().maximumValue //上个月的天数
        val firstDayOfWeek = LocalDate(localDate.year, localDate.monthOfYear, 1).dayOfWeek //当月第一天周几
        var endDayOfWeek =
            LocalDate(localDate.year, localDate.monthOfYear, days).dayOfWeek //当月最后一天周几
        val dateList: MutableList<LocalDate> = ArrayList()

        //周一开始的
        if (type == MONDAY) {
            //周一开始的
            for (i in 0 until firstDayOfWeek - 1) {
                val date = LocalDate(
                    lastMonthDate.year,
                    lastMonthDate.monthOfYear,
                    lastMonthDays - (firstDayOfWeek - i - 2)
                )
                dateList.add(date)
            }
            for (i in 0 until days) {
                val date = LocalDate(localDate.year, localDate.monthOfYear, i + 1)
                dateList.add(date)
            }
            for (i in 0 until 7 - endDayOfWeek) {
                val date = LocalDate(nextMonthDate.year, nextMonthDate.monthOfYear, i + 1)
                dateList.add(date)
            }
        } else {
            //上个月
            if (firstDayOfWeek != 7) {
                for (i in 0 until firstDayOfWeek) {
                    val date = LocalDate(
                        lastMonthDate.year,
                        lastMonthDate.monthOfYear,
                        lastMonthDays - (firstDayOfWeek - i - 1)
                    )
                    dateList.add(date)
                }
            }
            //当月
            for (i in 0 until days) {
                val date = LocalDate(localDate.year, localDate.monthOfYear, i + 1)
                dateList.add(date)
            }
            //下个月
            if (endDayOfWeek == 7) {
                endDayOfWeek = 0
            }
            for (i in 0 until 6 - endDayOfWeek) {
                val date = LocalDate(nextMonthDate.year, nextMonthDate.monthOfYear, i + 1)
                dateList.add(date)
            }
        }

        //某些年的2月份28天，又正好日历只占4行
        if (dateList.size == 28) {
            for (i in 0..6) {
                val date = LocalDate(nextMonthDate.year, nextMonthDate.monthOfYear, i + 1)
                dateList.add(date)
            }
        }
        return dateList
    }

    @JvmStatic
    fun isSameWeekWithToday(localDate: LocalDate): Boolean {
        val week = getDayWeek(localDate)
        return isThisYear(localDate) && week == todayWeek
    }

    private fun isThisYear(localDate: LocalDate): Boolean {
        return localDate.year == LocalDate.now().year
    }

    //获取今天坐在周
    @JvmStatic
    val todayWeek: Int
        get() = getDayWeek(LocalDate.now())

    //获取LocalDate所在的周
    @JvmStatic
    fun getDayWeek(localDate: LocalDate): Int {
        return localDate.weekOfWeekyear
    }

    /**
     * 获得两个日期距离几周
     *
     * @param type 一周
     */
    @JvmStatic
    fun getIntervalWeek(date1: LocalDate?, date2: LocalDate?, type: Int): Int {
        var date1 = date1
        var date2 = date2
        if (type == MONDAY) {
            date1 = getMonFirstDayOfWeek(date1!!)
            date2 = getMonFirstDayOfWeek(date2!!)
        } else {
            date1 = getSunFirstDayOfWeek(date1!!)
            date2 = getSunFirstDayOfWeek(date2!!)
        }
        return Weeks.weeksBetween(date1, date2).weeks
    }

    //获取这个周在这个月中的第几周
    fun getWeekOfMonth(dateList: List<LocalDate?>, mSelectDate: LocalDate?): Int {
        var weekOfMonth = -1
        val i = dateList.indexOf(mSelectDate)
        if (i >= 0) {
            weekOfMonth = if (i % 7 == 0) i / 7 else i / 7 + 1
        }
        return weekOfMonth
    }

    //转化一周从周日开始
    private fun getSunFirstDayOfWeek(date: LocalDate): LocalDate {
        return if (date.dayOfWeek().get() == 7) {
            date
        } else {
            date.minusWeeks(1).withDayOfWeek(7)
        }
    }

    //转化一周从周一开始
    @JvmStatic
    fun getMonFirstDayOfWeek(date: LocalDate): LocalDate {
        return date.dayOfWeek().withMinimumValue()
    }

    /**
     * 时间戳转成 LocalDate
     *
     * @param timestamp 13位 ms毫秒
     */
    @JvmStatic
    fun timestampToLocalDateInner(timestamp: Long): LocalDate {
        return Instant.ofEpochMilli(timestamp).toDateTime().toLocalDate()
    }

    /**
     * 时间戳转成 LocalDate
     *
     * @param timestamp 10位 秒 s
     */
    @JvmStatic
    fun timestampToLocalDate(timestamp: Long): LocalDate {
        return timestampToLocalDateInner(timestamp * 1000)
    }

    /**
     * LocalDate 转成 时间戳 13位 ms 毫秒
     *
     * @param localDate
     * @return timestamp 13位 ms 毫秒
     */
    private fun localDateToTimestampInner(localDate: LocalDate): Long {
        return localDate.toDateTimeAtCurrentTime().millis
    }

    /**
     * LocalDate 转成 时间戳 10位 s 秒
     *
     * @param localDate
     * @return timestamp 10位 s 秒
     */
    @JvmStatic
    fun localDateToTimestamp(localDate: LocalDate): Long {
        return localDateToTimestampInner(localDate) / 1000
    }

    /**
     * 时间取整，取到当天的0点对应的事件 例如 2019-03-23 00：00：00 (13位)
     *
     * @param localDate
     * @return timeStamp 13位 ms 毫秒
     */
    fun changZeroOfTheDayInner(localDate: LocalDate): Long {
        val time = localDateToTimestampInner(localDate)
        val sdf = SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.getDefault())
        val date = Date(time)
        val dateStr = sdf.format(date)
        return try {
            Timestamp.valueOf(dateStr).time
        } catch (e: Exception) {
            throw RuntimeException("changZeroOfTheDayInner $dateStr", e)
        }
    }

    fun changZeroOfTheDayInner(time: Long): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.getDefault())
        val date = Date(time)
        val dateStr = sdf.format(date)
        return try {
            Timestamp.valueOf(dateStr).time
        } catch (e: Exception) {
            throw RuntimeException("changZeroOfTheDayInner param time $dateStr", e)
        }
    }

    /**
     * 获取当日的0点， 返回timestamp 10位 秒 unit s
     *
     * @param time
     */
    @JvmStatic
    fun changZeroOfTheDay(time: Long): Long {
        return changZeroOfTheDayInner(time * 1000) / 1000
    }

    /**
     * 时间取整，取到当天的0点对应的事件 例如 2019-03-23 00：00：00 (10位)
     *
     * @param localDate
     * @return timeStamp 10位 s 秒
     */
    @JvmStatic
    fun changZeroOfTheDay(localDate: LocalDate): Long {
        return changZeroOfTheDayInner(localDate) / 1000
    }

    fun changeZeroOfTheDay(time: Long, tzOffsetInSec: Int): Long {
        val timeZone = getTimeZone(tzOffsetInSec.toLong())
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = time * 1000
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        return calendar.timeInMillis / 1000
    }

    @JvmStatic
    fun getTimeZone(zoneOffsetInSec: Long): TimeZone {
        val sign = if (zoneOffsetInSec >= 0) "+" else "-"
        val zoneOffInMin = TimeUnit.SECONDS.toMinutes(Math.abs(zoneOffsetInSec))
            .toInt()
        val hour = zoneOffInMin / 60
        val min = zoneOffInMin % 60
        val zoneId = "GMT" + sign + hour + ":" + if (min < 10) "0$min" else min
        return TimeZone.getTimeZone(zoneId)
    }
    //服务端夏令时时间、转成0点。
    //    public static long changZeroOfTheDayDST(long time){
    //        return changZeroOfTheDayInner((time  + TIME_HOUR) * 1000)/1000;
    //    }
    /**
     * 获取 下周一 0点0时0分
     *
     * @param localDate
     */
    @JvmStatic
    fun getNextWeekMondayTime(localDate: LocalDate?): Long {
        val mondayLocalDate = getNextWeekMonday(localDate?: LocalDate.now())
        return changZeroOfTheDay(mondayLocalDate)
    }

    /**
     * 获取下周一对应的LocalDate，用来请求周的参数
     *
     * @param localDate
     */
    private fun getNextWeekMonday(localDateParam: LocalDate?): LocalDate {
        val localDate = localDateParam?: LocalDate.now()
        val week = localDate.dayOfWeek
        val distance = 7 - week + 1
        return localDate.plusDays(distance)
    }

    /**
     * 获取本周一对应的LocalDate，用来请求周的参数
     *
     * @param localDate
     */
    @JvmStatic
    fun getWeekMonday(localDate: LocalDate?): LocalDate {
        return if (localDate == null) LocalDate.now().minusDays(LocalDate.now().dayOfWeek - 1) else{
            localDate.minusDays(localDate.dayOfWeek - 1)
        }
    }

    /**
     * 获取下个月1号对应的LocalDate，用来请求月的参数，返回 timestamp 秒 10位 s unit
     *
     * @param localDate
     */
    @JvmStatic
    fun getNextMonthFirstDayOfTime(localDate: LocalDate?): Long {
        val tempLocalDate = localDate?: LocalDate.now().plusMonths(1)
        return changZeroOfTheDay(getFirstDayOfMonth(tempLocalDate))
    }

    /**
     * 获取下个月1号对应的LocalDate，用来请求月的参数，返回 LocalDate
     *
     * @param localDate
     */
    @JvmStatic
    fun getFirstDayOfNextMonth(localDate: LocalDate?): LocalDate {
        val timestamp = getNextMonthFirstDayOfTime(localDate ?: LocalDate.now())
        return timestampToLocalDate(timestamp)
    }

    /**
     * 获取本月的1号对应的LocalDate，用来请求月的参数，返回LocalDate
     *
     * @param localDate
     */
    @JvmStatic
    fun getFirstDayOfMonth(localDate: LocalDate?): LocalDate {
        return if (localDate == null)timestampToLocalDate(getFirstDayOfMonthTime(LocalDate.now())) else  timestampToLocalDate(getFirstDayOfMonthTime(localDate))
    }

    /**
     * 获取本月的1号对应的LocalDate，用来请求月的参数，返回 timestamp 秒 10位 s unit
     *
     * @param localDate
     */
    @JvmStatic
    fun getFirstDayOfMonthTime(localDateParam: LocalDate): Long {
        val localDate = localDateParam?: LocalDate.now()
        val distance = localDate.dayOfMonth
        val firstDayOfMonth = localDate.minusDays(distance - 1)
        return changZeroOfTheDay(firstDayOfMonth)
    }

    @JvmStatic
    fun getFirstDayOfMonthTime(timestamp: Long): Long {
        val localDate = timestampToLocalDate(timestamp)
        val distance = localDate.dayOfMonth
        val firstDayOfMonth = localDate.minusDays(distance - 1)
        return changZeroOfTheDay(firstDayOfMonth)
    }

    /**
     * 判断是否是同一天
     *
     * @param date
     * @param compareDate
     */
    @JvmStatic
    fun isSameLocalDate(date: LocalDate, compareDate: LocalDate): Boolean {
        return date.year == compareDate.year && date.monthOfYear == compareDate.monthOfYear && date.dayOfMonth == compareDate.dayOfMonth
    }

    /**
     * 判断是不是 0点
     *
     * @param timestamp
     */
    @JvmStatic
    fun isEndHourOfTheDay(timestamp: Long): Boolean {
        if (timestamp % TIME_HOUR_INT != 0L) { //不能取整
            return false
        }
        val timestampPlusOneHour = timestamp - TIME_HOUR //画的是 0点
        val localDate1 = timestampToLocalDate(timestamp)
        val localDate2 = timestampToLocalDate(timestampPlusOneHour)
        return localDate1.dayOfWeek != localDate2.dayOfWeek
    }

    /**
     * 判断是不是月末
     *
     * @param localDate
     */
    @JvmStatic
    fun isLastDayOfMonth(localDate: LocalDate): Boolean {
        val temp = localDate.plusDays(1)
        return localDate.monthOfYear != temp.monthOfYear
    }

    /**
     * 距离当日零点 的 小时 值，必须整除，绘制图表的X轴需要
     *
     * @param timestamp
     */
    @JvmStatic
    fun getHourOfTheDay(timestamp: Long): Int {
        val localDate = timestampToLocalDate(timestamp)
        val zeroHour = changZeroOfTheDay(localDate)
        val distance = (timestamp - zeroHour).toInt()
        return if (distance % TIME_HOUR_INT != 0) {
            -1
        } else distance / TIME_HOUR_INT
    }

    /**
     * 判断是不是同一个月
     *
     * @param localDate
     * @param localDateCompare
     */
    @JvmStatic
    fun isSameMonth(localDate: LocalDate, localDateCompare: LocalDate): Boolean {
        return (localDate.year == localDateCompare.year
                && localDate.monthOfYear == localDateCompare.monthOfYear)
    }

    @JvmStatic
    fun isFuture(localDate: LocalDate): Boolean {
        return localDate.isAfter(LocalDate.now())
    }

    @JvmStatic
    fun isFuture(timestamp: Long): Boolean {
        val localDate = LocalDate(timestamp * 1000)
        return localDate.isAfter(LocalDate.now())
        //        return timestamp > System.currentTimeMillis()/1000;
    }

    @JvmStatic
    fun isSunday(localDateEntry: LocalDate): Boolean {
        return localDateEntry.dayOfWeek == 7
    }

    @JvmStatic
    fun getFirstMonthOfTheYear(localDate: LocalDate): LocalDate {
        val monthOfTheYear = localDate.monthOfYear
        val firstMonthOfYear = localDate.minusMonths(monthOfTheYear - 1)
        return getFirstDayOfMonth(firstMonthOfYear)
    }


    @JvmStatic
    fun isSameYear(compareLocalDate: LocalDate, localDate: LocalDate): Boolean {
        return compareLocalDate.year == localDate.year
    }

    @JvmStatic
    fun isSameYear(year1: Long, year2: Long): Boolean {
        return isSameYear(LocalDate(year1), LocalDate(year2))
    }

    /**
     * 时间单位
     *
     * @param timeDuration
     * @return
     */
    @JvmStatic
    fun getTimeStrWithSecDef(timeDuration: Int): String {
        return getTimeStrWithSec(timeDuration, ":", ":", "")
    }

    private fun getTimeStrWithSec(
        sleepTime: Int,
        hourUnit: String,
        minUnit: String,
        scdUnit: String
    ): String {
        var hourStr = "00"
        val hour = sleepTime / TIME_HOUR_INT
        when {
            hour >= 10 -> {
                hourStr = hour.toString() + hourUnit
            }
            hour > 0 -> {
                hourStr = "0$hour$hourUnit"
            }
            hour == 0 -> {
                hourStr += minUnit
            }
        }
        var minStr = "00"
        val minReminder = sleepTime % TIME_HOUR_INT
        val min = minReminder / TIME_MIN_INT
        when {
            min >= 10 -> {
                minStr = min.toString() + minUnit
            }
            min > 0 -> {
                minStr = "0$min$minUnit"
            }
            min == 0 -> {
                minStr += minUnit
            }
        }
        var secondStr = "00"
        val secondReminder = minReminder % TIME_MIN_INT
        when {
            secondReminder >= 10 -> {
                secondStr = secondReminder.toString() + scdUnit
            }
            secondReminder > 0 -> {
                secondStr = "0$secondReminder$scdUnit"
            }
            secondReminder == 0 -> {
                secondStr += scdUnit
            }
        }
        return hourStr + minStr + secondStr
    }

    /**
     * 配速单位
     *
     * @param time
     * @return
     */
    @JvmStatic
    fun getSportPaceStrWithSecDef(time: Int): String {
        return if (time > TIME_HOUR_INT) {
            getPaceStrWithSec(time, ":", "'", "\"")
        } else {
            getPaceStrWithSec(time, ":", "'", "\"")
        }
    }

    private fun getPaceStrWithSec(
        time: Int,
        hourUnit: String,
        minUnit: String,
        scdUnit: String
    ): String {
        var hourStr = ""
        val hour = time / TIME_HOUR_INT
        if (hour > 0) {
            hourStr = hour.toString() + hourUnit
        }
        var minStr = ""
        val minReminder = time % TIME_HOUR_INT
        val min = minReminder / TIME_MIN_INT
        if (min > 0) {
            minStr = min.toString() + minUnit
        }
        var secondStr = ""
        val secondReminder = minReminder % TIME_MIN_INT
        secondStr = if (secondReminder > 0) {
            secondReminder.toString() + scdUnit
        }else {
            "00$scdUnit"
        }
        if (!TextUtils.isEmpty(hourStr) && TextUtils.isEmpty(minStr)) {
            minStr = "0$minUnit"
        }
        return  hourStr + minStr + secondStr
    }

    @JvmStatic
    fun getZNTimeWithSec(time: Int): String? {
        val context: Context = app
        var hourStr = ""
        val hour = time / TIME_HOUR_INT
        if (hour > 0) {
            hourStr = context.resources.getQuantityString(R.plurals.unit_hour_desc2, hour, hour)
        }
        var minStr = ""
        val minReminder = time % TIME_HOUR_INT
        val min = minReminder / TIME_MIN_INT
        if (min > 0) {
            minStr = context.resources.getQuantityString(R.plurals.unit_min_desc2, min, min)
        }
        var secondStr = ""
        val second = minReminder % TIME_MIN_INT
        if (second > 0) {
            secondStr =
                context.resources.getQuantityString(R.plurals.unit_seconds_desc, second, second)
        }
        if (!TextUtils.isEmpty(hourStr) && TextUtils.isEmpty(minStr)) {
            minStr = context.resources.getQuantityString(R.plurals.unit_min_desc2, 0, 0)
        }
        var resultStr = getHourMinSecondStr(context, hourStr, minStr, secondStr)
        if (TextUtils.isEmpty(resultStr)) { //
            resultStr = context.resources.getQuantityString(R.plurals.unit_seconds_desc, 0, 0)
        }
        return resultStr
    }

    private fun getHourMinSecondStr(
        context: Context,
        hourStr: String?,
        minStr: String?,
        secondStr: String
    ): String {
        if (TextUtils.isEmpty(hourStr) && TextUtils.isEmpty(minStr) && TextUtils.isEmpty(secondStr)) {
            return ""
        }
        return if (TextUtils.isEmpty(hourStr)) {
            context.getString(
                R.string.common_time_join_str,
                minStr,
                secondStr
            )
        } else if (TextUtils.isEmpty(secondStr)) {
            context.getString(R.string.common_time_join_str, hourStr, minStr)
        } else {
            context.getString(
                R.string.common_time_join_str1,
                hourStr,
                minStr,
                secondStr
            )
        }
    }

    /**
     * 当小时>0时，显示"2时2分 2h2m", 当小时=0时，显示"2分钟 2min"
     *
     * @param time 单位 分/分钟
     * @return [x时]x分
     */
    fun getZNTimeWithMinute(time: Int, isSimple: Boolean): String {
        val context: Context = app
        var hourStr = ""
        var minStr = ""
        val hour = time / 60
        val min = time % 60
        if (hour > 0) {
            hourStr = context.resources.getQuantityString(R.plurals.unit_hour_desc2, hour, hour)
            if (min > 0) {
                minStr = context.resources.getQuantityString(R.plurals.unit_min_desc2, min, min)
            }
        } else if (min >= 0) {
            minStr = context.resources.getQuantityString(R.plurals.unit_min_desc, min, min)
        }
        if (!TextUtils.isEmpty(hourStr) && !TextUtils.isEmpty(minStr)) {
            return context.getString(R.string.common_time_join_str, hourStr, minStr)
        } else if (TextUtils.isEmpty(hourStr)) {
            return minStr
        } else if (TextUtils.isEmpty(minStr)) {
            return hourStr
        }
        return minStr
    }

    /**
     * 返回：x小时x分钟
     * @param seconds 秒数
     * @return
     */
    fun getZNTimeStrWithHrAndMin(seconds: Int): String {
        val mins = seconds / 60
        val hour = mins / 60
        val min = mins % 60
        return getZNTimeStrWithHrAndMin(hour, min)
    }

    /**
     * 返回：x小时x分钟
     * @param mins 分钟数
     * @return
     */
    fun getZNTimeStrWithHrAndMinInMins(mins: Int): String {
        val hour = mins / 60
        val min = mins % 60
        return getZNTimeStrWithHrAndMin(hour, min)
    }

    /**
     * hour>0 X小时X分X秒 hour=0 X分钟X秒  分钟和分英文都用min表示，区分单复数
     * 当小时和分钟一起描述时，1 hr 0 min是错的，应该是1 hr。
     *
     * @param hour
     * @param min
     * @return
     */
    fun getZNTimeStrWithHrAndMin(hour: Int, min: Int): String {
        val context: Context = app
        var hourStr = ""
        var minStr = ""
        if (hour > 0) {
            hourStr = context.resources.getQuantityString(
                R.plurals.unit_hour_desc, hour,
                hour
            )
            if (min > 0) {
                minStr = context.resources.getQuantityString(
                    R.plurals.unit_min_desc3, min,
                    min
                )
            }
        } else if (min > 0) {
            minStr = context.resources.getQuantityString(R.plurals.unit_min_desc, min, min)
        }
        return if (!TextUtils.isEmpty(hourStr) || !TextUtils.isEmpty(minStr)) {
            spliceTime(hourStr, minStr)
        } else context.resources.getQuantityString(
            R.plurals.unit_min_desc,
            0,
            0
        )
    }

    /**
     * 秒单独显示时，英文用sec区分单复数,和分钟一块显示时，用简写s
     * 当分钟和秒一起描述时，如果秒数为0，那么不需要写上0 s，比如1 min 0 s 可以改为：1 min；
     *
     * @param min
     * @param second
     * @return
     */
    fun getZNTimeStrWithMinAndSec(min: Int, second: Int): String {
        val context: Context = app
        var minStr = ""
        var secStr = ""
        if (min > 0) {
            minStr = context.resources.getQuantityString(
                R.plurals.unit_min_desc,
                min, min
            )
            if (second > 0) {
                secStr = context.resources.getQuantityString(
                    R.plurals.unit_seconds_short_desc,
                    second, second
                )
            }
        } else if (second > 0) {
            secStr = context.resources.getQuantityString(
                R.plurals.unit_seconds_desc,
                second, second
            )
        }
        return if (!TextUtils.isEmpty(minStr) || !TextUtils.isEmpty(secStr)) {
            spliceTime(minStr, secStr)
        } else context.resources.getQuantityString(
            R.plurals.unit_min_desc,
            0,
            0
        )
    }

    fun spliceTime(str1: String?, str2: String?): String {
        val context: Context = app
        return context.getString(R.string.common_time_join_str, str1, str2)
    }

    /**
     * 1天2小时2分钟->1 d 2 h 2 m; 1小时->1 h; 1分钟->1 min; 1小时10分钟->1 h 10m
     * eg: 1天2小时2分钟后响铃
     */
    fun getZNTimeDDHHMM(min: Int): String {
        val resource = app.resources
        if (min <= 0) {
            return resource.getQuantityString(R.plurals.min_in_simplify, min, min)
        }
        val days = TimeUnit.MINUTES.toDays(min.toLong()).toInt()
        val hours = (TimeUnit.MINUTES.toHours(min.toLong()) - days * 24).toInt()
        val minutes = min - days * 1440 - hours * 60
        var hourStr = ""
        var minStr = ""
        var dayStr = ""
        if (days > 0) {
            dayStr = resource.getQuantityString(R.plurals.day_in_simplify, days, days)
        }
        if (hours > 0) {
            hourStr = resource.getQuantityString(R.plurals.hour_in_simplify, hours, hours)
        }
        if (minutes > 0) {
            minStr =
                if (TextUtils.isEmpty(dayStr) && TextUtils.isEmpty(hourStr)) {
                    resource.getQuantityString(
                        R.plurals.min_in_simplify_2,
                        minutes,
                        minutes
                    )
                } else {
                    resource.getQuantityString(
                        R.plurals.min_in_simplify,
                        minutes,
                        minutes
                    )
                }
        }
        var ret = if (!dayStr.isEmpty()) dayStr else if (!hourStr.isEmpty()) hourStr else minStr
        if (!dayStr.isEmpty() && !hourStr.isEmpty()) {
            ret = resource.getString(R.string.common_time_join_str, ret, hourStr)
        }
        if (!hourStr.isEmpty() && !minStr.isEmpty()) {
            ret = resource.getString(R.string.common_time_join_str, ret, minStr)
        }
        return ret
    }

    /**
     * @param time 单位 分/分钟
     * @return [x时]x分
     */
    @JvmStatic
    fun getZNTimeWithMin(time: Int): String {
        return getZNTimeWithMinute(time, true)
    }

    //endTime, startTime 之间必须大于一天。
    @JvmStatic
    fun isEndTimeAfterStart(endTime: Long, startTime: Long): Boolean {
        val startLocalDate = timestampToLocalDate(startTime)
        val endLocalDate = timestampToLocalDate(endTime)
        return endLocalDate.isAfter(startLocalDate)
    }

    fun getBetweenDays(Msec1: Long, Msec2: Long): Long {
        return (Msec1 - Msec2 + 1000000) / (60 * 60 * 24 * 1000)
    }

    private fun isLeapYear(year: Int): Boolean {
        val b1 = year % 4 == 0
        val b2 = year % 100 != 0
        val b3 = year % 400 == 0
        return b1 && b2 || b3
    }

    fun getMonth(mills: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = mills
        return cal.get(Calendar.MONTH)
    }

    /**
     * @param timeInMillis
     * @return days in a month
     */
    fun getDaysOfMonth(timeInMillis: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeInMillis
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    /**
     * @param year
     * @param month 1~12
     * @return days in a month
     */
    fun getDaysOfMonth(year: Int, month: Int): Int {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month - 1
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    /**
     * 1..
     */
    fun getDayOfWeek(mills: Long): Int {
        return LocalDate(mills).dayOfWeek
    }

    /**
     * 1..31
     */
    fun getDayOfMonth(mills: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = mills
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 0..
     */
    fun getMonthOfYear(mills: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = mills
        return cal.get(Calendar.MONTH)
    }
    @JvmStatic
    fun getTimeStr(t1: Long): String {
        val strDateFormat = "yyyy-MM-dd HH:mm:ss"
        return getDateStr(t1, strDateFormat)
    }

    /**
     * 获取当前时区
     *
     * @return
     */
    val currentTimeID: String
        get() {
            val tz = TimeZone.getDefault()
            return tz.id
        }

    /**
     * @param time
     * @return
     */
    fun getTimePeriod(time: Long): String {
        val dateTime = DateTime(time * 1000)
        val hour = dateTime.hourOfDay
        val context: Context = app
        if (hour >= 0 && hour <= 6) {
            return context.getString(R.string.time_period_early_morning)
        } else if (hour > 6 && hour < 12) {
            return context.getString(R.string.time_period_morning)
        }
        return if (hour == 12) {
            context.getString(R.string.time_period_noon)
        } else if (hour >= 13 && hour <= 18) {
            context.getString(R.string.time_period_afternoon)
        } else {
//        }else(hour > 18 && hour <= 24) {
            context.getString(R.string.time_period_night)
        }
    }

    fun getTimePeriodAndTime(time: Long): String {
        val context: Context = app
        val timePeriod = getTimePeriod(time)
        val timeStr12 = getDateStr(time, "hh:mm")
        return context.getString(R.string.common_join_str, timePeriod, timeStr12)
    }

    /**
     * 是否是12小时制
     *
     * @param context
     */
    fun is12HourFormat(context: Context?): Boolean {
        return !DateFormat.is24HourFormat(context)
    }

    /**
     * @param time 单位为秒
     * @return
     */
    fun getLastUpdateTimeStr(time: Long): String {
        val context: Context = app
        return if (time < TIME_HOUR_MIN) { //小于 1分钟 返回 刚刚
            context.getString(R.string.time_update_just_now)
        } else if (time < TIME_DAY) { // 小于 1天 返回  h小时m分钟前
            val min = (time / TIME_MIN_INT).toInt()
            val timeStr = getZNTimeWithMin(min)
            context.getString(R.string.time_update_time_before, timeStr)
        } else if (time < 3 * TIME_DAY) { // 小于 3天，返回 1, 2, 3天前
            val days = (time / TIME_DAY).toInt()
            context.resources.getQuantityString(
                R.plurals.time_update_days_before,
                days,
                days
            )
        } else { // 大于 3天的 返回 具体更新的时间
            getDateYYYYMMDDHHmmLocalFormat(time)
        }
    }

    fun getTimeByHourMinute(hour: Int, minute: Int): String {
        var hourStr = hour.toString()
        var minuteStr = minute.toString()
        if (hour < 10) {
            hourStr = "0$hourStr"
        }
        if (minute < 10) {
            minuteStr = "0$minuteStr"
        }
        return "$hourStr:$minuteStr"
    }

    val zeroOfToday: Calendar
        get() {
            val calendar = Calendar.getInstance()
            zeroCalendar(calendar)
            return calendar
        }

    fun getZeroCalendarOf(timeInMills: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMills
        zeroCalendar(calendar)
        return calendar
    }

    /**
     * 返回值：秒
     */
    fun getZeroStartOf(timeInMills: Long, calendar: Calendar): Long {
        calendar.timeInMillis = timeInMills
        zeroCalendar(calendar)
        return calendar.timeInMillis / 1000
    }

    fun zeroCalendar(calendar: Calendar) {
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
    }

    @JvmStatic
    fun getHMFormatInMinutes(cxt: Context, timeInMinute: Long): String {
        return getHMFormatInSecond(cxt, timeInMinute * 60)
    }

    @JvmStatic
    fun getHMFormatInSecond(cxt: Context, timeInSecond: Long): String {
        return getHMFormatInMillis(cxt, timeInSecond * 1000)
    }

    /**
     * @return x小时，x分钟，x小时y分
     */
    fun getHMFormatInMillis(cxt: Context, timeInMillis: Long): String {
        Objects.requireNonNull(cxt)
        val res = cxt.resources
        val hms = getHourMinuteAndSecond(timeInMillis)
        val hour = hms[0]
        val min = hms[1]
        return if (hour == 0) {
            res.getQuantityString(R.plurals.unit_min_desc, min, min)
        } else {
            if (min == 0) {
                res.getQuantityString(R.plurals.unit_hour_desc, hour, hour)
            } else {
                val hStr =
                    res.getQuantityString(
                        R.plurals.unit_hour_desc,
                        hour,
                        hour
                    )
                val mStr =
                    res.getQuantityString(R.plurals.unit_min_desc2, min, min)
                res.getString(R.string.common_time_join_str, hStr, mStr)
            }
        }
    }

    @JvmStatic
    fun getDurationHrMinSecInMills(ctx: Context, timeInMillis: Long): String {
        Objects.requireNonNull(ctx)
        val res = ctx.resources
        val hms = getHourMinuteAndSecond(timeInMillis)
        val hour = hms[0]
        val min = hms[1]
        val sec = hms[2]
        return if (hour == 0) {
            res.getQuantityString(R.plurals.unit_min_desc, min, min)
        } else {
            val hStr = res.getQuantityString(
                R.plurals.unit_hour_desc,
                hour,
                hour
            )
            val mStr =
                res.getQuantityString(R.plurals.unit_min_desc2, min, min)
            val sStr = res.getQuantityString(R.plurals.unit_seconds_desc, sec, sec)
            res.getString(R.string.common_time_join_str1, hStr, mStr, sStr)
        }
    }

    @JvmStatic
    fun getCurrentHourTime(): Long {
        val currentDateTime = DateTime()
        val dateTime = currentDateTime.withMillisOfDay(0)//当日的零点
        val hour = currentDateTime.hourOfDay // 这个地方是 0 开头，还是 1.
        return dateTime.millis / 1000 + hour * TIME_HOUR_INT
    }

    fun getCurrentInitTime(): Long {
        val currentDateTime = DateTime()
        val dateTime = currentDateTime.withMillisOfDay(0)//当日的零点
        return dateTime.millis / 1000
    }

    /**
     * @param ms time in millisecond
     * @return 0: hour 1:minute 3:second
     */
    private fun getHourMinuteAndSecond(ms: Long): IntArray {
        val result = IntArray(3)
        if (ms <= 0) {
            return result
        }
        var second = (ms / 1000).toInt()
        var min = second / 60
        val hour = min / 60
        min %= 60
        second %= 60
        result[0] = hour
        result[1] = min
        result[2] = second
        return result
    }
}

internal class Hour12 {
    var hour = 0

    // 默认上午
    var isAnte = true

}