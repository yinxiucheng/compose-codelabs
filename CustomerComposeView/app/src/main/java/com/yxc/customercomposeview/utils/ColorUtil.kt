package com.yxc.customercomposeview.utils

import android.content.Context
import android.os.Build

object ColorUtil {

    fun getColor(context: Context, colorResourceId:Int):Int{
       return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(colorResourceId, null)
        }else{
            context.resources.getColor(colorResourceId)
        }

    }
}