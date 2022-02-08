package com.gh.libbase.utils

import android.content.pm.PackageManager
import android.os.Bundle

/**
 * 获取meta参数
 */
class MetaDataUtil {
    companion object {
        fun getMetaData(): Bundle? {
            try {
                val appInfo = RxTool.getContext().packageManager.getApplicationInfo(RxTool.getContext().packageName, PackageManager.GET_META_DATA)
                return appInfo.metaData
            } catch (e: Exception) {
            }
            return null
        }
        fun getString(key: String): String {
            val bundle = getMetaData()
            return if (bundle != null) bundle.getString(key, "") else ""
        }
        fun getInt(key: String): Int {
            val bundle = getMetaData()
            return bundle?.getInt(key, 0) ?: 0
        }
        fun getBoolean(key: String): Boolean {
            val bundle = getMetaData()
            return bundle?.getBoolean(key, false) ?: false
        }
    }
}