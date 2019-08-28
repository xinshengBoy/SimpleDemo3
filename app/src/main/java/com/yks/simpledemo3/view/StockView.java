package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * 描述：股票走势
 * 作者：zzh
 * time:2019/08/22
 */
public class StockView extends View {

    private Paint paint;
    private int width,height;
    private int startX = 0,startY = 0;
    private int defaultAdd = 2;

    public StockView(Context context) {
        this(context,null);
    }

    public StockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //重新计算宽高
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Random random = new Random();
//        startY = random.nextInt(height);
        drawCircleAndLine(canvas);
        mHandler.postDelayed(runnable,1000);
//        postInvalidateDelayed(1000);
    }

    private void drawCircleAndLine(Canvas canvas){
//        canvas.drawLine(startX,startY,startX,startY+defaultAdd,paint);
//        canvas.drawCircle(startX+defaultAdd,startY,3,paint);
        Path path = new Path();
        path.moveTo(startX,startY);
        canvas.drawPath(path,paint);
//        startX += defaultAdd;
    }

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startX += defaultAdd;
            Random random = new Random();
            startY = random.nextInt(height);
            mHandler.postDelayed(runnable,1000);
        }
    };
}
