package com.yxc.customercomposeview.calander.utils

import org.joda.time.Instant
import org.joda.time.LocalDate

object CalendarUtil {
    /**
     * 周视图的数据
     *
     * @param localDate
     * @return
     */
    @JvmStatic
    fun getWeekCalendar(localDate: LocalDate, type: Int): List<LocalDate> {
        var localDate = localDate
        val dateList: MutableList<LocalDate> = ArrayList()
        localDate = if (type == CalendarAttrs.MONDAY) {
            getMonFirstDayOfWeek(localDate)
        } else {
            getSunFirstDayOfWeek(localDate)
        }
        for (i in 0..6) {
            val date = localDate.plusDays(i)
            dateList.add(date)
        }
        return dateList
    }

    /**
     * @param localDate 今天
     * @param type      300，周日，301周一
     * @return
     */
    @JvmStatic
    fun getMonthCalendar(localDate: LocalDate, type: Int): List<LocalDate> {
        val lastMonthDate = localDate.plusMonths(-1) //上个月
        val nextMonthDate = localDate.plusMonths(1) //下个月
        val days = localDate.dayOfMonth().maximumValue //当月天数
        val lastMonthDays = lastMonthDate.dayOfMonth().maximumValue //上个月的天数
        val firstDayOfWeek = LocalDate(localDate.year, localDate.monthOfYear, 1).dayOfWeek //当月第一天周几
        var endDayOfWeek =
            LocalDate(localDate.year, localDate.monthOfYear, days).dayOfWeek //当月最后一天周几
        val dateList: MutableList<LocalDate> = ArrayList()


        //周一开始的
        if (type == CalendarAttrs.MONDAY) {

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

    //转化一周从周日开始
    fun getSunFirstDayOfWeek(date: LocalDate): LocalDate {
        return if (date.dayOfWeek().get() == 7) {
            date
        } else {
            date.minusWeeks(1).withDayOfWeek(7)
        }
    }

    //转化一周从周一开始
    fun getMonFirstDayOfWeek(date: LocalDate): LocalDate {
        return date.dayOfWeek().withMinimumValue()
    }

    //时间戳转成 LocalDate 13位
    fun timestampToLocalDateInner(timestamp: Long): LocalDate {
        return Instant.ofEpochMilli(timestamp).toDateTime().toLocalDate()
    }

    fun timestampToLocalDate(timestamp: Long): LocalDate {
        return timestampToLocalDateInner(timestamp * 1000)
    }
}