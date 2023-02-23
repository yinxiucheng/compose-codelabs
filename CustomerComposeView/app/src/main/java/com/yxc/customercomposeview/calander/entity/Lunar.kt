package com.yxc.customercomposeview.calander.entity

/**
 * 农历
 */
class Lunar {
    var isLeap = false
    var lunarDay = 0
    var lunarMonth = 0
    var lunarYear = 0
    var leapMonth = 0
    var lunarDayStr: String? = null
    var lunarMonthStr: String? = null
    var lunarYearStr: String? = null
    var animals //生肖
            : String? = null
    var chineseEra //天干地支
            : String? = null
    var lunarDrawStr //农历绘制的文字
            : String? = null
}