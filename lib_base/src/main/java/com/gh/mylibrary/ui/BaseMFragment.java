package com.gh.mylibrary.ui;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import com.gh.mylibrary.live.AbsViewModel;
import com.gh.mylibrary.live.BaseListVo;
import com.gh.mylibrary.live.LoadInterface;
import com.gh.mylibrary.live.LoadObserver;
import com.gh.mylibrary.utils.ParameterizedTypeUtil;


/**
 * BaseMFragment
 *
 * @version 4.0.0
 * @auth GH
 * @time 2019/10/21
 * @description applibrary
 */
public class BaseMFragment<VM extends AbsViewModel> extends BaseFragment implements LoadInterface {
    protected VM mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
    }

    public VM initViewModel() {
        mViewModel = ParameterizedTypeUtil.VMProviders(this);
        if (null != mViewModel && !mViewModel.getClass().getSimpleName().equals(AbsViewModel.class.getSimpleName())) {
            mViewModel.setFragmentName(getClassName());
            mViewModel.getLoadState().observe(this, new LoadObserver(this));

        }
        return mViewModel;
    }



    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel == null) {
            initViewModel();
            initDataObserver();
        }
    }


    @Override
    public void finishFramager() {
        if (mViewModel != null) mViewModel.unSubscribe();
        mViewModel = null;
        super.finishFramager();
    }

    protected <M> MutableLiveData<M> registerObserver(Class<M> tClass) {
        return registerObserver(tClass, "");
    }

    protected <M> MutableLiveData<M> registerObserver(Class<M> tClass, String tag) {
        String event = getClassName().concat(tClass.getSimpleName());
        event = event.concat(tag);
        return mViewModel.putLiveBus(event);
    }

    protected <M> MutableLiveData<BaseListVo<M>> registerObservers(Class<M> tClass) {
        String event = getClassName().concat(tClass.getSimpleName()).concat("list");
        return mViewModel.putLiveBus(event);
    }

    protected <M> MutableLiveData<BaseListVo<M>> registerObservers(Class<M> tClass, String tag) {
        String event = getClassName().concat(tClass.getSimpleName()).concat("list");
        event = event.concat(tag);
        return mViewModel.putLiveBus(event);
    }

    @Override
    public void initDataObserver() {

    }

    @Override
    public void showSuccess(int state, String msg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(int state, String msg) {
        toast(msg);
    }

    @Override
    public String getClassName() {
        return getClass().getSimpleName() + getStateEventKey();
    }

    @Override
    public String getStateEventKey() {
        return "";
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }
}
