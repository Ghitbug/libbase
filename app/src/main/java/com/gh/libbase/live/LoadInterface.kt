package com.gh.libbase.live

open interface LoadInterface {
    fun dataObserver()

    fun showSuccess(state: Int, msg: String)

    fun showLoading()

    fun showError(state: Int, msg: String)

    fun getClassName(): String?

    fun getStateEventKey(): String?
}