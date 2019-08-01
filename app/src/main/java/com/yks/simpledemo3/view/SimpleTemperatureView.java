package com.yks.simpledemo3.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Scroller;

import com.yks.simpledemo3.R;

/**
 * 描述：温度计
 * 作者：zzh
 * time:2019/07/31
 * https://github.com/ibore/TemperatureView/blob/master/app/src/main/java/com/monians/temperatureview/MainActivity.java
 */
public class SimpleTemperatureView extends View {

    private float mWidth,mHeight,mMin,mProgress;
    private Paint [] mPaints;
    private RectF oval;
    private Scroller mScroller;
    private int mBackgoundColor;
    private int mThermometerColor;
    private int mValueColor = Color.RED;
    private float mValueize = 36;
    private String[] mNumber = {"80", "60", "40", "20", "0", "-20", "-40"};

    public SimpleTemperatureView(Context context) {
        this(context,null);
    }

    public SimpleTemperatureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleTemperatureView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TemperatureView,defStyleAttr,0);
        mBackgoundColor = array.getColor(R.styleable.TemperatureView_backgroundColor,Color.GRAY);//默认灰色,背景颜色
        mThermometerColor = array.getColor(R.styleable.TemperatureView_thermometerColor,Color.RED);//温度颜色
        mValueColor = array.getColor(R.styleable.TemperatureView_valueColor,Color.GRAY);//刻度颜色
        mValueize = array.getDimension(R.styleable.TemperatureView_valueSize,36);//默认文字大小为36

        mPaints = new Paint[2];
        for (int i=0;i<mPaints.length;i++){
            mPaints[i] = new Paint();
        }

        oval = new RectF();
        AccelerateInterpolator lator = new AccelerateInterpolator();
        this.mScroller = new Scroller(context,lator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        mMin = mHeight / 10;//最小刻度单位
        oval.set(mWidth / 2 - mMin * 0.75f,mMin * 8,mWidth / 2 + mMin * 0.75f,mMin * 9.5f);

        //绘制背景
        mPaints[0].setColor(mBackgoundColor);
        mPaints[0].setAntiAlias(true);//消除锯齿
        mPaints[0].setStrokeCap(Paint.Cap.ROUND);
        mPaints[0].setStrokeWidth(mMin / 2);//设置线条宽度
        canvas.drawLine(mWidth / 2,mMin * 0.5f,mWidth / 2,mMin * 8.5f,mPaints[0]);
        //绘制底部圆
        mPaints[0].setColor(mThermometerColor);
        canvas.drawArc(oval,0,360,false,mPaints[0]);
        //绘制刻度
        mPaints[1].setColor(mValueColor);
        mPaints[1].setStrokeWidth(mMin / 16);
        mPaints[1].setAntiAlias(true);
        mPaints[1].setTextSize(mValueize);
        mPaints[1].setTextAlign(Paint.Align.CENTER);
        canvas.drawText("℃",mWidth / 2 + mMin * 0.75f,mMin *0.75f,mPaints[1]);

        Rect rect = new Rect();
        mPaints[1].getTextBounds(mNumber[6],0,mNumber[6].length(),rect);
        for (int i=1;i<8;i++){
            canvas.drawLine(mWidth / 2 + mMin / 4,mMin * (i + 0.5f),mWidth / 2 + mMin / 2,mMin * (i + 0.5f),mPaints[1]);
            canvas.drawText(mNumber[i-1],mWidth / 2 + mMin / 2 + rect.width() / 3 * 2,mMin * (i + 0.5f) + rect.height() / 2,mPaints[1]);
        }
        //进度条
        if (mProgress != 0f){
            mPaints[0].setColor(mThermometerColor);
            mPaints[0].setStrokeCap(Paint.Cap.BUTT);
            canvas.drawLine(mWidth / 2,mMin * 8.5f,mWidth / 2,mMin * (7.5f - 7 * mProgress / 140),mPaints[0]);
        }
    }

    public float getProgress(){
        return mProgress;
    }

    @Keep
    public void setProgress(float progress){
        this.mProgress = progress;
        invalidate();
    }

    public void start(float progress){
        setProgress(progress+40);
        AnimatorSet animationSet = new AnimatorSet();
        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(this,"progress",0f,mProgress);
        progressAnimation.setDuration(3000);
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.playTogether(progressAnimation);
        animationSet.start();
    }
}
