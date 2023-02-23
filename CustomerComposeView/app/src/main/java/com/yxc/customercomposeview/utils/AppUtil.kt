package com.yxc.customercomposeview.utils

import android.app.Application
import android.content.Context
import android.os.Looper
import android.util.AndroidRuntimeException
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import java.util.*

/**
 * App相关的工具类
 */
object AppUtil {

    private const val TAG = "AppUtil"

    const val PACKAGE_NAME_MIHEALTH = "com.mi.health"

    const val PACKAGE_NAME_WEARABLE = "com.xiaomi.wearable"

    @Volatile
    private var sApp: Application? = null

    var isPlayChannel = false

    var isDevChannel = false

    fun init(app: Application) {
        if (sApp != null) {
            throw AndroidRuntimeException("App already init")
        }
        sApp = app
    }

    fun hasInit(): Boolean {
        return sApp != null
    }

    /**
     * 先不要使用该工具类，后面可能会替换掉，使用[AnyExtKt.getApplication]
     * Kotlin 可以直接调用application获取。
     *
     * @return
     */
    @JvmStatic
    val app: Application
        get() {
            if (sApp == null) {
                throw AndroidRuntimeException("Please call AppUtil.init(Application app)")
            }
            return sApp!!
        }

    val isMainThread: Boolean
        get() = Looper.getMainLooper().thread === Thread.currentThread()

    @JvmStatic
    val versionName: String by lazy { AppVersionUtil.appVersionName }

    @JvmStatic
    val versionCode: Long by lazy { AppVersionUtil.appVersionCode }

    @JvmStatic
    fun isRTLDirection(): Boolean {
        val direction = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
        return direction == ViewCompat.LAYOUT_DIRECTION_RTL
    }

    @JvmStatic
    fun getTxtFontSize(context: Context):Float{
       return context.resources.configuration.fontScale
    }



}