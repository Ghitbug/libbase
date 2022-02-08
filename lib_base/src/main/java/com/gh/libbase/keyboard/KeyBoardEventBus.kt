package com.gh.libbase.keyboard

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.util.Log
import android.view.View
import java.util.*

/**
 * 用于管理键盘事件
 *
 * @author wcy
 */
class KeyBoardEventBus {
    /**
     * TAG
     */
    private val TAG = "KeyBoardEventBus"

    /**
     * 用于缓存监听回调信息
     */
    private val callbackCache: MutableMap<Any, GlobalLayoutListenerImp> = HashMap()

    /**
     * 状态栏高度
     */
    private var statusBarHeight = -1

    /**
     * 全屏时的高度
     */
    private var fullScreenHeight = -1



    /**
     * 用于注册键盘监听，此方法适用于 View、Dialog、Fragement(v4、app)、FragementActivity、Activity
     *
     * @param object 需要监听的类（）
     */
    fun register(`object`: Any?) {
        //获取失败则直接停止，反之进行注册
        val activity = getActivity(`object`)
        if (activity == null) {
            print("获取activity失败！")
            return
        }
        register(activity, `object`)
    }

    /**
     * 此方法区别于 [.register] ,之前的方法会限制注册的类型，当前的不会限制类型
     *
     * @param activity 宿主activity
     * @param object   监听的类
     */
    fun register(activity: Activity?, `object`: Any?) {
        if (activity == null || `object` == null) {
            print("activity或object为null!")
            return
        }
        var imp = callbackCache[activity]
        if (imp == null) {
            imp = GlobalLayoutListenerImp(activity)
        }
        //移除对应的回调
        imp.addCallback(`object`)

        //如果不是空
        if (!imp.isEmpty()) {
            //设置监听
            activity.window.decorView.viewTreeObserver.addOnGlobalLayoutListener(imp)
            //缓存
            callbackCache[activity] = imp
        }
    }

    /**
     * 反注册
     *
     * @param object 取消监听的类
     */
    fun unregister(`object`: Any?) {
        //获取失败则直接停止，反之进行反注册
        val activity = getActivity(`object`)
        if (activity == null) {
            print("获取activity失败！")
            return
        }
        unregister(activity, `object`)
    }

    /**
     * 反注册
     *
     * @param activity 宿主activity
     * @param object   监听的类
     */
    fun unregister(activity: Activity?, `object`: Any?) {
        if (activity == null || `object` == null) {
            print("activity或object为null!")
            return
        }
        val imp = callbackCache[activity] ?: return
        //移除对应的回调
        imp.removeCallback(`object`)

        //如果回调集合为空则直接移除
        if (imp.isEmpty()) {
            //去掉监听
            activity.window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(imp)
            //释放监听内缓存、引用
            imp.release()
            //释放缓存
            callbackCache.remove(activity)
        }
    }


    /**
     * 获取对应View、Dialog、Fragement(v4、app)、FragementActivity、Activity 的Activity
     * (如果Object为null或者不是支持的类型则返回null)
     *
     * @param object 需要获取的类
     * @return 返回对应的activity
     */
    fun getActivity(`object`: Any?): Activity? {
        if (`object` == null) {
            return null
        }
        if (`object` is Activity) {
            return `object`
        } else if (`object` is Fragment) {
            return `object`.activity
        } else if (`object` is Fragment) {
            return `object`.activity
        } else if (`object` is Dialog) {
            return `object`.context as Activity
        } else if (`object` is View) {
            return `object`.context as Activity
        }
        return null
    }

    /**
     * 用于打印信息
     *
     * @param info 待打印的内容
     */
    private fun print(info: String) {
        Log.e(TAG, info)
    }

    /**
     * 用于获取状态栏高度
     *
     * @return 状态栏高度
     */
    fun getStatusBarHeight(activity: Activity): Int {
        if (statusBarHeight == -1) {
            statusBarHeight = realGetStatusBarHeight(activity)
            return statusBarHeight
        }
        return statusBarHeight
    }

    /**
     * 使用反射获取状态栏高度
     *
     * @return 状态栏高度
     */
    private fun realGetStatusBarHeight(activity: Activity): Int {
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = field[obj].toString().toInt()
            return activity.resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * 用于获取全屏时的整体高度
     *
     * @return 屏幕高度
     */
    fun getFullScreenHeight(activity: Activity): Int {
        if (fullScreenHeight == -1) {
            fullScreenHeight = realGetFullScreenHeight(activity)
            return fullScreenHeight
        }
        return fullScreenHeight
    }

    /**
     * 用于获取全屏高度
     *
     * @return 屏幕高度
     */
    private fun realGetFullScreenHeight(activity: Activity): Int {
        val wm = activity.windowManager
        return wm.defaultDisplay.height
    }
}