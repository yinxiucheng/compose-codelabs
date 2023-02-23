package com.yxc.customercomposeview.calander.model

import org.joda.time.LocalDate

/**
 * @author yxc
 * @date 2019/3/8
 */
class MonthCalendarModel {
    @JvmField
    var localDate: LocalDate? = null

    @JvmField
    var isFuture //未来的月，灰色不可点击
            = false
    @JvmField
    var isCurrent //当前月
            = false
    @JvmField
    var isSelected //选中月
            = false

    constructor() {}
    constructor(localDate: LocalDate?) {
        this.localDate = localDate
    }
}