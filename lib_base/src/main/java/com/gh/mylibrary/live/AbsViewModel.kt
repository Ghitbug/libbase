package com.gh.mylibrary.live

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.gh.mylibrary.interfaces.StateConstants
import com.gh.mylibrary.utils.ParameterizedTypeUtil
import com.gh.mylibrary.utils.RxDataTool
import com.trello.rxlifecycle4.LifecycleProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import java.lang.ref.WeakReference
import java.util.*

open class AbsViewModel<T : AbsRepository>(application: Application) : AndroidViewModel(application),
    ILifecycleViewModel,
    Consumer<Disposable> {

    var mLiveBus: HashMap<String, MutableLiveData<Any>> = hashMapOf()
    var loadState: MutableLiveData<String> = MutableLiveData()
    private var fragmentName = ""
    var mRepository: T = ParameterizedTypeUtil.getNewInstance(this, 0)

    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    var mCompositeDisposable: CompositeDisposable? = null

    //弱引用持有
    var lifecycle: WeakReference<LifecycleProvider<*>>? = null

    init {
        mCompositeDisposable = CompositeDisposable()
        /*mRepository?.let {
            it.setmContext(RxActivityTool.currentActivity())
             mRepository!!.viewModel = this
        }*/
    }

    protected open fun addSubscribe(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(disposable)
    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    open fun injectLifecycleProvider(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = WeakReference<LifecycleProvider<*>>(lifecycle)
    }

    open fun getLifecycleProvider(): LifecycleProvider<*>? {
        return lifecycle?.get()
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    @Throws(Exception::class)
    override fun accept(disposable: Disposable?) {
        addSubscribe(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        mRepository?.onCleared()
        //ViewModel销毁时会执行，同时取消所有异步任务
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.clear()
        }
    }


    fun unSubscribe() {
        mRepository?.unSubscribe()
    }



    /**
     * 封装错误返回信息
     *
     * @param
     * @return
     */
    fun getStateError(state: Int, msg: String?): String? {
        val stringBuffer = StringBuffer()
        stringBuffer.append(StateConstants.ERROR_STATE).append("@").append(state).append("@").append(msg ?: "操作失败")
        return stringBuffer.toString()
    }

    fun getStateError(): String? {
        val stringBuffer = StringBuffer()
        stringBuffer.append(StateConstants.ERROR_STATE).append("@").append("1").append("@").append("操作失败")
        return stringBuffer.toString()
    }

    fun getStateSuccess(): String? {
        val stringBuffer = StringBuffer()
        stringBuffer.append(StateConstants.SUCCESS_STATE).append("@").append(StateConstants.SUCCESS_STATE)
        return stringBuffer.toString()
    }

    fun getStateSuccess(state: Int, msg: String?): String? {
        val stringBuffer = StringBuffer()
        stringBuffer.append(StateConstants.SUCCESS_STATE).append("@").append(state).append("@").append(msg ?: "@")
        return stringBuffer.toString()
    }

    fun setFragmentName(fragmentName: String) {
        this.fragmentName = fragmentName
         mRepository?.fragmentName = fragmentName
    }



    protected fun postData(rxResult: BaseResult) {
        postData(rxResult.getResult(), rxResult.className)
    }

    protected fun postData(`object`: Any, tag: String) {
        if (!RxDataTool.isNullString(tag)) {
            if (`object` is List<*>) {
                val baseListVo: BaseListVo<*> = BaseListVo<Any?>()
                baseListVo.data = `object`
                postEvent(fragmentName + tag + "list", baseListVo)
            } else {
                postEvent(fragmentName + tag, `object`)
            }
        }
    }

    fun succeed(result: String?) {
        if (!RxDataTool.isNullString(result)) {
            loadState.postValue(getStateSuccess(1, result))
        }
    }

    fun error(e: String?) {
        if (!RxDataTool.isNullString(e)) {
            loadState.postValue(getStateError(1, e))
        }
    }

    fun putLiveBus(key: String): MutableLiveData<Any> {
        val liveData: MutableLiveData<Any> = MutableLiveData()
        mLiveBus[key] = liveData
        return liveData
    }

    fun <M> postEvent(eventKey: String?, value: M): MutableLiveData<M>? {
        val mutableLiveData = mLiveBus!![eventKey] as MutableLiveData<M>?
        mutableLiveData!!.postValue(value)
        return mutableLiveData
    }


}