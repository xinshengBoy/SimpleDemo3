package com.yks.simpledemo3.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;

import java.text.DecimalFormat;

/**
 * 描述：数字滚动，仿支付宝余额滚动显示
 * 作者：zzh
 * time:2019/08/31
 */
public class RunNumberView extends android.support.v7.widget.AppCompatTextView {

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
//        mAnimator.setRepeatCount(1);//重复次数
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
    //重新设置数值的变化区间
    public void setMoney(float money,long duration){
        if (mAnimator.isRunning() || mAnimator.isStarted()){
            cancelAnim();
        }
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(0,money);
        mAnimator.start();//开启动画
    }
    //取消动画
    public void cancelAnim(){
        mAnimator.removeAllUpdateListeners();//清除监听事件
        mAnimator.cancel();//取消动画
    }
}
