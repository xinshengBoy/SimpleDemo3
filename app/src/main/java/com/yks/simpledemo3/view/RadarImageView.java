package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yks.simpledemo3.R;


/**
 * 描述：雷达实现的第二种方法，仿微信雷达搜索
 * 实现方式，星空背景图，然后让带圈圈的图片匀速的逆时针旋转，达到扫描的效果
 * 作者：zzh
 * time:2019/08/03
 * https://blog.csdn.net/qq_35114086/article/details/70053116
 */
public class RadarImageView extends ImageView {

    private Matrix matrix;
    private int width,height;
    private  int degrees;
    public RadarImageView(Context context) {
        this(context,null);
    }

    public RadarImageView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setBackgroundResource(R.mipmap.radar_bg);
        matrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setMatrix(matrix);
        super.onDraw(canvas);
        matrix.reset();
    }

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            degrees += 3;
            matrix.postRotate(degrees,width / 2,height / 2);
            RadarImageView.this.invalidate();//重绘
            mHandler.postDelayed(runnable,32);
        }
    };

    public void start(){
        mHandler.postDelayed(runnable,500);
    }

    public void stop(){
        mHandler.removeCallbacks(runnable);
    }
}
