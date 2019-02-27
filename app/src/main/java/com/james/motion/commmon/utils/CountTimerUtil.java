package com.james.motion.commmon.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

/**
 * 描述: 倒计时
 * 作者: james
 * 日期: 2019/2/18 19:34
 * 类名: CountTimerUtil
 */
public class CountTimerUtil {

    // 默认计时
    private static final int DEFAULT_REPEAT_COUNT = 4;
    // 最后一秒显示的文本
    private static final String LAST_SECOND_TEXT = "Go";

    // 当前的计时
    private static int sCurCount = DEFAULT_REPEAT_COUNT;

    public static <T extends TextView> void start(T animationViewTv, AnimationState animationState) {
        start(animationViewTv, DEFAULT_REPEAT_COUNT, animationState);
    }

    /**
     * @param animationViewTv 要被倒计时的控件
     * @param repeatCount     要倒计时多久，单位，秒。
     */
    private static <T extends TextView> void start(final T animationViewTv, final int repeatCount, AnimationState animationState) {

        // 设置计时
        sCurCount = repeatCount - 1;
        animationViewTv.setText(String.valueOf(sCurCount));
        animationViewTv.setVisibility(View.VISIBLE);

        // 透明度渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        // 缩放渐变动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.1f, 1.3f, 0.1f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setRepeatCount(sCurCount);
        alphaAnimation.setRepeatCount(sCurCount);
        alphaAnimation.setDuration(1000);
        scaleAnimation.setDuration(1000);

//        scaleAnimation.setRepeatCount(1); //设置重复次数
//        alphaAnimation.setRepeatMode(Animation.REVERSE); //设置重复模式为倒转，即先从小变大再变小

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationState.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束时，隐藏
                animationViewTv.setVisibility(View.GONE);
                animationState.end();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // 减秒
                --sCurCount;
                // 设置文本
                if (sCurCount == 0) animationViewTv.setText(LAST_SECOND_TEXT);
                else animationViewTv.setText(String.valueOf(sCurCount));

                animationState.repeat();
            }
        });

        // 两个动画同时播放
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationViewTv.startAnimation(animationSet);
    }

    public interface AnimationState {
        void start();

        void repeat();

        void end();
    }
}
