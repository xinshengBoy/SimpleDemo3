package com.yks.simpledemo3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;

import java.util.Calendar;

/**
 * 描述：自定义时钟
 * 作者：zzh
 * time:2019/08/05
 * 参考：https://blog.csdn.net/sinat_36668731/article/details/81032530
 * https://github.com/Jmengfei/CustomView/blob/master/app/src/main/java/com/example/jmf/timetest/WatchBoard.java
 */
public class CustomClockView extends View {

    private float mRadius;//圆形半径
    private float mPadding;//边距
    private float mTextSize;//文字大小
    private float mHourPointWidth;//时针宽度
    private float mMinutePointWidth;//分针宽度
    private float mSecondPointWidth;//秒针宽度
    private float mPointRadius;//指针圆角
    private float mPointEndLength;//指针末尾长度

    private int mHourPointColor;//时针颜色
    private int mMinutePointColor;//分针颜色
    private int mSecondPointColor;//秒针颜色
    private int mLongColor;//长线颜色
    private int mShortColor;//短线颜色

    private Paint mPaint;//画笔
    private PaintFlagsDrawFilter mFilter;//为画布设置抗锯齿
    private int width;//钟表边长

    public CustomClockView(Context context) {
        this(context,null);
    }

    public CustomClockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
        initPaint();
        //为画布实现抗锯齿
        mFilter = new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        //获取控件宽高
        int widthPix = context.getResources().getDisplayMetrics().widthPixels;
        int heightPix = context.getResources().getDisplayMetrics().heightPixels;
        width = Math.min(widthPix,heightPix);//与最小值相等
    }

    private void initAttrs(Context context,AttributeSet attr){
        TypedArray array = context.obtainStyledAttributes(attr, R.styleable.CustomClockView);

        mPadding = array.getDimension(R.styleable.CustomClockView_ccv_padding, Info.dp2px(context,10));
        mTextSize = array.getDimension(R.styleable.CustomClockView_ccv_text_size, Info.dp2px(context,16));
        mHourPointWidth = array.getDimension(R.styleable.CustomClockView_ccv_hour_pointer_width, Info.dp2px(context,5));
        mMinutePointWidth = array.getDimension(R.styleable.CustomClockView_ccv_minute_pointer_width, Info.dp2px(context,3));
        mSecondPointWidth = array.getDimension(R.styleable.CustomClockView_ccv_second_pointer_width, Info.dp2px(context,2));
        mPointRadius = array.getDimension(R.styleable.CustomClockView_ccv_pointer_cornor_radius, Info.dp2px(context,10));
        mPointEndLength = array.getDimension(R.styleable.CustomClockView_ccv_pointer_end_length, Info.dp2px(context,10));

        mHourPointColor = array.getColor(R.styleable.CustomClockView_ccv_hour_pointer_color, Color.BLACK);
        mMinutePointColor = array.getColor(R.styleable.CustomClockView_ccv_minute_pointer_color, Color.BLACK);
        mSecondPointColor = array.getColor(R.styleable.CustomClockView_ccv_second_pointer_color, Color.RED);
        mLongColor = array.getColor(R.styleable.CustomClockView_ccv_scale_long_color, Color.argb(255,0,0,0));
        mShortColor = array.getColor(R.styleable.CustomClockView_ccv_scale_short_color, Color.argb(125,0,0,0));

        array.recycle();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = measureSize(widthMeasureSpec);
        int h = measureSize(heightMeasureSpec);
        setMeasuredDimension(w,h);
    }

    //测量宽高和屏幕做对比
    private int measureSize(int spec){
        int size = MeasureSpec.getSize(spec);
        width = Math.min(width,size);
        return width;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = (Math.min(w,h) - mPadding) / 2;
        mPointEndLength = mRadius / 6;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //抗锯齿
        canvas.setDrawFilter(mFilter);
        //1：先画圆
        drawCircle(canvas);
        //2：画刻度
        drawScale(canvas);
        //3：画指针
        drawPointer(canvas);
        //4：每秒刷新一次
        postInvalidateDelayed(1000);
    }

    /**
     * 描述：画外圈的圆
     * 作者：zzh
     * @param canvas 画布
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width/2,width/2,mRadius,mPaint);
    }

    /**
     * 描述：画刻度
     * 作者：zzh
     * @param canvas 画布
     */
    private void drawScale(Canvas canvas) {
        mPaint.setStrokeWidth(Info.dp2px(getContext(),1));
        int lineWidth;
        for (int i=0;i<60;i++){
            if (i % 5 == 0){
                mPaint.setStrokeWidth(Info.dp2px(getContext(),1.5f));//粗一点
                mPaint.setColor(mLongColor);
                lineWidth = 40;
                mPaint.setTextSize(mTextSize);
                String text = ((i / 5) == 0 ? 12 : (i / 5)) + "";
                Rect rect = new Rect();
                mPaint.getTextBounds(text,0,text.length(),rect);
                mPaint.setColor(Color.BLACK);
                canvas.drawText(text,width / 2 - rect.width() / 2,rect.height() + Info.dp2px(getContext(),5) + lineWidth + mPadding,mPaint);//画分钟刻度
            }else {
                lineWidth = 30;
                mPaint.setColor(mShortColor);;
                mPaint.setStrokeWidth(Info.dp2px(getContext(),1f));
            }

            canvas.drawLine(width / 2,mPadding,width / 2,mPadding + lineWidth,mPaint);
            canvas.rotate(6,width / 2,width / 2);
        }
    }

    /**
     * 描述：画指针
     * 作者：zzh
     * @param canvas 画布
     */
    private void drawPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);//时
        int minute = calendar.get(Calendar.MINUTE);//分
        int second = calendar.get(Calendar.SECOND);//秒

        //转过的角度
        float angleHour = (hour + (float)minute / 60) * 360 / 12;//当前时针的角度
        float angleMinute = (minute + (float) second / 60) * 360 / 60;//当前分针的角度
        float angleSecond = second * 360 / 60;//当前秒针的角度

        //1:绘制时针
        canvas.save();
        canvas.rotate(angleHour,width / 2,width / 2);//旋转到时针的角度
        RectF rectHour = new RectF(width / 2 - mHourPointWidth / 2,width / 2 - mRadius * 3 / 5,width / 2 + mHourPointWidth / 2,width / 2 + mPointEndLength);
        mPaint.setColor(mHourPointColor);
        mPaint.setStyle(Paint.Style.STROKE);//加粗
        mPaint.setStrokeWidth(mHourPointWidth);//线粗
        canvas.drawRoundRect(rectHour,mPointRadius,mPointRadius,mPaint);
        canvas.restore();
        //2：绘制分针
        canvas.save();
        canvas.rotate(angleMinute,width / 2,width / 2);
        RectF rectMinute = new RectF(width / 2 - mMinutePointWidth / 2,width / 2 - mRadius * 3.5f / 5,width / 2 + mMinutePointWidth / 2,width / 2 + mPointEndLength);
        mPaint.setColor(mMinutePointColor);
        mPaint.setStrokeWidth(mMinutePointWidth);
        canvas.drawRoundRect(rectMinute,mPointRadius,mPointRadius,mPaint);
        canvas.restore();
        //绘制秒针
        canvas.save();
        canvas.rotate(angleSecond,width / 2,width / 2);
        RectF rectSecond = new RectF(width / 2 - mSecondPointWidth / 2,width / 2 - mRadius + Info.dp2px(getContext(),10),width / 2 + mSecondPointWidth / 2,width / 2 + mPointEndLength);
        mPaint.setColor(mSecondPointColor);
        mPaint.setStrokeWidth(mSecondPointWidth);
        canvas.drawRoundRect(rectSecond,mPointRadius,mPointRadius,mPaint);
        canvas.restore();
        //绘制原点
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2,width /2,mSecondPointWidth * 4,mPaint);
        //绘制日期
        canvas.save();
        mPaint.setColor(mHourPointColor);
        mPaint.setStrokeWidth(mSecondPointWidth);
        canvas.drawText(year+"-"+month+"-"+day,width / 2 - mPointEndLength,width / 2 + mPointEndLength * 2,mPaint);
        canvas.restore();
    }
}
