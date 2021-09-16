package com.gh.mylibrary.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gh.mylibrary.R
import com.gh.mylibrary.interfaces.*
import com.gh.mylibrary.utils.RxActivityTool
import com.gh.mylibrary.utils.RxDataTool
import com.gh.mylibrary.utils.RxFragmentUtil
import com.gh.mylibrary.utils.RxKeyboardTool
import com.gh.mylibrary.view.HeaderLayout
import com.gh.mylibrary.view.toast.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.noober.background.BackgroundLibrary
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 *
 */
open class BaseActivity : SupportActivity(), BackHandledInterface, ClickAction, BundleAction,
    ResourcesAction, HandlerAction,ContextAction {
    var headerLayout: HeaderLayout? = null
    var mImmersionBar: ImmersionBar? = null
    var mBackHandedFragment: Fragment? = null

    open fun initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this)
        if (getTitleId() != null) {
            if (getTitleId() is View) {
                mImmersionBar?.titleBar(getTitleId() as View)
            } else {
                mImmersionBar?.titleBar(getTitleId().toString().toInt())
            }
        }
        if (isStatusBarDarkFont()) {
            mImmersionBar?.statusBarDarkFont(true, 0.2f)
        }
        mImmersionBar?.init()
    }

    open fun getTitleId(): Any? {
        return null
    }

    open fun isStatusBarDarkFont(): Boolean {
        return false
    }

    override fun setSelectedFragment(selectedFragment: Fragment?) {
        mBackHandedFragment = selectedFragment
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        BackgroundLibrary.inject(this)
        super.onCreate(savedInstanceState)
        finishInputWindow() //隐藏输入法
        RxActivityTool.addActivity(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (mBackHandedFragment != null) {
            mBackHandedFragment!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        init()
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        init()
    }

    private fun setAnnotation() {
        var ids = setOnClickListenerIds()
        if (ids != null) setOnClickListener(*(ids!!))
    }

    open fun setOnClickListenerIds(): IntArray? {
        return null
    }

    override fun getResources(): Resources? {
        val resources = super.getResources()
        val configuration = Configuration()
        configuration.setToDefaults()
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return resources
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity优先滑动退出;  false: Fragment优先滑动退出
     */
    override fun onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        // if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
        if (RxFragmentUtil.getChildFragment(supportFragmentManager) > 1) {
            pop()
        } else {
            finishActivity()
        }
    }

    open fun setView(layoutID: Int, title: String?) {
        setContentView(layoutID)
        headerLayout = findViewById(R.id.headerlayout)
        if (headerLayout != null) {
            if (!RxDataTool.isNullString(title)) headerLayout!!.setTitleText(title)
            headerLayout!!.navigationView.setOnClickListener { finishActivity() }
        }
        init()
    }

    private fun init() {
        initImmersionBar()
        setAnnotation()
    }



    //设置所有Fragment的转场动画
    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置横向(和安卓4.x动画相同)
        return DefaultHorizontalAnimator()
    }

    open fun toastError(obj: Any?) {
        showToast(obj, ToastUtils.ERROR_TYPE)
    }

    open fun toastSuccess(obj: Any?) {
        showToast(obj, ToastUtils.SUCCESS_TYPE)
    }

    open fun toast(obj: Any?) {
        showToast(obj, ToastUtils.NO)
    }


    private fun showToast(obj: Any?, type: Int) {
        if (obj != null && !RxDataTool.isEmpty(obj)) {
            ToastUtils.show(this, obj.toString(), type)
        } else {
            ToastUtils.show(this, "数据异常", ToastUtils.ERROR_TYPE)
        }
    }


    open fun startActivity(c: Class<*>, isclose: Boolean) {
        startActivity(c, isclose, null)
    }

    fun startActivity(c: Class<*>, isclose: Boolean, bundle: Bundle?) {
        val intent = Intent(context, c)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        if (isclose) {
            finishActivity()
        }
    }


    fun loadMultipleRootFragment(containerId: Int, showPosition: Int, fragments: List<BaseFragment?>) {
        val supportFragments = arrayOfNulls<SupportFragment>(fragments.size)
        for (i in fragments.indices) {
            supportFragments[i] = fragments[i]
        }
        loadMultipleRootFragment(containerId, showPosition, *supportFragments)
    }


    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent)
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun getContext(): Context {
        return this
    }



    override fun getThisActivity(): BaseActivity {
        return this
    }



    /**
     * 关闭当前activity
     */
    open fun finishActivity() {
        finishInputWindow()
        RxActivityTool.finishActivity(this)
    }

    /**
     * 关闭软键盘
     */
    open fun finishInputWindow() {
        RxKeyboardTool.hideSoftInput(this)
    }

    override fun onDestroy() {
        RxActivityTool.finishActivity(this)
        super.onDestroy()
    }
}