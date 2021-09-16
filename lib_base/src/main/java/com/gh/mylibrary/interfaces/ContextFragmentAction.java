package com.gh.mylibrary.interfaces;


import com.gh.mylibrary.ui.BaseFragment;

public interface ContextFragmentAction extends ContextAction {
    BaseFragment getThisFragment();
}
