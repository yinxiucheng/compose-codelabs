package com.yxc.customercomposeview.utils

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

/**
 * @author yxc
 * @since 2019-11-25
 */
object ViewUtil {
    @JvmStatic
    fun setTouchDelegate(view: View, expandTouchWidth: Int) {
        val parentView = view.parent as View
        parentView.post {
            val rect = Rect()
            view.getHitRect(rect) // view构建完成后才能获取，所以放在post中执行
            // 4个方向增加矩形区域
            rect.top -= expandTouchWidth
            rect.bottom += expandTouchWidth
            rect.left -= expandTouchWidth
            rect.right += expandTouchWidth
            parentView.touchDelegate = TouchDelegate(rect, view)
        }
    }

    private var lastClickTime: Long = 0 //上次点击的时间
    private const val spaceTime = 500 //时间间隔//是否允许点击

    //当前系统时间
    val isFastClick: Boolean
        get() {
            val currentTime = System.currentTimeMillis() //当前系统时间
            val isFastClick: Boolean = currentTime - lastClickTime <= spaceTime //是否允许点击
            lastClickTime = currentTime
            return isFastClick
        }
}