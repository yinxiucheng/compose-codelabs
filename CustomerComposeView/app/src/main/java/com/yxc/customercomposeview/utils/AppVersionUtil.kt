package com.yxc.customercomposeview.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.yxc.customercomposeview.utils.AppUtil.app

object AppVersionUtil {
    private const val TAG = "AppVersionUtils"

    private var sAppVersionName: String = ""

    private var sAppVersionCode: Long = 0

    val appVersionName: String
        get() {
            if (TextUtils.isEmpty(sAppVersionName)) {
                loadAppVersionInfo()
            }
            return sAppVersionName
        }
    val appVersionCode: Long
        get() {
            if (TextUtils.isEmpty(sAppVersionName)) {
                loadAppVersionInfo()
            }
            return sAppVersionCode
        }

    fun getAppName(context: Context): String {
        return try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "getAppName", e)
            ""
        }
    }

    private fun loadAppVersionInfo() {
        try {
            val cxt = app
            val pm = cxt.packageManager
            val pkgInfo = pm.getPackageInfo(cxt.packageName, 0)
            sAppVersionName = pkgInfo.versionName
            sAppVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pkgInfo.longVersionCode
            } else {
                pkgInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "cannot find out myself")
        }
    }
}