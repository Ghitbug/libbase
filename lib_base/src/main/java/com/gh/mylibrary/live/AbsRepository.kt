package com.gh.mylibrary.live

import android.content.Context
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


open class AbsRepository {
    var fragmentName = ""
    private var mCompositeSubscription: CompositeDisposable? = null
    private var mContext: Context? = null

    var loadState: MutableLiveData<String> ?= null

    init {
        loadState = MutableLiveData()
    }



    fun addDisposable(disposable: Disposable) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeDisposable()
        }
        disposable.let {
            mCompositeSubscription?.add(it)
        }
    }

    open fun unSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription!!.isDisposed) {
            mCompositeSubscription?.clear()
            mCompositeSubscription = null
        }
    }


    /**
     * ViewModel销毁时清除Model，与ViewModel共消亡。Model层同样不能持有长生命周期对象
     */
    open fun onCleared(){

    }



}