package com.gh.libbase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gh.libbase.R
import me.yokeyword.fragmentation.SupportFragment


open class CommonActivity : BaseActivity() {
    companion object {
        const val EXTRA_FRAGMENT = "fragmentName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        initView()
    }

    protected fun initView() {
        val intent = intent
        try {
            val fragmentClazz = intent.getStringExtra(EXTRA_FRAGMENT)
            val fragment = Class.forName(fragmentClazz).newInstance() as Fragment
            fragment.arguments = intent.extras
            if (fragment is SupportFragment) {
                loadRootFragment(R.id.common_frame, fragment)
            } else {
                toast("页面加载类型错误，请稍后重试！")
            }
        } catch (e: Exception) {
            toast("页面加载失败，请稍后重试！")
        }
    }
}