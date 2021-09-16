package com.gh.mylibrary.interfaces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/09/15
 * desc   : Context 意图处理（扩展非 Context 类的方法，禁止 Context 类实现此接口）
 */
public interface ResourcesAction {

    /**
     * 获取 Context
     */
    Context getContext();

    /**
     * 获取资源对象（仅供子类调用）
     */
    default Resources getAResources() {
        return getContext().getResources();
    }

    /**
     * 根据 id 获取一个文本
     */
    default String getAString(@StringRes int id) {
        return getContext().getString(id);
    }

    default String getAString(@StringRes int id, Object... formatArgs) {
        return getAResources().getString(id, formatArgs);
    }

    /**
     * 根据 id 获取一个 Drawable
     */
    default Drawable getADrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    /**
     * 根据 id 获取一个颜色
     */
    @ColorInt
    default int getAColor(@ColorRes int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    /**
     * 根据 id
     */
    @ColorInt
    default int[] getAIntArry(int id) {
        return getContext().getResources().getIntArray(id);
    }

    /**
     * 获取系统服务
     */
    default <S> S getASystemService(@NonNull Class<S> serviceClass) {
        return ContextCompat.getSystemService(getContext(), serviceClass);
    }
}