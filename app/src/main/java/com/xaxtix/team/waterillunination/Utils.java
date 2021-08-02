package com.xaxtix.team.waterillunination;

public class Utils {

    public static int dp(float f) {
        float scale = App.context.getResources().getDisplayMetrics().density;
        return (int) (f * scale + 0.5f);
    }
}
