package com.yks.simpledemo3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.yks.simpledemo3.R;

/**
 * 描述：印章
 * 作者：zzh
 * time:2020/03/06
 * https://www.cnblogs.com/frames/p/4402689.html
 */
public class SealView extends View {

    private float textSize;
    private int OUTSIDE_RING_WIDTH; //圆环宽度
    private static final int RADIUS = 180;  //印章半径
    private static final float TEXT_ANGLE = 180f;  //文字扇形排列的角度
    private static final float STAMP_ROTATE = 30f;  //印章旋转角度
    private Paint mCirclePaint,mStarPaint,mTextPaint;
    private String topText,bottomText;

    public SealView(Context context) {
        this(context,null);
    }

    public SealView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SealView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SealView);
        textSize = array.getFloat(R.styleable.SealView_seal_textSize,20f);
        int textColor = array.getColor(R.styleable.SealView_seal_textColor, Color.RED);
        OUTSIDE_RING_WIDTH = array.getInteger(R.styleable.SealView_seal_ringHeight,5);
        array.recycle();

        mCirclePaint = new Paint();
        mCirclePaint.setColor(textColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);

        mStarPaint = new Paint();
        mStarPaint.setColor(textColor);

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int mWidth = getWidth();
        int mHeight = getHeight();

        float textY = (mHeight / 2) - RADIUS + OUTSIDE_RING_WIDTH + textSize;
        //画圆
        mCirclePaint.setStrokeWidth(OUTSIDE_RING_WIDTH);
        canvas.drawCircle(mWidth/2, mHeight/2, RADIUS, mCirclePaint);
        canvas.save();
        canvas.rotate(STAMP_ROTATE,mWidth / 2,mHeight / 2);
        //画顶部文字
        canvas.rotate(-(TEXT_ANGLE/4*3),mWidth/2,mHeight/2);
        char[] chs = topText.toCharArray();
        float spaceAngle = TEXT_ANGLE / (chs.length - 1);
        for (int i=0;i<chs.length;i++){
            String a = String.valueOf(chs[i]);
            canvas.drawText(a,mWidth/2,textY,mTextPaint);
            canvas.rotate(spaceAngle,mWidth/2,mHeight/2);
        }
        canvas.save();
        //画底部文字
        canvas.rotate(-(TEXT_ANGLE/2),mWidth/2,mHeight/2);
        canvas.drawText(bottomText,mWidth/2,mHeight/4*3,mTextPaint);
        canvas.save();

        //画五角星
        canvas.rotate(TEXT_ANGLE+textSize*3/2,mWidth/2,mHeight/2);
        Path mPath = new Path();
        float[] floats = fivePoints(mWidth/2,mHeight/2,100);
        for (int i=0;i<floats.length;i++){
            mPath.lineTo(floats[i],floats[i+=1]);
        }
        canvas.drawPath(mPath,mStarPaint);
        canvas.restore();
    }


    /**
     * @param xA 起始点位置A的x轴绝对位置
     * @param yA 起始点位置A的y轴绝对位置
     * @param rFive 五角星边的边长
     */
    public static float[] fivePoints(float xA, float yA, int rFive) {
        float xB = 0;
        float xC = 0;
        float xD = 0;
        float xE = 0;
        float yB = 0;
        float yC = 0;
        float yD = 0;
        float yE = 0;
        xD = (float) (xA - rFive * Math.sin(Math.toRadians(18)));
        xC = (float) (xA + rFive * Math.sin(Math.toRadians(18)));
        yD = yC = (float) (yA + Math.cos(Math.toRadians(18)) * rFive);
        yB = yE = (float) (yA + Math.sqrt(Math.pow((xC - xD), 2) - Math.pow((rFive / 2), 2)));
        xB = xA + (rFive / 2);
        xE = xA - (rFive / 2);
        float[] floats = new float[]{xA, yA,  xD, yD,xB, yB, xE, yE, xC, yC,xA, yA};
        return floats;
    }
    /**
     * 描述：设置印章要显示的内容
     * 作者：zzh
     * @param topText 顶部的文字
     * @param bottomText 底部的文字
     */
    public void setSealInfo(String topText,String bottomText){
        this.topText = topText;
        this.bottomText = bottomText;
        invalidate();
    }
}
