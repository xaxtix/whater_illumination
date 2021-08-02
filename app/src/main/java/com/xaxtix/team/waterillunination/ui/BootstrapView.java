package com.xaxtix.team.waterillunination.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.xaxtix.team.waterillunination.R;
import com.xaxtix.team.waterillunination.Utils;

import java.util.ArrayList;
import java.util.Random;

class BootstrapView extends View {

    int backgroundColor = 0xFF1D2255;

    ArrayList<Ball> balls = new ArrayList();
    ArrayList<Ball> removable = new ArrayList();
    Drawable b1;
    Drawable b2;
    Drawable b3;
    Drawable b4;
    Drawable b5;
    Drawable b6;
    Drawable b7;
    Drawable bottomShadow;

    int tryCount = 10;

    Paint bgPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint bgPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BootstrapView(Context context) {
        super(context);
        b1 = context.getResources().getDrawable(R.drawable.d1);
        b2 = context.getResources().getDrawable(R.drawable.d2);
        b3 = context.getResources().getDrawable(R.drawable.d3);
        b4 = context.getResources().getDrawable(R.drawable.d4);
        b5 = context.getResources().getDrawable(R.drawable.d5);
        b6 = context.getResources().getDrawable(R.drawable.d6);
        b7 = context.getResources().getDrawable(R.drawable.d7);

        bottomShadow = context.getResources().getDrawable(R.drawable.bottom_shadow);

        bgPaint1.setColor(0xFF171B4E);
        bgPaint2.setColor(0xFF171842);

    }


    int lastH = 0;
    Random random = new Random();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        b1.setBounds(-b1.getIntrinsicWidth() / 2, -b1.getIntrinsicHeight() / 2, b1.getIntrinsicWidth() / 2, b1.getIntrinsicHeight() / 2);
        b2.setBounds(-b2.getIntrinsicWidth() / 2, -b2.getIntrinsicHeight() / 2, b2.getIntrinsicWidth() / 2, b2.getIntrinsicHeight() / 2);
        b3.setBounds(-b3.getIntrinsicWidth() / 2, -b3.getIntrinsicHeight() / 2, b3.getIntrinsicWidth() / 2, b3.getIntrinsicHeight() / 2);
        b4.setBounds(-b4.getIntrinsicWidth() / 2, -b4.getIntrinsicHeight() / 2, b4.getIntrinsicWidth() / 2, b4.getIntrinsicHeight() / 2);
        b5.setBounds(-b5.getIntrinsicWidth() / 2, -b5.getIntrinsicHeight() / 2, b5.getIntrinsicWidth() / 2, b5.getIntrinsicHeight() / 2);
        b6.setBounds(-b6.getIntrinsicWidth() / 2, -b6.getIntrinsicHeight() / 2, b6.getIntrinsicWidth() / 2, b6.getIntrinsicHeight() / 2);
        b7.setBounds(-b7.getIntrinsicWidth() / 2, -b7.getIntrinsicHeight() / 2, b7.getIntrinsicWidth() / 2, b7.getIntrinsicHeight() / 2);
        bottomShadow.setBounds(0, getMeasuredHeight() - Utils.dp(312), getMeasuredWidth(), getMeasuredHeight());
        if (getMeasuredHeight() != lastH) {
            balls.clear();
            createBalls();
            lastTime = System.currentTimeMillis();
        }
    }

    private void createBalls() {

        lastH = getMeasuredHeight();

        for (int i = 0; i < 17; i++) {
            int bestX = 0;
            int bestY = 0;
            float bestDistanse = 0;
            for (int j = 0; j < tryCount; j++) {
                int x = randomX();
                int y = randomY();


                float minDistance = Integer.MAX_VALUE;
                for (int k = 0; k < balls.size(); k++) {
                    Ball b = balls.get(k);
                    float distance = (x - b.x) * (x - b.x) + (y - b.y) * (y - b.y);
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }

                if (minDistance > bestDistanse) {
                    bestX = x;
                    bestY = y;
                    bestDistanse = minDistance;
                }
            }


            Ball b = new Ball();
            balls.add(b);
            b.d = getBallDrawable();
            b.x = bestX;
            b.y = bestY;
            b.creationTime = System.currentTimeMillis() + Math.abs(random.nextInt()) % 5000 + 200;
            b.killTime = 5000 + System.currentTimeMillis() + Math.abs(random.nextInt()) % 7000;
            b.s = 0.2f + ((random.nextInt() % 100) / 2000f);
            b.r = random.nextFloat() % 360;
            b.dR = -1f + random.nextInt(20) / 10f;

        }

        postDelayed(this::createBalls, 6000);
    }

    private Drawable getBallDrawable() {
        switch (random.nextInt(12)) {
            case 0: {
                switch (random.nextInt(4)) {
                    case 1:
                        return b2;
                    case 2:
                        return b3;
                    case 3:
                        return b4;
                    default:
                        return b1;
                }
            }
            case 1:
            case 2:
            case 3:
                return b5;
            case 4:
            case 5:
            case 6:
                return b6;
            default:
                return b7;
        }

    }

    private int randomY() {
        return Utils.dp(36) + Math.abs(random.nextInt()) % (getMeasuredHeight() - Utils.dp(72));
    }

    private int randomX() {
        return Utils.dp(36) + Math.abs(random.nextInt()) % (getMeasuredWidth() - Utils.dp(72));
    }

    long lastTime;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);


        canvas.drawCircle(
                getMeasuredWidth() >> 1,
                getMeasuredWidth() * 0.15f + getMeasuredWidth() / 2f,
                getMeasuredWidth() >> 1, bgPaint1
        );

        canvas.drawCircle(
                getMeasuredWidth() >> 1,
                getMeasuredWidth() * 0.15f + getMeasuredWidth() / 2f,
                getMeasuredWidth() / 2f * 0.6f, bgPaint2
        );

        long time = System.currentTimeMillis();
        int n = balls.size();
        removable.clear();
        for (int i = 0; i < n; i++) {
            if (balls.get(i).draw(canvas, time)) removable.add(balls.get(i));
        }
        balls.removeAll(removable);

        bottomShadow.draw(canvas);

        invalidate();
        lastTime = time;
    }

    OvershootInterpolator overshot = new OvershootInterpolator();

    class Ball {
        Drawable d;
        float x;
        float y;
        float s;
        float r;
        float dR;
        long creationTime;
        long killTime;

        float animateScale;
        float enterProgress = 0f;

        public boolean draw(Canvas canvas, long time) {
            if (time < creationTime) return false;
            float dt = (time - lastTime) / 16f;

            float e = ((time - creationTime) / 1000f);

            if (e > 1f) e = 1f;

            if (time > killTime) {
                enterProgress = (time - killTime) / 2000f;
                if (enterProgress > 1f) return true;
                enterProgress = 1f - enterProgress;
            } else {
                enterProgress = overshot.getInterpolation(e);
            }


            canvas.save();
            canvas.translate(x, y);
            canvas.rotate(r, 0, 0);
            canvas.scale(s * enterProgress, s * enterProgress);
            d.draw(canvas);
            canvas.restore();


            y -= dt * (1f - s) * 2;
            r += dR * dt;
            return false;
        }

    }
}
