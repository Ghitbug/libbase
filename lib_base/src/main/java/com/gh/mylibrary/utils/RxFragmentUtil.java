package com.gh.mylibrary.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.gh.mylibrary.ui.BaseFragment;
import com.gh.mylibrary.ui.CommonActivity;

import java.util.List;

public class RxFragmentUtil {
    public static void startFragment(Context context, Class cl) {
        startFragment(context, cl, null);
    }

    public static void startFragment(Context context, Class cl, Bundle bundle) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(CommonActivity.EXTRA_FRAGMENT, cl.getName());
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startFragmentForResult(Fragment fragment, Class cl, Bundle bundle, Integer requestCode) {

        Intent intent = new Intent(fragment.getContext(), CommonActivity.class);
        intent.putExtra(CommonActivity.EXTRA_FRAGMENT, cl.getName());
        if (bundle != null)
            intent.putExtras(bundle);
        fragment.startActivityForResult(intent, requestCode);
    }
    public static void startFragmentForResult(AppCompatActivity activity, Class cl, Bundle bundle, Integer requestCode) {

        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(CommonActivity.EXTRA_FRAGMENT, cl.getName());
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }
    public static int getChildFragment(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        int count = 0;
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseFragment) count++;
        }
        return count;
    }
}
