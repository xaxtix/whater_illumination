package com.xaxtix.team.waterillunination;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.xaxtix.team.waterillunination.ui.OnboardFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);

        frameLayout.setFitsSystemWindows(true);
        setContentView(frameLayout);
        ViewCompat.setOnApplyWindowInsetsListener(frameLayout, (view, windowInsetsCompat) -> windowInsetsCompat);
        frameLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        frameLayout.setId(R.id.content);


        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        if (f == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, new OnboardFragment())
                    .commit();

        }

    }


    public void nextFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.sline_in,R.animator.slide_out)
                .replace(R.id.content, f)
                .commit();
    }


}
