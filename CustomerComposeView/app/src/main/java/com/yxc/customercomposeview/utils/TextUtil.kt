package com.yxc.customercomposeview.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.yxc.customercomposeview.utils.AppUtil.app
import com.yxc.customercomposeview.utils.DisplayUtil.sp2px
import java.util.*

/**
 * @author yxc
 * @since 2019/3/7
 */
object TextUtil {
    fun getSpannableStr(
        context: Context, texSize: Float,
        parentStr: String, vararg strArgs: String
    ): SpannableStringBuilder {
        return getSpannableStr(context, texSize, false, parentStr, *strArgs)
    }

    fun getSpannableStr(
        context: Context, texSize: Float,
        isBold: Boolean, parentStr: String, vararg strArgs: String
    ): SpannableStringBuilder {
        return getSpannableStr(context, texSize, isBold, null, parentStr, *strArgs)
    }

    fun getSpannableStr(
        context: Context, texSize: Float,
        isBold: Boolean,
        colorSpan: ForegroundColorSpan?, parentStr: String, vararg strArgs: String
    ): SpannableStringBuilder {
        val spannable = SpannableStringBuilder(parentStr)
        var styleSpan = StyleSpan(Typeface.NORMAL) //粗体
        var fromIndex = 0
        for (subStr in strArgs) {
            val sizeSpan = AbsoluteSizeSpan(
                sp2px(
                    context!!,
                    texSize
                )
            )
            val subBeginIndex = parentStr.indexOf(subStr, fromIndex)
            if (subBeginIndex == -1) {
                continue
            }
            val subEndIndex = subBeginIndex + subStr.length
            spannable.setSpan(
                sizeSpan, subBeginIndex, subEndIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (isBold) {
                styleSpan = StyleSpan(Typeface.BOLD)
            }
            spannable.setSpan(
                styleSpan, subBeginIndex, subEndIndex,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
            if (null != colorSpan) {
                spannable.setSpan(
                    colorSpan, subBeginIndex, subEndIndex,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
            fromIndex = subEndIndex
        }
        return spannable
    }

    fun getSpannableWithTypeface(
        typeface: Typeface?,
        context: Context?, texSize: Float,
        colorSpan: ForegroundColorSpan?, parentStr: String, vararg strArgs: String
    ): SpannableStringBuilder {
        val spannable = SpannableStringBuilder(parentStr)
        var fromIndex = 0
        for (subStr in strArgs) {
            val customTypefaceSpan = CustomTypefaceSpan(typeface)
            val sizeSpan = AbsoluteSizeSpan(
                sp2px(
                    context!!,
                    texSize
                )
            )
            val subBeginIndex = parentStr.indexOf(subStr, fromIndex)
            if (subBeginIndex == -1) {
                continue
            }
            val subEndIndex = subBeginIndex + subStr.length
            spannable.setSpan(
                sizeSpan, subBeginIndex, subEndIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                customTypefaceSpan, subBeginIndex, subEndIndex,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
            if (null != colorSpan) {
                spannable.setSpan(
                    colorSpan, subBeginIndex, subEndIndex,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
            fromIndex = subEndIndex
        }
        return spannable
    }

    fun getSpannableWithTypeface(
        color: Int, parentStr: String,
        vararg strArgs: String
    ): SpannableStringBuilder {
        val spannable = SpannableStringBuilder(parentStr)
        val colorSpan = ForegroundColorSpan(color)
        var fromIndex = 0
        for (subStr in strArgs) {
            val subBeginIndex = parentStr.indexOf(subStr, fromIndex)
            if (subBeginIndex == -1) {
                continue
            }
            val subEndIndex = subBeginIndex + subStr.length
            if (null != colorSpan) {
                spannable.setSpan(
                    colorSpan, subBeginIndex, subEndIndex,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
            fromIndex = subEndIndex
        }
        return spannable
    }

    fun getSpannableStr(
        context: Context, texSize: Int,
        colorSpan: ForegroundColorSpan, parentStr: String, vararg strArgs: String
    ): SpannableStringBuilder {
        return getSpannableStr(context, texSize.toFloat(), true, colorSpan, parentStr, *strArgs)
    }

    fun getTxtHeight1(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return fontMetrics.bottom - fontMetrics.top
    }

    fun getTxtHeight2(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return fontMetrics.descent - fontMetrics.ascent
    }

    fun getTextBaseY(rectF: RectF, paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return rectF.centerY() - fontMetrics.top / 2 - fontMetrics.bottom / 2
    }

    @JvmStatic
    fun setTypeface(paint: Paint, typefaceResource: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && typefaceResource != 0) {
            val context = app
            val typeface = context.resources.getFont(typefaceResource)
            paint.typeface = typeface
        }
    }

    fun changeIntToStr(value: Int): String {
        return String.format(Locale.getDefault(), "%d", value)
    }
}