package com.yxc.customercomposeview.rainbow

import android.content.res.Resources


object RainbowConstant {

    val RESIZE_BG: Int = dip2px(0.14285712688)

    val RESIZE: Int = dip2px(0.1428571269)

    const val TRANSPARENT_VALUE = (255 * 0.2).toInt()

    const val FIRST_WRAPPER_FIX_ANGLE = 2.05f

    const val FIRST_INNER_FIX_ANGLE = 2.7f

    const val SECOND_WRAPPER_FIX_ANGLE = 3f

    const val SECOND_INNER_FIX_ANGLE = 4.7f

    const val THIRD_WRAPPER_FIX_ANGLE = 3.8f

    const val THIRD_INNER_FIX_ANGLE = 15f

    const val TARGET_FIRST_TYPE = 1

    const val TARGET_SECOND_TYPE = 2

    const val TARGET_THIRD_TYPE = 3

    const val FIRST_FOUND_ANGLE_FRACTION = 0.03f

    const val SECOND_FOUND_ANGLE_FRACTION = 0.05f

    const val THIRD_FOUND_ANGLE_FRACTION = 0.10f

    private fun dip2px(dpValue: Double): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}