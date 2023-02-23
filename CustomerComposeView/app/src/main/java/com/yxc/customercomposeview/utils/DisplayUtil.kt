package com.yxc.customercomposeview.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import androidx.annotation.AttrRes
import androidx.core.content.res.ResourcesCompat

/**
 * @author yxc
 * @since 2019/2/27
 */
object DisplayUtil {
    val defBorderMargin: Int
        get() = dip2px(12f)

    fun dip2pxF(dpValue: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return dpValue * scale + 0.5f
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    @JvmStatic
    fun dip2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun getFixDp2Px(dpValue: Float): Int {
        val targetDisplayMetrics = Resources.getSystem().displayMetrics
        val density = targetDisplayMetrics.widthPixels / 360f
        return (dpValue * density + 0.5f).toInt()
    }

    fun px2dp(context: Context, pxValue: Float): Int {
        val density = context.resources.displayMetrics.density //得到设备的密度
        return (pxValue / density + 0.5f).toInt()
    }

    @JvmStatic
    @Deprecated("", ReplaceWith("dpValue.dp","com.xiaomi.fitness.common.utils.ExtUtils"))
    fun dp2px(context: Context, dpValue: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dpValue * density + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val scaleDensity = context.resources.displayMetrics.scaledDensity //缩放密度
        return (pxValue / scaleDensity + 0.5f).toInt()
    }

    @JvmStatic
    @Deprecated("", ReplaceWith("dpValue.sp","com.xiaomi.fitness.common.utils.ExtUtils"))
    fun sp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    @JvmStatic
    @Deprecated("", ReplaceWith("dpValue.sp","com.xiaomi.fitness.common.utils.ExtUtils"))
    fun sp2px(context: Context, spValue: Float): Int {
        val scaleDensity = context.resources.displayMetrics.scaledDensity
        return (spValue * scaleDensity + 0.5f).toInt()
    }

    fun sp2pxF(dpValue: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return dpValue * scale + 0.5f
    }

    fun getScreenDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    @JvmStatic
    val screenDensity: Float
        get() = Resources.getSystem().displayMetrics.density

    @JvmStatic
    fun setTranslucentStatusBar(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = Color.TRANSPARENT
    }


    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }



    @JvmStatic
    val statusBarHeight: Int
        get() {
            val resourceId =
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) Resources.getSystem()
                .getDimensionPixelSize(resourceId) else 0
        }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    @JvmStatic
    fun hideBottomUIMenu(mActivity: Activity?) {
        if (null == mActivity || mActivity.isFinishing || !hasNavBar(mActivity)) {
            return
        }
        //隐藏虚拟按键，并且全屏
        val _window = mActivity.window
        val params = _window.attributes
        params.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
        _window.attributes = params
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    fun hasNavBar(context: Context): Boolean {
        val res = context.resources
        val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
        return if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId)
            // check override flag
            val sNavBarOverride = navBarOverride
            if ("1" == sNavBarOverride) {
                hasNav = false
            } else if ("0" == sNavBarOverride) {
                hasNav = true
            }
            hasNav
        } else { // fallback
            !ViewConfiguration.get(context).hasPermanentMenuKey()
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private val navBarOverride: String?
        private get() {
            var sNavBarOverride: String? = null
            try {
                val c = Class.forName("android.os.SystemProperties")
                val m = c.getDeclaredMethod("get", String::class.java)
                m.isAccessible = true
                sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
            } catch (e: Throwable) {
            }
            return sNavBarOverride
        }

    @JvmStatic
    fun setTypeface(context: Context, paint: Paint, @AttrRes attr: Int) {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attr, typedValue, true)
        val resourceId = typedValue.resourceId
        if (resourceId > 0) {
            val font = ResourcesCompat.getFont(context, resourceId)
            paint.typeface = font
        }
    }

    fun getTypeface(context: Context, @AttrRes attr: Int): Typeface? {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attr, typedValue, true)
        val resourceId = typedValue.resourceId
        return if (resourceId > 0) ResourcesCompat.getFont(
            context,
            resourceId
        ) else Typeface.DEFAULT
    }

    @JvmStatic
    fun getTypefaceAsResourceId(context: Context?, resourceId: Int): Typeface? {
        return if (resourceId > 0) ResourcesCompat.getFont(
            context!!,
            resourceId
        ) else Typeface.DEFAULT
    }

    @JvmStatic
    fun setStatusBarFontColor(activity: Activity?, isBlack: Boolean) {
        if (isBlack) {
            //实现状态栏图标和文字颜色为暗色
            activity!!.window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            //实现状态栏图标和文字颜色为浅色
            activity!!.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}