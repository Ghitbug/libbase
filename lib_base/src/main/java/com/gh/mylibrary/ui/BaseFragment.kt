package com.gh.mylibrary.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.gh.mylibrary.R
import com.gh.mylibrary.interfaces.*
import com.gh.mylibrary.utils.RxActivityTool
import com.gh.mylibrary.utils.RxFragmentUtil
import com.gh.mylibrary.view.HeaderLayout
import com.gyf.immersionbar.ImmersionBar
import me.yokeyword.fragmentation.SupportFragment


open class BaseFragment : SupportFragment(), ClickAction, BundleAction, ResourcesAction,
    HandlerAction, ContextFragmentAction {
    var headerLayout: HeaderLayout? = null
    var rootView: View? = null
    var mImmersionBar: ImmersionBar? = null
    open fun setCreatedLayoutViewId(): Int {
        return 0
    }

    open fun setTitle(): String? {
        return ""
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)
        thisActivity!!.finishInputWindow()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return setView(inflater, setCreatedLayoutViewId())
    }

    open fun finishFramager() {
        setFragmentResult(0, null)
        if (RxFragmentUtil.getChildFragment(activity!!.supportFragmentManager) <= 1) {
            RxActivityTool.finishActivity(activity)
        } else {
            pop()
        }
        thisActivity!!.finishInputWindow()
    }

    open fun finishActivity() {
        RxActivityTool.finishActivity(activity)
    }

    /**
     * fragment切换
     *
     * @param toFragment 目标fragment
     * @param bundle     参数
     */
    open fun startFragment(toFragment: BaseFragment, bundle: Bundle?) {
        toFragment.arguments = bundle
        start(toFragment)
    }

    open fun startFragmentForResult(toFragment: BaseFragment, bundle: Bundle?, requestCode: Int) {
        toFragment.arguments = bundle
        startForResult(toFragment, requestCode)
    }

    /**
     * 跳转到一个新的activiy的fragment
     *
     * @param cl
     * @param bundle
     */
    open fun startActivityToFragment(cl: Class<*>?, bundle: Bundle?) {
        RxFragmentUtil.startFragment(context, cl, bundle)
    }


    /**
     * 跳转到一个新的activiy带返回的fragment
     *
     * @param cl
     * @param bundle
     * @param requestCode
     */
    open fun startActivityToFragmentForResult(cl: Class<*>?, bundle: Bundle?, requestCode: Int?) {
        RxFragmentUtil.startFragmentForResult(this, cl, bundle, requestCode)
    }

    open fun toast(obj: Any?) {
        thisActivity!!.toast(obj)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (headerLayout != null) {
            headerLayout?.navigationView?.setOnClickListener {
                finishFramager()
            }
        }
        initImmersionBar()
        initView()
        setAnnotation()
    }

    private fun setAnnotation() {
        var ids = setOnClickListenerIds()
        if (ids != null) setOnClickListener(*(ids!!))
    }

    open fun setOnClickListenerIds(): IntArray? {
        return null
    }

    fun isAPP(): Boolean {
        return false
    }

    fun setView(inflater: LayoutInflater, layoutId: Int): View? {
        return setView(inflater, layoutId, null)
    }

    fun setView(inflater: LayoutInflater, layoutId: Int, title: String?): View? {
        return setView(inflater, null, layoutId, title)
    }

    override fun onStart() {
        super.onStart()
        if (thisActivity != null) {
            //告诉FragmentActivity，当前Fragment在栈顶
            thisActivity!!.setSelectedFragment(this)
        }
    }

    fun setView(inflater: LayoutInflater, container: ViewGroup?, layoutId: Int, title: String?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(layoutId, container, false)
            headerLayout = rootView?.findViewById(R.id.headerlayout)
            if (headerLayout != null) headerLayout?.setTitleText(setTitle())
            initTitle(title)
        }
        return rootView
    }


    open fun initTitle(title: String?) {}

    protected open fun initView() {}


    /**
     * Back Event
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    override fun onBackPressedSupport(): Boolean {
        return if (parentFragment != null) {
            setFragmentResult(0, null)
            super.onBackPressedSupport()
        } else {
            finishFramager()
            true
        }
    }

    /**
     * 初始化沉浸式
     */
    protected open fun initImmersionBar() {
        mImmersionBar = ImmersionBar.with(thisActivity)
        try {
            if (getTitleId() != null) {
                if (getTitleId() is View) {
                    mImmersionBar!!.titleBar(getTitleId() as View)
                } else {
                    mImmersionBar!!.titleBar(getTitleId().toString().toInt())
                }
            } else if (headerLayout != null) {
                mImmersionBar!!.titleBar(headerLayout)
            }
            if (isStatusBarDarkFont()) {
                mImmersionBar!!.statusBarDarkFont(true, 0.2f)
            }
            mImmersionBar!!.init()
        } catch (e: Exception) {
        }
    }

    /**
     * 设置状态栏沉浸式 返回内容只能是id和view
     *
     * @return
     */
    open fun getTitleId(): Any? {
        return null
    }

    /**
     * 状态栏字体深色或亮色，判断设备支不支持状态栏变色来设置状态栏透明度
     *
     * @return
     */
    open fun isStatusBarDarkFont(): Boolean {
        return false
    }

    override fun onDestroy() {
        try {
            if (mImmersionBar != null) ImmersionBar.destroy(thisFragment)
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

    open fun toastError(obj: Any?) {
        thisActivity.toastError(obj)
    }

    open fun toastSuccess(obj: Any?) {
        thisActivity.toastSuccess(obj)
    }

    fun loadMultipleRootFragment(containerId: Int, showPosition: Int, fragments: List<BaseFragment>) {
        val supportFragments = arrayOfNulls<SupportFragment>(fragments.size)
        for (i in fragments.indices) {
            supportFragments[i] = fragments[i]
        }
        loadMultipleRootFragment(containerId, showPosition, *supportFragments)
    }

    override fun getContext(): Context {
        return thisActivity.context
    }



    override fun getThisActivity(): BaseActivity {
        return _mActivity as BaseActivity
    }

    override fun getThisFragment(): BaseFragment {
        return this
    }

    override fun getBundle(): Bundle? {
        return arguments
    }

    override fun <V : View> findViewById(@IdRes id: Int): V? {
        return rootView?.findViewById(id)
    }

}