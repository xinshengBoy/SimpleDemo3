package com.yks.simpledemo3.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yks.simpledemo3.R;

import java.math.BigDecimal;

/**
 * 描述：温度计
 * 作者：zzh
 * time:2019/07/31
 * https://blog.csdn.net/mxw3755/article/details/48658425
 */
@SuppressLint("AppCompatCustomView")
public class TemperatureView extends ImageView {

    private Paint mPaint;
    private int width,height,circle_radius;
    private int COLOR_BROWN = Color.parseColor("#A5937B");//棕色
    private int COLOR_YELLOW = Color.parseColor("#F7AF1F");//黄色
    private int COLOR_GRAY = Color.parseColor("#C1CDCD");//灰色，线条颜色
    private int COLOR_TEXT = Color.parseColor("#49BDCC");//蓝色，字体颜色
    private float temperature = 0f;//当前温度
    private float temp_temperature = 0f;

    public TemperatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TemperatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (width < 100){
            width = 100;
        }
        if (height < 100){
            height = 100;
        }
        circle_radius = width / 4;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBG(canvas);
        drawScale(canvas);
        drawFG(canvas);

        if (temp_temperature < temperature && temp_temperature < 41.5) {
            if (temp_temperature < 34) {
                BigDecimal b1 = new BigDecimal(Float.toString(temp_temperature));
                BigDecimal b2 = new BigDecimal(Float.toString(1f));
                temp_temperature = b1.add(b2).floatValue();
            } else {
                BigDecimal b1 = new BigDecimal(Float.toString(temp_temperature));
                BigDecimal b2 = new BigDecimal(Float.toString(0.1f));
                temp_temperature = b1.add(b2).floatValue();
            }
            invalidate();
        }
    }

    private void drawBG(Canvas canvas) {
        drawWhiteCircle(canvas);
        drawWhiteRec(canvas);
        drawBrownCircle(canvas);
        drawBrownRec(canvas);
    }

    /**
     * 描述：画白色的圆圈
     * 作者：zzh
     * @param canvas 画笔
     */
    private void drawWhiteCircle(Canvas canvas) {
        mPaint.reset();;
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);//抗锯齿

        int circle_x = width - getPaddingRight() - circle_radius;
        int circle_y = height - getPaddingBottom() - circle_radius;

        canvas.drawCircle(circle_x,circle_y,circle_radius,mPaint);
    }

    /**
     * 描述：画白色的矩形
     * 作者：zzh
     * @param canvas 画笔
     */
    private void drawWhiteRec(Canvas canvas){
        mPaint.reset();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);

        RectF rectF = new RectF();
        rectF.left = width - getPaddingRight() - circle_radius - circle_radius / 3;//左边
        rectF.right = width - getPaddingRight() - circle_radius + circle_radius / 3;//右边
        rectF.top = getPaddingTop();//上边
        rectF.bottom = height - circle_radius;//下边
        canvas.drawRoundRect(rectF,20,20,mPaint);
    }

    /**
     * 描述：画棕色圆
     * 作者：zzh
     * @param canvas 画笔
     */
    private void drawBrownCircle(Canvas canvas){
        mPaint.reset();
        mPaint.setColor(COLOR_BROWN);
        mPaint.setAntiAlias(true);
        int brown_radius = circle_radius - 5;
        int circle_x = width - getPaddingRight() - circle_radius;
        int circel_y = height - getPaddingBottom() - circle_radius;

        canvas.drawCircle(circle_x,circel_y,brown_radius,mPaint);
    }

    /**
     * 描述：画棕色矩形
     * 作者：zzh
     * @param canvas 画笔
     */
    private void drawBrownRec(Canvas canvas){
        mPaint.reset();
        mPaint.setColor(COLOR_BROWN);
        mPaint.setAntiAlias(true);

        RectF rectF = new RectF();
        rectF.left = width - getPaddingRight() - circle_radius - circle_radius / 3 + 5;
        rectF.right = width - getPaddingRight() - circle_radius + circle_radius / 3 - 5;
        rectF.top = getPaddingTop() + 5;
        rectF.bottom = height - getPaddingBottom() - circle_radius - 5;

        canvas.drawRoundRect(rectF,20,20,mPaint);
    }

    /**
     * 描述：画刻度等
     * 作者：zzh
     * @param canvas 画笔
     */
    private void drawScale(Canvas canvas){
        drawLines(canvas);
        drawText(canvas);
    }

    /**
     * 描述：画灰色刻度线
     * 作者：zzh
     * @param canvas 画笔
     */
    private void drawLines(Canvas canvas) {
        mPaint.reset();
        mPaint.setColor(COLOR_GRAY);
        mPaint.setAntiAlias(true);

        int line_width = circle_radius * 2 / 3 - 10;
        int line_x = width - getPaddingRight() - (circle_radius - 5 + circle_radius) - line_width;

        float begin = 41.0f;
        float recH = height - getPaddingBottom() - circle_radius - circle_radius + 15 - (getPaddingTop() + 5);
        while (begin >= 35){
            float addition = 41.5f - begin;
            float addition_h = addition / (41.5f - 34.5f) * recH;
            int temp = (int) begin;
            if (begin - temp > 0){
                canvas.drawLine(line_x + line_width * 3f / 4,getPaddingTop() + addition_h,line_x + line_width,getPaddingTop() + addition_h,mPaint);
            }else {
                canvas.drawLine(line_x,getPaddingTop() + addition_h,line_x + line_width,getPaddingTop() + addition_h,mPaint);
            }

            BigDecimal b1 = new BigDecimal(Float.toString(begin));
            BigDecimal b2 = new BigDecimal(Float.toString(0.1f));
            begin = b1.subtract(b2).floatValue();
        }

    }

    /**
     * 描述：画刻度文字
     * 作者：zzh
     * @param canvas 画笔
     */
    private void drawText(Canvas canvas) {
        mPaint.reset();
        mPaint.setColor(COLOR_TEXT);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(getResources().getDimension(R.dimen.scale_text_size));
        Typeface font = Typeface.create(Typeface.DEFAULT_BOLD,Typeface.BOLD);
        mPaint.setTypeface(font);
        mPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //计算文字高度
        float fonHeight = fontMetrics.bottom - fontMetrics.top;
        int line_width = circle_radius * 2 / 3 - 10;
        int text_x = width - getPaddingRight() - (circle_radius - 5 + circle_radius) - line_width - line_width / 2 - 15;

        int begin = 41;
        float recH = height - getPaddingBottom() - circle_radius - circle_radius + 15 - (getPaddingTop() + 5);
        while (begin >= 35){
            float addition = 41.5f - begin;
            float addition_h = addition / (41.5f - 34.5f) * recH;
            //计算文字baseline
            canvas.drawText(String.valueOf(begin),text_x,getPaddingTop() + addition_h + fonHeight / 3,mPaint);
            begin --;
        }
    }

    private void drawFG(Canvas canvas){
        drawYellowArc(canvas, temp_temperature);
        drawYellowRec(canvas, temp_temperature);
        drawRecShadow(canvas);
        drawCircleShadow(canvas);
    }

    private void drawYellowArc(Canvas canvas, float value) {
        mPaint.reset();
        mPaint.setColor(COLOR_YELLOW);
        mPaint.setAntiAlias(true);

        RectF r2 = new RectF(); // RectF对象
        r2.left = width - getPaddingRight() - (circle_radius - 5 + circle_radius); // 左边
        r2.top = height - getPaddingBottom() - (circle_radius - 5 + circle_radius); // 上边
        r2.right = width - getPaddingRight() - 5; // 右边
        r2.bottom = height - getPaddingBottom() - 5;

        if (value <= 34.5f) {
            if (value >= 0) {
                float angle = (value / 34.5f) * 360;
                float start_angle = 90 - angle / 2;
                canvas.drawArc(r2, start_angle, angle, false, mPaint);
            }
        } else {
            canvas.drawArc(r2, -90, 360, false, mPaint);
        }
    }

    private void drawYellowRec(Canvas canvas, float temperature2) {
        if (temperature2 <= 34)
            return;

        float addition = temperature2 - 34.5f;
        double rate = addition / (41.5d - 34.5d);
        if (rate > 1f) {
            rate = 1f;
        }

        rate = 1 - rate;

        mPaint.reset();
        mPaint.setColor(COLOR_YELLOW);
        mPaint.setAntiAlias(true);

        float recH = height - getPaddingBottom() - circle_radius - circle_radius + 15 - (getPaddingTop() + 5);
        RectF r2 = new RectF(); // RectF对象
        r2.left = width - getPaddingRight() - circle_radius - circle_radius / 3 + 5; // 左边
        r2.top = (float) (getPaddingTop() + 5 + recH * rate); // 上边
        r2.right = width - getPaddingRight() - circle_radius + circle_radius / 3 - 5; // 右边
        r2.bottom = height - getPaddingBottom() - circle_radius - 5; // 下边

        if ((float) (getPaddingTop() + 5 + recH * rate) - 30 >= (getPaddingTop() + 5)) {
            canvas.drawRoundRect(r2, 0, 0, mPaint);
            RectF r3 = new RectF(); // RectF对象
            r3.left = width - getPaddingRight() - circle_radius - circle_radius / 3 + 5; // 左边
            r3.top = (float) (getPaddingTop() + 5 + recH * rate) - 20; // 上边
            r3.right = width - getPaddingRight() - circle_radius + circle_radius / 3 - 5; // 右边
            r3.bottom = (float) (getPaddingTop() + 5 + recH * rate); // 下边
            canvas.drawRoundRect(r3, 0, 0, mPaint);

            mPaint.setColor(COLOR_BROWN);
            RectF r4 = new RectF(); // RectF对象
            r4.left = width - getPaddingRight() - circle_radius - circle_radius / 3 + 5; // 左边
            r4.top = (float) (getPaddingTop() + 5 + recH * rate) - 30; // 上边
            r4.right = width - getPaddingRight() - circle_radius + circle_radius / 3 - 5; // 右边
            r4.bottom = (float) (getPaddingTop() + 5 + recH * rate); // 下边
            canvas.drawRoundRect(r4, 10, 10, mPaint);
        } else {
            if ((float) (getPaddingTop() + 5 + recH * rate) - 10 >= (getPaddingTop() + 5)) {
                canvas.drawRoundRect(r2, 0, 0, mPaint);
            } else
                canvas.drawRoundRect(r2, 20, 20, mPaint);
        }
    }

    private void drawRecShadow(Canvas canvas) {
        float begin = 41.2f;
        float end = 34.8f;
        float totalH = height - getPaddingBottom() - circle_radius - circle_radius + 15 - (getPaddingTop() + 5);
        float recH = totalH * (begin - end) / (41.5f - 34.5f);
        float beginY = getPaddingTop() + (41.5f - begin) / (41.5f - 34.5f) * totalH;
        //
        Rect r2 = new Rect(); // RectF对象
        r2.left = width - getPaddingRight() - circle_radius - circle_radius / 3 + 5 + circle_radius / 4; // 左边
        r2.top = (int) beginY; // 上边
        r2.right = width - getPaddingRight() - circle_radius + circle_radius / 3 - 5 - circle_radius / 10; // 右边
        r2.bottom = (int) (beginY + recH); // 下边
        // canvas.drawRect(r2, mPaint);

        Drawable d = getResources().getDrawable(R.drawable.temperature_bg);
        d.setBounds(r2);
        d.draw(canvas);
    }

    private void drawCircleShadow(Canvas canvas) {
        mPaint.reset();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(80);

        int radius = (circle_radius - 5) * 2 / 5;

        int circle_x = width - getPaddingRight() - circle_radius * 3 / 4;
        int circle_y = height - getPaddingBottom() - circle_radius * 5 / 4;

        canvas.drawCircle(circle_x, circle_y, radius, mPaint);
    }

    public void setTemperature(float value) {
        if (value < 0f) {
            value = 0;
        } else if (value > 41.5f) {
            value = 41.5f;
        }

        if (value <= 34f) {
            value = new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        } else {
            value = new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        if (temp_temperature > value) {
            temp_temperature = 0f;
        }
        temperature = value;
        invalidate();
    }
}
