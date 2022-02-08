package com.gh.libbase.live

import android.text.TextUtils
import androidx.lifecycle.Observer
import com.gh.libbase.interfaces.StateConstants

class LoadObserver(loadInterface: LoadInterface) : Observer<String> {
    var loadInterface: LoadInterface? = null

    init {
        this.loadInterface = loadInterface
    }

    override fun onChanged(result: String) {
        if (!TextUtils.isEmpty(result)) {
            val str: Array<String> = result.split("@".toRegex()).toTypedArray()
            if (str != null) {
                val state = str[0].toInt()
                if (StateConstants.ERROR_STATE == state) {
                    val stateT = str[1].toInt()
                    if (str.size > 2) {
                        loadInterface!!.showError(stateT, str[2])
                    } else {
                        loadInterface!!.showError(stateT, "操作失败")
                    }
                } else if (StateConstants.LOADING_STATE == state) {
                    loadInterface!!.showLoading()
                } else if (StateConstants.SUCCESS_STATE == state) {
                    val stateT = str[1].toInt()
                    if (str.size > 2) {
                        loadInterface!!.showSuccess(stateT, str[2])
                    } else {
                        loadInterface!!.showSuccess(stateT, "")
                    }
                }
            }
        }
    }
}