package com.gh.libbase.interfaces;

import android.view.View;

import androidx.annotation.IdRes;

/**
 * desc   : 点击事件意图
 */
public interface ClickAction extends View.OnClickListener {

    <V extends View> V findViewById(@IdRes int id);

    @Override
    default void onClick(View v) {
        // 默认不实现，让子类实现
    }

    default void setOnClickListener(@IdRes int... ids) {
        if (ids != null) {
            for (int id : ids) {
                if (id > 0) findViewById(id).setOnClickListener(this);
            }
        }
    }

    default void setOnClickListener(View... ids) {
        for (View id : ids) {
            id.setOnClickListener(this);
        }
    }

}