package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 描述：
 * 作者：
 * time:2019/08/05
 */
public class MarqueeTextView2 extends TextView implements View.OnClickListener {

    private Paint paint = null;
    private String text;
    private float textLength;
    private float viewWidth;
    private float step  = 0f;//文字的横坐标
    private float y = 0f;//文字的纵坐标
    private float temp_view_plus_text_length  = 0.0f;
    private float temp_view_plus_two_text_length  = 0.0f;
    private boolean isStart = false;//是否开始滚动
    private int mSpeed;
    private float mWidth;
    public MarqueeTextView2(Context context) {
        super(context);
        initView();
    }

    public MarqueeTextView2(Context context,AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MarqueeTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isStart){
            //停止滚动，重新开始滚动
            stopScroll();
        }else {
            //还未滚动，开始滚动
            startScroll();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        while (textLength < mWidth){
            text += " ";
            textLength = paint.measureText(text);
        }
        canvas.drawText(text,temp_view_plus_text_length - step,y,paint);
        if (!isStart){
            return;
        }

        step += mSpeed;//速度设置，最大为10
        if (step > temp_view_plus_two_text_length){
            step = textLength;
        }

        invalidate();
    }

    public void init(WindowManager manager,String input, int textColor, int speed){
        paint = getPaint();
        paint.setColor(textColor);
        mSpeed = speed;

        text = input;
        textLength = paint.measureText(text);
        viewWidth = getWidth();
        if (viewWidth == 0){
            if (manager != null){
                Display play = manager.getDefaultDisplay();
                viewWidth = play.getWidth();
            }
        }
        step = viewWidth + textLength;
        y = getTextSize() + getPaddingTop();
        temp_view_plus_text_length  = viewWidth + textLength;
        temp_view_plus_two_text_length = viewWidth + textLength * 2;
    }

    public void startScroll(){
        isStart = true;
        invalidate();
    }


    public void stopScroll(){
        isStart = false;
        invalidate();
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
