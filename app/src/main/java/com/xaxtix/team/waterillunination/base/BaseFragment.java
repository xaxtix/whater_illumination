package com.xaxtix.team.waterillunination.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.xaxtix.team.waterillunination.MainActivity;

public class BaseFragment extends Fragment {

    public void nextFragmet(Fragment f){
        Activity a = getActivity();
        if(a instanceof MainActivity){
            ((MainActivity) a).nextFragment(f);
        }
    }
}
