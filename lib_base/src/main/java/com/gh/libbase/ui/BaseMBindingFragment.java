package com.gh.libbase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gh.libbase.live.AbsViewModel;

import org.jetbrains.annotations.NotNull;

public abstract class BaseMBindingFragment<V extends ViewDataBinding, VM extends AbsViewModel> extends  BaseMFragment<VM> {
    protected V binding;
    private int viewModelId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

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
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.unbind();
        }
    }


}
