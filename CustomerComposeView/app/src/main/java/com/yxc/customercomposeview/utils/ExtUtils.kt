package com.yxc.customercomposeview.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

/**
 * Created by ccino on 2021/9/8.
 */

val Number.dp: Int
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).roundToInt()

val Number.dpf: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

val Number.sp: Int
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics).roundToInt()

val Number.spf: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics)

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun View.toggleGone() = apply { visibility = if (visibility != View.VISIBLE) View.VISIBLE else View.GONE }

val <T : Context> T.inflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun getColor(context: Context, @ColorRes colorRes: Int) = ContextCompat.getColor(context, colorRes)

fun getDimension(context: Context, @DimenRes dimen: Int) = context.resources.getDimensionPixelSize(dimen)

inline fun <reified T> Activity.intent(key: String) = lazy(LazyThreadSafetyMode.NONE) {
    when (val value = intent?.extras?.get(key)) {
        null -> value
        is T -> value
        else -> throw RuntimeException("type not match")
    }
}

inline fun <reified T> Activity.intent(key: String, crossinline default: () -> T) = lazy(LazyThreadSafetyMode.NONE) {
    when (val value = intent?.extras?.get(key)) {
        null -> default.invoke()
        is T -> value
        else -> throw RuntimeException("type not match")
    }
}

fun getBasicValue(clazz: Class<*>): Any? {
    if (clazz == Int::class.java || clazz == Int::class.javaPrimitiveType) {
        return 0
    } else if (clazz == Boolean::class.java || clazz == Boolean::class.javaPrimitiveType) {
        return false
    } else if (clazz == Float::class.java || clazz == Float::class.javaPrimitiveType) {
        return 0f
    }
    return null
}
