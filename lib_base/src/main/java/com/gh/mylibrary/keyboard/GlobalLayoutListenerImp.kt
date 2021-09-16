package com.gh.mylibrary.keyboard

import android.app.Activity
import android.graphics.Rect
import android.view.ViewTreeObserver.OnGlobalLayoutListener

/**
 * 用于真正的监听布局变化的回调类
 *
 * @author wcy
 */
class GlobalLayoutListenerImp(activity: Activity) : OnGlobalLayoutListener {
    var activity: Activity?=null
    var ikeyBoardCallbackList: ArrayList<IkeyBoardCallback>?=null
    private val NONE = -1
    private val SHOW = 1
    private val HIDDEN = 2
    private var status = NONE
    init {
        this.activity = activity
        ikeyBoardCallbackList = arrayListOf()
    }

    override fun onGlobalLayout() {
        //activity为null不执行

        //activity为null不执行
        if (activity == null) {
            return
        }
        //获取可视范围
        //获取可视范围
        val r = Rect()
        activity!!.window.decorView.getWindowVisibleDisplayFrame(r)
        //获取屏幕高度
        //获取屏幕高度
        val screenHeight = KeyBoardEventBus().getFullScreenHeight(activity!!)
        //获取状态栏高度
        //获取状态栏高度
        val statusBarHeight = KeyBoardEventBus().getStatusBarHeight(activity!!)

        //获取被遮挡高度（键盘高度）(屏幕高度-状态栏高度-可视范围)

        //获取被遮挡高度（键盘高度）(屏幕高度-状态栏高度-可视范围)
        val keyBoardHeight = screenHeight - statusBarHeight - r.height()

        //显示或者隐藏

        //显示或者隐藏
        val isShowKeyBoard = keyBoardHeight >= screenHeight / 3

        //当首次或者和之前的状态不一致的时候会回调，反之不回调(用于当状态变化后才回调，防止多次调用)

        //当首次或者和之前的状态不一致的时候会回调，反之不回调(用于当状态变化后才回调，防止多次调用)
        if (status == NONE || isShowKeyBoard && status == HIDDEN || !isShowKeyBoard && status == SHOW) {
            if (isShowKeyBoard) {
                status = SHOW
                dispatchKeyBoardShowEvent(keyBoardHeight)
            } else {
                status = HIDDEN
                dispatchKeyBoardHiddenEvent()
            }
        }
    }

    /**
     * 添加监听回调
     *
     * @param callback 监听的回调类
     */
    fun addCallback(callback: Any?) {
        if (ikeyBoardCallbackList == null || callback !is IkeyBoardCallback) {
            return
        }
        ikeyBoardCallbackList?.add(callback)
    }

    /**
     * 移除监听回调
     *
     * @param callback 监听的回调类
     */
    fun removeCallback(callback: Any?) {
        if (ikeyBoardCallbackList == null) {
            return
        }
        ikeyBoardCallbackList?.remove(callback)
    }

    /**
     * 判断是不是没有监听回调
     *
     * @return true:空 false:不空
     */
    fun isEmpty(): Boolean {
        return ikeyBoardCallbackList?.isEmpty() ?: true
    }

    /**
     * 清除内部内存引用
     */
    fun release() {
        status = NONE
        activity = null
        ikeyBoardCallbackList?.clear()
        ikeyBoardCallbackList = null
    }

    /**
     * 分发隐藏事件
     */
    private fun dispatchKeyBoardHiddenEvent() {
        if (ikeyBoardCallbackList == null) {
            return
        }
        for (callback in ikeyBoardCallbackList!!) {
            callback.onKeyBoardHidden()
        }
    }

    /**
     * 分发显示事件
     */
    private fun dispatchKeyBoardShowEvent(keyBoardHeight: Int) {
        if (ikeyBoardCallbackList == null) {
            return
        }
        for (callback in ikeyBoardCallbackList!!) {
            callback.onKeyBoardShow(keyBoardHeight)
        }
    }

}