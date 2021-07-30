package com.gh.libbase.interfaces;


import com.gh.libbase.ui.BaseFragment;

public interface ContextFragmentAction extends ContextAction {
    BaseFragment getThisFragment();
}
