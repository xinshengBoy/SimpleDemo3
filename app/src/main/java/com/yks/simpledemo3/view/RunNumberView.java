package com.yks.simpledemo3.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * 描述：
 * 作者：
 * time:2019/08/31
 */
public class RunNumberView extends TextView {

    private DecimalFormat mDf;
    private ValueAnimator mAnimator;

    public RunNumberView(Context context) {
        this(context,null);
    }

    public RunNumberView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RunNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDf = new DecimalFormat("0.00");//格式化小数，保留小数点后两位
        initnim();
    }

    private void initnim() {
        //金钱用ofFloat
        mAnimator = ValueAnimator.ofFloat(0,0);
        mAnimator.setDuration(1500);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value > 0){
                    setText(mDf.format(value));
                }
            }
        });
    }

    public void setMoney(float money){
        mAnimator.setFloatValues(0,money);//重新设置数值的变化区间
        mAnimator.start();//开启动画
    }

    public void cancelAnim(){
        mAnimator.removeAllUpdateListeners();//清除监听事件
        mAnimator.cancel();;//取消动画
    }
}
