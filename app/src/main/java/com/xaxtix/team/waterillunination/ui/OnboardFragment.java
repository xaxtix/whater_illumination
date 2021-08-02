package com.xaxtix.team.waterillunination.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xaxtix.team.waterillunination.App;
import com.xaxtix.team.waterillunination.ColorUtilites;
import com.xaxtix.team.waterillunination.R;
import com.xaxtix.team.waterillunination.base.BaseFragment;
import com.xaxtix.team.waterillunination.ui.viewpager.ViewPagerAdapter;

public class OnboardFragment extends BaseFragment {

    int startColor = ContextCompat.getColor(App.context, R.color.background);
    int endColor = ContextCompat.getColor(App.context, R.color.background_end);

    int currentPage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = LayoutInflater.from(getActivity()).inflate(R.layout.onboarding_fragment, container, false);
        TextView button = parent.findViewById(R.id.button);
        ViewPager v = parent.findViewById(R.id.view_pager);
        v.setOverScrollMode(View.OVER_SCROLL_NEVER);
        v.setAdapter(new ViewPagerAdapter() {
            @Override
            protected View createView(int position, ViewGroup parent) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_board, parent, false);
                ImageView iv = v.findViewById(R.id.image);
                TextView tv = v.findViewById(R.id.text);
                switch (position) {
                    case 0:
                        iv.setImageResource(R.drawable.icon_1);
                        tv.setText(R.string.d1);
                        break;
                    case 1:
                        iv.setImageResource(R.drawable.icon_2);
                        tv.setText(R.string.d2);
                        break;
                    case 2:
                        iv.setImageResource(R.drawable.icon_3);
                        tv.setText(R.string.d3);
                        break;
                    case 3:
                        iv.setImageResource(R.drawable.icon_4);
                        tv.setText(R.string.d4);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                        tv.setLines(4);
                        break;
                }
                return v;
            }


            @Override
            public int getCount() {
                return 4;
            }


        });

        currentPage = 0;

        v.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                parent.setBackgroundColor(ColorUtilites.blend(endColor, startColor, (i + v) / 3f));
            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
                button.setText(i != 3 ? R.string.next : R.string.begin);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        button.setText(currentPage != 3 ? R.string.next : R.string.begin);



        button.setOnClickListener(v1 ->  {
            if(currentPage == 3){
                nextFragmet(new ActionFragment());
            } else {
                v.setCurrentItem(++currentPage);
            }
        });
        return parent;
    }

}
