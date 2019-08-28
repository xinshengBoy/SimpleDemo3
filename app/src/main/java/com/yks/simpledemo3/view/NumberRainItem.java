package com.yks.simpledemo3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.yks.simpledemo3.R;

/**
 * 描述：数字雨item
 * 作者：zzh
 * time:2019/08/26
 */
public class NumberRainItem extends View {

    private Paint mPaint;
    private float mTextSize = 30 * getResources().getDisplayMetrics().density;
    private final float DISPLAY_HEIGHT = getResources().getDisplayMetrics().heightPixels;
    private int mNormalColor = Color.GREEN;
    private int mHighLightColor = Color.YELLOW;
    private long mStartOffset = 1000L;
    private float mNowHeight = 0f;
    private int mHighLightNumIndex = 0;
    private int mVerticalTotalCount = (int)(DISPLAY_HEIGHT / mTextSize);

    public NumberRainItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberRainItem);
            mNormalColor = array.getColor(R.styleable.NumberRainItem_normalColor,Color.GREEN);
            mHighLightColor = array.getColor(R.styleable.NumberRainItem_highColor,Color.YELLOW);
            mTextSize = array.getDimension(R.styleable.NumberRainItem_textSize,mTextSize);
            array.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mNormalColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mNowHeight >= DISPLAY_HEIGHT){
            drawTotalNumber(canvas);
        }else {
            drawPartNumber(canvas);
        }
    }

    private void drawTotalNumber(Canvas canvas) {
        drawNumber(canvas,mVerticalTotalCount);
    }

    private void drawPartNumber(Canvas canvas) {
        int count = (int) (mNowHeight / mTextSize);
        mNowHeight += mTextSize;
        drawNumber(canvas,count);
    }

    private void drawNumber(Canvas canvas,int count){
        if (count == 0){
            postInvalidateDelayed(mStartOffset);
        }else {
            float offset = 0f;
            for (int i=0;i<count;i++){
                String text = String.valueOf((int) (Math.random() * 10));
                if (mHighLightNumIndex == i){
                    mPaint.setColor(mHighLightColor);
                }else {
                    mPaint.setColor(mNormalColor);
                }
                canvas.drawText(text,0f,mTextSize+offset,mPaint);
                offset += mTextSize;
            }
            if (mNowHeight >= DISPLAY_HEIGHT){
                mHighLightNumIndex = (++mHighLightNumIndex) % count;
            }else {
                mHighLightNumIndex++;
            }
            postInvalidateDelayed(100L);
        }
    }

    public void setTextSize(float size){
        this.mTextSize = size;
        postInvalidateDelayed(1000);
    }

    public void setNormalColor(int color){
        this.mNormalColor = color;
        postInvalidateDelayed(1000);
    }

    public void setHighLightColor(int color){
        this.mHighLightColor = color;
        postInvalidateDelayed(1000);
    }

    public void setStartOffset(int startOffset){
        this.mStartOffset = startOffset;
        postInvalidateDelayed(1000);
    }
}
