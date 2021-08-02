package com.xaxtix.team.waterillunination.ui;

import android.animation.AnimatorInflater;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.video.VideoListener;
import com.xaxtix.team.waterillunination.AnimationUtils;
import com.xaxtix.team.waterillunination.App;
import com.xaxtix.team.waterillunination.R;
import com.xaxtix.team.waterillunination.Utils;

import static android.widget.ListPopupWindow.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.xaxtix.team.waterillunination.Utils.dp;

public class ActionFragment extends Fragment {


    private final static int STATE_WAIT_ACTION = 0;
    private final static int STATE_PROGRESS = 1;
    private final static int STATE_FINISH = 2;

    private int state = 0;

    PlayerView playerView;
    ImageView firstFrame;
    View initialBackground;
    TextView button;
    ImageView resetButton;
    TextView stateText;

    SimpleExoPlayer actionPlayer;
    SimpleExoPlayer finishPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(getContext());

        playerView = new PlayerView(getContext());
        playerView.hideController();
        playerView.setControllerVisibilityListener((PlaybackControlView.VisibilityListener) i -> {
            if (i == 0) {
                playerView.hideController();
            }
        });

        firstFrame = new ImageView(getContext());
        firstFrame.setImageResource(R.drawable.first_frame);
        firstFrame.setScaleType(ImageView.ScaleType.CENTER_CROP);

        initialBackground = new BootstrapView(getActivity());

        button = new TextView(getContext());
        button.setOnClickListener(v -> setState(STATE_PROGRESS));
        button.setBackgroundResource(R.drawable.button_background);
        button.setPadding(dp(16), dp(16), dp(16), dp(16));

        resetButton = new ImageView(getContext());
        resetButton.setOnClickListener(v -> setState(STATE_WAIT_ACTION));
        resetButton.setBackgroundResource(R.drawable.button_background);
        resetButton.setPadding(dp(16), dp(16), dp(16), dp(16));

        stateText = new TextView(getContext());
        stateText.setTextSize(20);
        stateText.setTypeface(Typeface.DEFAULT_BOLD);
        stateText.setTextColor(Color.WHITE);
        stateText.setText(R.string.illimination_end_sate);
        stateText.setGravity(Gravity.CENTER_HORIZONTAL);

        frameLayout.addView(playerView);
        frameLayout.addView(firstFrame);
        frameLayout.addView(initialBackground);
        frameLayout.addView(button, MATCH_PARENT, WRAP_CONTENT);
        frameLayout.addView(resetButton, dp(64), dp(64));

        frameLayout.addView(stateText, WRAP_CONTENT, WRAP_CONTENT);
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) button.getLayoutParams();

        flp.leftMargin = dp(80);
        flp.rightMargin = dp(80);
        flp.bottomMargin = dp(60);
        flp.gravity = Gravity.BOTTOM;
        button.setText(R.string.illuminate_action);
        button.setGravity(Gravity.CENTER);
        button.setTextColor(Color.WHITE);
        resetButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        resetButton.setImageResource(R.drawable.ic_close_48dp);


        flp = (FrameLayout.LayoutParams) resetButton.getLayoutParams();

        flp.bottomMargin = dp(80);
        flp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;


        flp = (FrameLayout.LayoutParams) stateText.getLayoutParams();
        flp.topMargin = dp(60);
        flp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        actionPlayer = ExoPlayerFactory.newSimpleInstance(getActivity());
        actionPlayer.setVolume(0f);
        playerView.setPlayer(actionPlayer);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        playerView.hideController();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            button.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getActivity(),
                    R.animator.button_anim));
            resetButton.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getActivity(),
                    R.animator.button_anim));
        }


        MediaSource videoSource = new ExtractorMediaSource(Uri.parse("assets:///kek.mp4"), () -> new AssetDataSource(App.context), new DefaultExtractorsFactory(), null, null);

        actionPlayer.prepare(videoSource);
        actionPlayer.setPlayWhenReady(false);

        actionPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (STATE_ENDED == playbackState) setState(STATE_FINISH);
            }
        });


        finishPlayer = ExoPlayerFactory.newSimpleInstance(getActivity());
        finishPlayer.setVolume(0f);

        videoSource = new ExtractorMediaSource(Uri.parse("assets:///finish.mp4"), () -> new AssetDataSource(App.context), new DefaultExtractorsFactory(), null, null);

        finishPlayer.prepare(videoSource);
        finishPlayer.setPlayWhenReady(false);
        finishPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        finishPlayer.addVideoListener(new VideoListener() {
            @Override
            public void onRenderedFirstFrame() {
                if (state == STATE_FINISH) firstFrame.setVisibility(View.GONE);
            }
        });

        resetButton.setVisibility(View.GONE);
        stateText.setVisibility(View.GONE);
        setState(STATE_WAIT_ACTION);
        return frameLayout;
    }

    private void setState(int newState) {
        switch (newState) {
            case STATE_WAIT_ACTION:
                AnimationUtils.setVisibilityAnimated(firstFrame, View.GONE);
                AnimationUtils.setVisibilityAnimated(initialBackground, View.VISIBLE);
                AnimationUtils.setVisibilityAnimated(button, View.VISIBLE);
                AnimationUtils.setVisibilityAnimated(resetButton, View.GONE);
                AnimationUtils.setVisibilityAnimated(stateText, View.GONE);
                break;
            case STATE_PROGRESS:
                AnimationUtils.setVisibilityAnimated(firstFrame, View.GONE);
                AnimationUtils.setVisibilityAnimated(initialBackground, View.GONE);
                AnimationUtils.setVisibilityAnimated(button, View.GONE);
                AnimationUtils.setVisibilityAnimated(resetButton, View.GONE);
                AnimationUtils.setVisibilityAnimated(stateText, View.GONE);
                playerView.setPlayer(actionPlayer);
                actionPlayer.seekTo(0);
                actionPlayer.setPlayWhenReady(true);
                break;
            case STATE_FINISH:
                firstFrame.setVisibility(View.VISIBLE);
                AnimationUtils.setVisibilityAnimated(stateText, View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                resetButton.setTranslationY(Utils.dp(100));
                resetButton.animate()
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .translationY(0).start();
                playerView.setPlayer(finishPlayer);
                finishPlayer.setPlayWhenReady(true);
        }
        this.state = newState;
    }
}
