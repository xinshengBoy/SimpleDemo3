package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * 描述：扇叶
 * 作者：zzh
 * time:2019/07/25
 * 参考：https://github.com/yinyiliang/Windmill/blob/master/app/src/main/java/yyl/windmill/MainActivity.java
 */
public class WindPath extends View {

    private float width,height;
    private float centerX,centerY;//圆心
    private RotateAnimation animation;
    private int windSpeed = 1;//风速
    private Paint windPaint;//扇叶画笔
    private float x1,y1,x2,y2,x3,y3,x4,y4,x5,y5;

    public WindPath(Context context) {
        this(context,null);
    }

    public WindPath(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WindPath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        windPaint = new Paint();
        windPaint.setStyle(Paint.Style.FILL);
        windPaint.setStrokeWidth(2);
        windPaint.setAntiAlias(true);
        windPaint.setColor(Color.WHITE);

        animation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setRepeatCount(-1);//设置无限重复
        animation.setInterpolator(new LinearInterpolator());//设置匀速
        animation.setFillAfter(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (height > width){
            height = width;
        }else {
            width = height;
        }
        measure();
        setMeasuredDimension((int) width,(int)height);
    }

    private void measure(){
        x1 = width / 2;
        y1 = width / 2 - getFitSize(20);
        x2 = width / 2 + getFitSize(20);
        y2 = width / 2 - getFitSize(50);
        x3 = x2;
        y3 = width / 2 - getFitSize(60);
        x4 = width / 2;
        y4 = 0;
        x5 = width / 2 - getFitSize(10);
        y5 = y2 / 2;
        centerX = width / 2;
        centerY = width / 2;
    }

    private float getFitSize(float orgSize) {
        return orgSize * width / 496;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        drawPoint(canvas);
        drawFan(canvas);

        canvas.restore();
    }

    private void drawPoint(Canvas canvas){
        canvas.drawCircle(centerX,centerY,getFitSize(20),windPaint);
    }

    private void drawFan(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x1,y1);
        path.cubicTo(x2,y2,x3,y3,x4,y4);
        path.quadTo(x5,y5,x1,y1);
        canvas.drawPath(path,windPaint);

        canvas.rotate(120,centerX,centerY);
        canvas.drawPath(path,windPaint);

        canvas.rotate(120,centerX,centerY);
        canvas.drawPath(path,windPaint);
    }

    public void setWindVelocity(int value){
        if (value == 0){
            value = 1;
        }
        if (value > 17){
            value = 17;
        }
        this.windSpeed = value;
    }

    public void startAnim(){
        stopAnim();
        animation.setDuration(1800 - windSpeed * 100);
        startAnimation(animation);
    }

    public void stopAnim(){
        clearAnimation();
    }
}
