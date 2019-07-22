package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：小米计步器
 * 作者：zzh
 * time:2019/07/20
 */
public class XiaomiStepView extends View {

    private final Paint thirdPaint;//三角形画笔
    private final Paint linePaint;//刻度线画笔
    private final Paint textPaint;//文字画笔
    private final Paint circlePaint;//内部圆形画笔

    private int mLen;//实际尺寸大小
    private int mMilliSeconds;//计时的总毫秒数
    private int outAngle;//外圈指针的角度
    private int innerAngle;//小圈指针角度
    private float thirdLen;//三角形的边长
    private boolean isPause;//是否暂停
    private float eachLineAngle = 360f / 240f;//两个刻度线质检的角度，1.5度一个刻度，总共240个间隔
    private Timer timer;//计时器
    private String showContent;//显示的总的时间

    public XiaomiStepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //三角形指针画笔
        thirdPaint = new Paint();
        thirdPaint.setColor(Color.WHITE);
        thirdPaint.setAntiAlias(true);//扛锯齿
        //刻度线画笔
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(2);//刻度线的宽度
        //文字画笔
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);//居中
        textPaint.setStrokeWidth(2);
        //内部圆形画笔
        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);//无填充
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //重新定义尺寸，为正方形
        int width = measuredDimension(widthMeasureSpec);
        int height = measuredDimension(heightMeasureSpec);
        mLen = Math.min(width,height);
        //小三角形指针断点到外圆之间的距离，用于计算三角形坐标（整体宽度的1/16）
        thirdLen = mLen / 16.0f;
        //设置新的值
        setMeasuredDimension(mLen,mLen);
    }

    private int measuredDimension(int measure){
        int defaultSize = 800;//默认大小
        int mode = MeasureSpec.getMode(measure);
        int size = MeasureSpec.getSize(measure);
        switch (mode){
            case MeasureSpec.EXACTLY://指定尺寸
                return size;
            case MeasureSpec.AT_MOST://match_parent
                return size;
            case MeasureSpec.UNSPECIFIED://wrap_parent
                return defaultSize;
            default:
                return defaultSize;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateValue();
        drawThird(canvas);
        drawLine(canvas);
        drawText(canvas);
        drawSecondHand(canvas);
    }

    /**
     * 描述：计算相关值，如当前毫秒值，内外指针角度等
     * 作者：zzh
     */
    private void calculateValue(){
        //文字
        int hours = mMilliSeconds / (1000 * 60 * 60);
        int minutes = (mMilliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (mMilliSeconds - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
        int milliSec = mMilliSeconds % 1000 / 100;//毫秒值
        if (hours == 0){
            showContent = toDoubleDigit(minutes) + ":" + toDoubleDigit(seconds) + "." + milliSec;
        }else {
            showContent = toDoubleDigit(hours) + ":" + toDoubleDigit(minutes) + ":" + toDoubleDigit(seconds) + "." + milliSec;
        }

        //外角度
        outAngle = 360 * (mMilliSeconds % 60000) / 60000;
        //内角度
        innerAngle = 360 * (mMilliSeconds % 1000) / 1000;
    }

    /**
     * 描述：转成两位数，如小于10则在数值前面加0
     * 作者：zzh
     * @param value 传入的值
     * @return 返回的字符
     */
    private String toDoubleDigit(int value){
        if (value < 10){
            return "0" + value;
        }else {
            return "" + value;
        }
    }

    /**
     * 描述：根据角度绘制三角形
     * 作者：zzh
     * @param canvas 画布
     */
    private void drawThird(Canvas canvas){
        canvas.save();
        //确定坐标
        canvas.translate(mLen / 2,mLen / 2);//移动的位置
        canvas.rotate(outAngle);//旋转的角度
        //画三角形
        Path path = new Path();
        //指针点
        path.moveTo(0,mLen / 2 - thirdLen);
        //左右侧点
        path.lineTo(0.5f * thirdLen,mLen / 2 - 0.134f * thirdLen);
        path.lineTo(-0.5f * thirdLen,mLen / 2 - 0.134f * thirdLen);
        path.close();
        canvas.drawPath(path,thirdPaint);
        canvas.restore();
    }

    /**
     * 描述：画线
     * 作者：zzh
     * @param canvas 画布
     */
    private void drawLine(Canvas canvas){
        canvas.save();
        canvas.translate(mLen / 2,mLen / 2);
        int totalLines = (int) (360f / eachLineAngle);//240条线
        int lightLine = (int) (outAngle / eachLineAngle);//最亮的线
        int darkLine = lightLine - (int) (90 / eachLineAngle);//最暗的线
        boolean negativeFlag = false;//负数标识，跨过了0这个起始位置
        if (darkLine < 0){
            negativeFlag = true;
            darkLine = totalLines - Math.abs(darkLine);
        }

        int count = 0;//已画的线
        for (int i=0;i<totalLines;i++){
            canvas.rotate(eachLineAngle);
            int color = 0;
            if (!negativeFlag){
                //没有跨过起始点标志
                if (i >= darkLine && i <= lightLine && count < (totalLines / 4)){
                    count ++;
                    color = Color.argb(255 - ((totalLines / 4 - count) * 3),255,255,255);
                }else {
                    color = Color.argb(255 - (int)(360f * 3 / (eachLineAngle * 4)),255,255,255);
                }
            }else {
                //跨过了起始点
                if (i >= 0 && i < lightLine){
                    if (count == 0){
                        count = totalLines / 4 - lightLine;
                    }else {
                        count ++;
                    }
                    color = Color.argb(255 - ((totalLines / 4 - count) * 3),255,255,255);
                }else if (mMilliSeconds != 0 && i < totalLines && i >= darkLine){
                    count ++;
                    color = Color.argb(255 - ((totalLines / 4 - (i - lightLine)) * 3),255,255,255);
                }else {
                    color = Color.argb(255 - (int)(360f * 3 / (eachLineAngle * 4)),255,255,255);
                }
            }
            linePaint.setColor(color);
            canvas.drawLine(0,(float)(mLen / 2 - (thirdLen + thirdLen / 5)),0,(float)(mLen / 2 - (2 * thirdLen + thirdLen / 5)),linePaint);
        }
        canvas.restore();
    }

    /**
     * 描述：画文字
     * 作者：zzh
     * @param canvas 画布
     */
    private void drawText(Canvas canvas){
        canvas.save();
        canvas.translate(mLen / 2,mLen / 2);
        textPaint.setTextSize(mLen / 10);
        canvas.drawText(showContent,0,0,textPaint);
        canvas.restore();
    }

    /**
     * 描述：绘制内部指针
     * 作者：zzh
     * @param canvas 画布
     */
    private void drawSecondHand(Canvas canvas){
        canvas.save();
        canvas.translate(mLen / 2,(float) mLen * 3 / 4.0f - mLen / 16);
        canvas.drawCircle(0,0,mLen / 12 ,circlePaint);
        canvas.drawCircle(0,0,mLen / 80 ,circlePaint);
        canvas.rotate(innerAngle);
        canvas.drawLine(0,mLen / 80,0,mLen / 14 ,circlePaint);
        canvas.restore();
    }

    public void start(){
        if (timer == null){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isPause){
                        mMilliSeconds += 50;
                        postInvalidate();
                    }
                }
            },50,50);
        }else {
            resume();
        }
    }

    public void pause(){
        isPause = true;
    }

    private void resume(){
        isPause = false;
    }

    public void reset(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        isPause = false;
        mMilliSeconds = 0;
        invalidate();
    }

    public int record(){
        return mMilliSeconds;
    }
}
