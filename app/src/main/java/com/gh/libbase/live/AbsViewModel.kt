package com.gh.libbase.live

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gh.libbase.interfaces.StateConstants
import com.gh.libbase.utils.ParameterizedTypeUtil
import com.gh.libbase.utils.RxActivityTool
import com.gh.libbase.utils.RxDataTool

import java.util.*

open class AbsViewModel<T : AbsRepository>(application: Application) : AndroidViewModel(application) {
    var mLiveBus: HashMap<String, MutableLiveData<Any>> = hashMapOf()
    var loadState: MutableLiveData<String> = MutableLiveData()
    private var fragmentName = ""
    var mRepository: T = ParameterizedTypeUtil.getNewInstance(this, 0)

    init {
        if (mRepository != null) {
            mRepository!!.setmContext(RxActivityTool.currentActivity())
            mRepository!!.viewModel = this
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun unSubscribe() {
        if (mRepository != null) {
            mRepository!!.unSubscribe()
        }
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

    /**
     * 封装错误返回信息
     *
     * @param
     * @return
     */
    fun getStateError(state: Int, msg: String?): String? {
        val stringBuffer = StringBuffer()
        stringBuffer.append(StateConstants.ERROR_STATE).append("@").append(state).append("@").append(msg
                ?: "操作失败")
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
        if (mRepository != null) mRepository!!.fragmentName = fragmentName
    }

    protected fun postData(rxResult: BaseResult) {
        postData(rxResult.getResult(), rxResult.className)
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