package com.yxc.customercomposeview.common

import android.content.Context
import android.content.res.Resources

fun dip2px(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun getResourcesColor(context: Context, color: Int): Int {
    var ret = 0x00ffffff
    try {
        ret = context.resources.getColor(color)
    } catch (e: Exception) {
    }
    return ret
}