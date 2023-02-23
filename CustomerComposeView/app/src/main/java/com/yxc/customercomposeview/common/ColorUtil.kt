package com.yxc.customercomposeview.common

import android.content.Context
import com.yxc.customercomposeview.utils.AppUtil.app

/**
 * @author yxc
 * @date 2019/3/1
 */
object ColorUtil {
    /**
     * 获取资源中的颜色
     * @param color
     * @return
     */
    fun getResourcesColor(context: Context, color: Int): Int {
        var ret = 0x00ffffff
        try {
            ret = context.resources.getColor(color)
        } catch (e: Exception) {
        }
        return ret
    }

    @JvmStatic
    fun getResourcesColor(color: Int): Int {
        return getResourcesColor(app, color)
    }
}