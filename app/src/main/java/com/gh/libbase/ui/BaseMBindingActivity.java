package com.gh.libbase.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gh.libbase.live.AbsViewModel;

public abstract class BaseMBindingActivity<V extends ViewDataBinding, VM extends AbsViewModel> extends  BaseMActivity<VM> {
    protected V binding;
    private int viewModelId;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面接受的参数方法
        initParam();
        //初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);

        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
    }

    private void initViewDataBinding(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));
        viewModelId = initVariableId();
        mViewModel = initViewModel();
        if(mViewModel==null){
            mViewModel=super.initViewModel();
        }

        //关联ViewModel
        binding.setVariable(viewModelId, mViewModel);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(mViewModel);
        //注入RxLifecycle生命周期
        mViewModel.injectLifecycleProvider(this);

    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    @Override
    public VM initViewModel() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(binding != null){
            binding.unbind();
        }
    }
}
