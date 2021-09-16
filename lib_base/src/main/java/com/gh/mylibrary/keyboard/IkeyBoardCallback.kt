package com.gh.mylibrary.keyboard
/**
 * 用于接收键盘事件的回调
 *
 * @author wcy
 */
interface IkeyBoardCallback{
    /**
     * 当键盘显示时回调
     */
    fun onKeyBoardShow(keyBoardHeight: Int)

    /**
     * 当键盘隐藏时回调
     */
    fun onKeyBoardHidden()
}