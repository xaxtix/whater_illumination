package com.xaxtix.team.waterillunination;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class AnimationUtils {

    private static final Map<View, Animator> visibilityAnims = new WeakHashMap<>();

    /**
     * Scale the input value according to the device's display density
     * @param dp Input value in density-independent pixels (dp)
     * @return Scaled value in physical pixels (px)
     */
    public static int dp(float dp){
        return Utils.dp(dp);
    }

    /**
     * Change a View's visibility with a fade-in/-out animation. If that doesn't change the actual visibility, (INVISIBLE -> GONE or VISIBLE -> VISIBLE) does nothing.
     * @param view The target view
     * @param visibility The new visibility constant, either View.VISIBLE, View.INVISIBLE, or View.GONE
     */
    public static void setVisibilityAnimated(final View view, final int visibility, @Nullable Animator.AnimatorListener listener) {
        setVisibilityAnimated(view, visibility, false, 300, listener);
    }

    public static void setVisibilityAnimated(final View view, final int visibility) {
        setVisibilityAnimated(view, visibility, false, 300, null);
    }

    public static void setVisibilityAnimated(final View view, final int visibility,
                                             final boolean scale, final int duration) {
        setVisibilityAnimated(view, visibility, scale, duration, null);
    }

    public static void setVisibilityAnimated(final View view, final int visibility,
                                             final boolean scale, final int duration,
                                             @Nullable final Animator.AnimatorListener listener) {
        if (view == null) {
            return;
        }
        boolean vis = visibility == View.VISIBLE;
        boolean viewVis = view.getVisibility() == View.VISIBLE;
        boolean scaleVis = scale
                ? view.getVisibility() == View.VISIBLE && view.getScaleX() == 1f && view.getScaleY() == 1f
                : viewVis;
        if (vis == viewVis && scaleVis == viewVis) {
            return;
        }
        if (visibilityAnims.containsKey(view)) {
            visibilityAnims.get(view).cancel();
            visibilityAnims.remove(view);
        }
        List<Animator> animators = new ArrayList<>();
        AnimatorSet anim = new AnimatorSet();
        if (vis) {
            if (scale) {
                animators.add(ObjectAnimator.ofFloat(view, View.SCALE_X, view.getScaleX() < 1 ? view.getScaleX() : 0.1f, 1));
                animators.add(ObjectAnimator.ofFloat(view, View.SCALE_Y, view.getScaleY() < 1 ? view.getScaleY() : 0.1f, 1));
            }
            animators.add(ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha() < 1 ? view.getAlpha() : 0, 1));
            anim.playTogether(animators);
            anim.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator anim) {
                    view.setVisibility(visibility);
                    if (listener != null) listener.onAnimationStart(anim);
                    //Log.i("vk", "Anim start "+anim);
                }

                public void onAnimationEnd(Animator anim) {
                    view.setVisibility(visibility);
                    //Log.i("vk", "Anim end "+anim);
                    visibilityAnims.remove(view);
                    if (listener != null) listener.onAnimationEnd(anim);
                }

                public void onAnimationCancel(Animator anim) {
                    view.setVisibility(visibility);
                    if (listener != null) listener.onAnimationCancel(anim);
                    //Log.i("vk", "Anim cancel "+anim);
                }
            });
            anim.setDuration(duration);
            visibilityAnims.put(view, anim);
            anim.start();
        } else {
            if (scale) {
                animators.add(ObjectAnimator.ofFloat(view, View.SCALE_X, 0.1f));
                animators.add(ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.1f));
            }
            animators.add(ObjectAnimator.ofFloat(view, View.ALPHA, 0));
            anim.playTogether(animators);
            anim.addListener(new AnimatorListenerAdapter() {
                boolean canceled = false;

                public void onAnimationStart(Animator anim) {
                    //Log.i("vk", "Anim start "+anim);
                    if (listener != null) listener.onAnimationStart(anim);
                }

                public void onAnimationEnd(Animator anim) {
                    visibilityAnims.remove(view);
                    if (canceled) return;
                    view.setVisibility(visibility);
                    view.setAlpha(1);
                    if (listener != null) listener.onAnimationEnd(anim);
                }

                public void onAnimationCancel(Animator anim) {
                    //Log.i("vk", "Anim cancel "+anim);
                    canceled = true;
                    if (listener != null) listener.onAnimationCancel(anim);
                }
            });
            anim.setDuration(duration);
            visibilityAnims.put(view, anim);
            anim.start();
        }
    }

    public static boolean isAnimating(View... views) {
        for (View view : views) {
            if (visibilityAnims.containsKey(view)) {
                return true;
            }
        }
        return false;
    }

    public static void cancelVisibilityAnimation(final View view){
        //if(view.getTag(R.id.tag_visibility_anim)==null) return;
        if(!visibilityAnims.containsKey(view)) return;
        visibilityAnims.get(view).cancel();
        view.setAlpha(1);
    }

    public static Point getViewOffset(@Nullable View v1, @Nullable  View v2){
        int[] p1={0,0}, p2={0,0};
        if (v1 != null && v2 != null) {
            v1.getLocationOnScreen(p1);
            v2.getLocationOnScreen(p2);
        }
        //Log.i("vk", "view 1: "+p1[0]+","+p1[1]+"; view 2: "+p2[0]+","+p2[1]);
        return new Point(p1[0]-p2[0], p1[1]-p2[1]);
    }

    public static Point getViewOffset(@Nullable View v1, @Nullable Rect r2){
        if (v1 != null && r2 != null) {
            int[] p1 = {0, 0};
            v1.getLocationOnScreen(p1);
            return new Point(p1[0] - r2.left, p1[1] - r2.top);
        } else {
            return new Point();
        }
    }

    public static View findClickableChild(ViewGroup viewGroup, int x, int y){
        for(int i=0;i<viewGroup.getChildCount();i++){
            View c=viewGroup.getChildAt(i);
            if(c.getLeft()<x && c.getRight()>x && c.getTop()<y && c.getBottom()>y){
                if(c.isClickable())
                    return c;
                if(c instanceof ViewGroup) {
                    View r=findClickableChild((ViewGroup) c, x - c.getLeft(), y - c.getTop());
                    if(r!=null)
                        return r;
                }
            }
        }
        return null;
    }
}
