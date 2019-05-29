package com.appttude.h_mal.days_left.Global;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.appttude.h_mal.days_left.R;

import static com.appttude.h_mal.days_left.MainActivity.fragmentManager;

public class ExecuteFragment {

    public static void executeFragment(Fragment fragment, Bundle bundle) {
        executeFragmentMethod(fragment,bundle);
    }

    public static void executeFragment(Fragment fragment) {
        executeFragmentMethod(fragment);
    }

    private static void executeFragmentMethod(Fragment f){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,f).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(f.getClass().getSimpleName()).commit();
    }

    private static void executeFragmentMethod(Fragment f, Bundle b){
        if (b != null){
            f.setArguments(b);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,f).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(f.getClass().getSimpleName()).commit();
    }
}
