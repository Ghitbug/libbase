package com.gh.mylibrary.live

open interface LoadInterface {

    /**
     * 初始化界面传递参数
     */
    fun initParam()

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    fun initDataObserver()

    /**
     * 初始化界面观察者的监听
     */
    fun initViewObservable()

    fun showSuccess(state: Int, msg: String)

    fun showLoading()

    fun showError(state: Int, msg: String)

    fun getClassName(): String?

    fun getStateEventKey(): String?
}