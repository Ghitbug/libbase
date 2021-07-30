package com.gh.libbase.live

import android.content.Context
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


open class AbsRepository {
    var fragmentName = ""
    private var mCompositeSubscription: CompositeDisposable? = null
    private var mContext: Context? = null
    var viewModel: ViewModel? = null




    fun addDisposable(disposable: Disposable) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeDisposable()
        }
        disposable.let {
            mCompositeSubscription!!.add(it)
        }
    }

    open fun unSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription!!.isDisposed) {
            mCompositeSubscription?.clear()
            mCompositeSubscription = null
        }
    }

    fun setmContext(mContext: Context?) {
        this.mContext = mContext
    }

    fun getmContext(): Context? {
        return mContext
    }

}