package com.yxc.customercomposeview.calander.entity

import org.joda.time.LocalDate
import java.io.Serializable

/**
 * 公历
 */
class NDate : Serializable {
    var localDate //公历日期
            : LocalDate? = null
    var lunar: Lunar? = null
    var solarHoliday //公历节日
            : String? = null
    var lunarHoliday //农历节日
            : String? = null
    var solarTerm //节气
            : String? = null

    constructor(localDate: LocalDate) {
        this.localDate = localDate
    }

    override fun equals(obj: Any?): Boolean {
        return if (obj is NDate) {
            localDate!! == obj.localDate
        } else {
            false
        }
    }
}