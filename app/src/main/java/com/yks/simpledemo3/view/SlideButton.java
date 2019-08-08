package com.yks.simpledemo3.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;

/**
 * 描述：仿ios滑动解锁自定义view
 * 作者：zzh
 * time:2019/08/06
 * https://github.com/crowerly/android_slide_button/blob/master/app/src/main/java/info/tangchen/slidebutton/SlideButton.java
 */
public class SlideButton extends View implements View.OnTouchListener {

    private int bgColor,circleColor,textColor;
    private float textSize;
    private String showContent;
    private float startX;
    private float width, height, spaceX, spaceY;
    private float offset = 0.0f, maxOffsetX, r;
    private float bottom, top, centerX;
    private SlideEndListener listener;
    private final int REFRESH = 0;//刷新

    public SlideButton(Context context) {
        super(context);
    }

    public SlideButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SlideButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        setOnTouchListener(this);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlideButton);
        bgColor = array.getColor(R.styleable.SlideButton_slide_button_bg, Color.GRAY);
        circleColor = array.getColor(R.styleable.SlideButton_slide_button_circle_color,Color.WHITE);
        textColor = array.getColor(R.styleable.SlideButton_slide_button_text_color,Color.WHITE);
        showContent = array.getString(R.styleable.SlideButton_slide_buuton_text_content);
        textSize = array.getDimension(R.styleable.SlideButton_slide_button_text_size, Info.dp2px(context,30));
        if (showContent == null){
            showContent = "滑动解锁";
        }
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0){
            width = getWidth();
            height = getHeight();
            spaceX = width / 21;//x轴留空距离
            spaceY = height / 8;//y轴留空间距
            r = (getHeight() - spaceY * 2) / 2;//圆形按钮的半径，即为整个view的高度减去上下两部分留空区域的一半
            maxOffsetX = getWidth() - 2 * (spaceX + r);//圆形按钮圆形最远移动距离，
        }
        paint(canvas);
    }

    /**
     * 描述：开始绘制
     * @param canvas 画布
     */
    private void paint(Canvas canvas) {
        //1：绘制圆角按钮背景
        drawBackground(canvas);
        //2：绘制文字
        drawTextContent(canvas);
        //3：绘制滑动圆圈
        drawCircle(canvas,r);
    }

    /**
     * 描述：绘制圆角背景
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas) {
        Paint bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(bgColor);
        RectF background = new RectF(spaceX, spaceY, getWidth() - spaceX, getHeight() - spaceY);
        canvas.drawRect(background,  bgPaint);

        bottom = background.bottom;
        top = background.top;
        centerX = background.centerX();
    }

    /**
     * 描述：绘制文字
     * @param canvas 画布
     */
    private void drawTextContent(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        Paint.FontMetricsInt metrics = textPaint.getFontMetricsInt();
        int baseline = (int) (bottom + top - metrics.bottom - metrics.top) / 2;
        //实现文字居中（上下居中，左右居中）
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(2);
        textPaint.setTextSize(textSize);
        textPaint.setAlpha(getTextAlpha());
        canvas.drawText(showContent,centerX,baseline+10,textPaint);
    }

    /**
     * 画圆
     * @param canvas 画布
     * @param radius 圆的半径
     */
    private void drawCircle(Canvas canvas,float radius) {
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(circleColor);
        //给偏移量设置一个阈值，不能超过圆形的最大移动距离，否则会出现圆形控件移出整个滑动控件的问题
        if (offset >= maxOffsetX){
            offset = maxOffsetX;
        }
        canvas.drawCircle(radius + spaceX + offset,radius + spaceY,radius,circlePaint);
    }

    /**
     * 描述：获取文字透明度
     * @return 透明度
     */
    private int getTextAlpha(){
        int base = (int) (255 - (255 * (offset >= maxOffsetX ? maxOffsetX : offset) / maxOffsetX));
        return base <= 235 ? base + 20 : base;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float offsetX;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN://按下
                startX = event.getX();
                if (startX > this.spaceX + 2 * r){
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE://滑动
                offsetX = event.getX() - startX;
                if (offsetX > 0){
                    offset = offsetX;
                    handler.sendEmptyMessage(REFRESH);
                }
                break;
            case MotionEvent.ACTION_UP://抬起
                if (offset + 25 >= maxOffsetX && listener != null){
                    listener.onScrollFinish();
                }

                for (;offset > 0;offset--){
                    handler.sendEmptyMessage(REFRESH);
                }
                break;
        }
        return true;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH){
                invalidate();
            }
        }
    };

    public void setOnScrollListener(SlideEndListener listeners){
        this.listener = listeners;
    }

    public interface SlideEndListener {
        void onScrollFinish();
    }
}
