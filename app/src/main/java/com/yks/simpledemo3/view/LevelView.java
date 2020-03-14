package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.yks.simpledemo3.R;

/**
 * 描述：水平仪
 * 作者：zzh
 * time:2020/03/13
 */
public class LevelView extends View {

    private Bitmap back;//水平仪大圆盘图片
    private Bitmap bubble;//水平仪气泡图标
    public int bubbleX,bubbleY;//水平仪气泡x，y的坐标
    private int MAX_ANGLE = 30;//灵敏度

    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取窗口管理器
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //获取屏幕的高度和宽度
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        //int screenHeight = metrics.heightPixels;

        //创建位图
        back = Bitmap.createBitmap(screenWidth, screenWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(back);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //设置绘制风格：仅填充
        paint.setStyle(Paint.Style.FILL);
        //创建一个线性渐变来绘制线性渐变
        Shader shader = new LinearGradient(0, screenWidth, screenWidth * 0.8f, screenWidth * 0.2f,
                Color.YELLOW, Color.WHITE, Shader.TileMode.MIRROR);
        paint.setShader(shader);
        //绘制圆形
        canvas.drawCircle(screenWidth / 2, screenWidth / 2, screenWidth / 2 - MAX_ANGLE, paint);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        //设置绘制风格：仅绘制边框
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(2);
        paint2.setColor(Color.BLACK);
        //绘制圆形边框
        canvas.drawCircle(screenWidth / 2, screenWidth / 2, screenWidth / 2 - MAX_ANGLE, paint2);
        //绘制水平横线
        canvas.drawLine(MAX_ANGLE, screenWidth / 2, screenWidth, screenWidth / 2, paint2);
        //绘制垂直横线
        canvas.drawLine(screenWidth / 2 , MAX_ANGLE, screenWidth / 2 , screenWidth - MAX_ANGLE, paint2);
        //设置画笔宽度
        paint2.setStrokeWidth(10);
        paint2.setColor(Color.RED);
        //绘制中心的红色“十字”
        canvas.drawLine(screenWidth / 2 - MAX_ANGLE * 2, screenWidth / 2, screenWidth / 2 + MAX_ANGLE*2,
                screenWidth / 2, paint2);
        canvas.drawLine(screenWidth / 2, screenWidth / 2 - MAX_ANGLE*2, screenWidth / 2,
                screenWidth / 2 + MAX_ANGLE*2, paint2);
        //加载气泡图片
        bubble = BitmapFactory.decodeResource(getResources(), R.mipmap.small);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制水平仪表盘
        canvas.drawBitmap(back, 0, 0, null);
        //根据气泡坐标绘制气泡
        canvas.drawBitmap(bubble, bubbleX, bubbleY, null);
    }

    public void updateLevelViewBubble(float zAngle,float yAngle){
        //气泡位于中间时（水平仪完全水平）
        int x = (back.getWidth() - bubble.getWidth()) / 2;
        int y = (back.getHeight() - bubble.getHeight()) / 2;
        //如果与Z轴的倾斜角还在最大角度之内
        if (Math.abs(zAngle) <= MAX_ANGLE) {
            //根据与Z轴的倾斜角度计算X坐标轴的变化值
            int deltaX = (int) ((back.getWidth() - bubble.getWidth()) / 2
                    * zAngle / MAX_ANGLE);
            x += deltaX;
        }
        //如果与Z轴的倾斜角已经大于MAX_ANGLE，气泡应到最左边
        else if (zAngle > MAX_ANGLE) {
            x = 0;
        }
        //如果与Z轴的倾斜角已经小于负的Max_ANGLE,气泡应到最右边
        else {
            x = back.getWidth() - bubble.getWidth();
        }

        //如果与Y轴的倾斜角还在最大角度之内
        if (Math.abs(yAngle) <= MAX_ANGLE) {
            //根据与Z轴的倾斜角度计算X坐标轴的变化值
            int deltaY = (int) ((back.getHeight() - bubble.getHeight()) / 2
                    * yAngle / MAX_ANGLE);
            y += deltaY;
        }
        //如果与Y轴的倾斜角已经大于MAX_ANGLE，气泡应到最下边
        else if (yAngle > MAX_ANGLE) {
            y = back.getHeight() - bubble.getHeight();
        }
        //如果与Y轴的倾斜角已经小于负的Max_ANGLE,气泡应到最上边
        else {
            y = 0;
        }
        //如果计算出来的X，Y坐标还位于水平仪的仪表盘之内，则更新水平仪气泡坐标
        if (true) {
            bubbleX = x;
            bubbleY = y;
            postInvalidate();
        }
    }
    private boolean isContain(int x, int y) {
        //计算气泡的圆心坐标X，y
        int bubbleCx = x + bubble.getWidth() / 2;
        int bubbleCy = y + bubble.getWidth() / 2;
        //计算水平仪仪表盘圆心的坐标
        int backCx = back.getWidth() / 2;
        int backCy = back.getWidth() / 2;
        //计算气泡的圆心与水平仪表盘的圆心之间的距离
        double distance = Math.sqrt((bubbleCx - backCx) * (bubbleCx * backCx) +
                (bubbleCy - backCy) * (bubbleCy - backCy));
        //若两圆心的距离小于他们的半径差，即可认为处于该点的气泡任然位于仪表盘内
        if (distance < (back.getWidth() - bubble.getWidth())) {
            return true;
        } else {
            return false;
        }
    }
}
