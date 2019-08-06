package com.yks.simpledemo3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 描述：自定义扫描雷达
 * 作者：zzh
 * time:2019/08/03
 * https://github.com/donkingliang/RadarView/blob/master/app/src/main/java/com/donkingliang/radar/RadarView.java
 */
public class RadarView extends View {
    //初始化属性值
    private int DEFAULT_COLOR = Color.parseColor("#91D7F4");
    private int radarCicleColor = DEFAULT_COLOR;//圆圈和交叉线的颜色
    private int radarCircleNum = 3;//圆圈数量,最小为3个圆圈
    private int radarSweepColor = DEFAULT_COLOR;//扫描的颜色
    private int radarRainDropColor = DEFAULT_COLOR;//水滴的颜色
    private int radarRainDropNum = 4;//水滴数量，最多4个
    private boolean radarShowCross = true;//是否显示交叉线，默认显示
    private boolean radarShowRainDrop = true;//是否显示水滴，默认显示
    private float radarSpeed = 3.0f;//扫描的转速
    private float radarFlicker = 3.0f;//水滴显示和消失的速度
    //画笔
    private Paint mCirclePaint;//圆圈的画笔
    private Paint mRainDropPaint;//水滴的画笔
    private Paint mSweepPaint;//扫描效果的画笔
    private float mDegrees; //扫描时的扫描旋转角度。
    private boolean isScanning = false;//是否扫描
    //保存水滴数据
    private ArrayList<RainDrop> mRaindrops = new ArrayList<>();

    public RadarView(Context context) {
        this(context,null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadarView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttr(context,attrs);
        init();
    }

    /**
     * 描述：获取属性值
     * 作者：zzh
     * @param context 上下文
     * @param attrs 属性值
     */
    private void getAttr(Context context, AttributeSet attrs) {
        if (attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
            radarCicleColor = array.getColor(R.styleable.RadarView_radarCicleColor,DEFAULT_COLOR);//圆圈和交叉线的颜色
            radarCircleNum = array.getInt(R.styleable.RadarView_radarCircleNum,radarCircleNum);//圆圈数量
            if (radarCircleNum < 3) {//圆圈数量不少于三个
                radarCircleNum = 3;
            }
            radarSweepColor = array.getColor(R.styleable.RadarView_radarSweepColor,DEFAULT_COLOR);//扫描的颜色
            radarRainDropColor = array.getColor(R.styleable.RadarView_radarRainDropColor,DEFAULT_COLOR);//水滴的颜色
            radarRainDropNum = array.getInt(R.styleable.RadarView_radarRainDropNum,radarRainDropNum);//水滴数量
            if (radarRainDropNum > 4){
                radarRainDropNum = 4;
            }
            radarShowCross = array.getBoolean(R.styleable.RadarView_radarShowCross,true);//是否显示交叉线，默认显示
            radarShowRainDrop = array.getBoolean(R.styleable.RadarView_radarShowRainDrop,true);//是否显示水滴，默认显示
            radarSpeed = array.getFloat(R.styleable.RadarView_radarSpeed,radarSpeed);//扫描的转速
            if (radarSpeed < 3){
                radarSpeed = 3;
            }
            radarFlicker = array.getFloat(R.styleable.RadarView_radarFlicker,radarFlicker);//水滴显示和消失的速度
            if (radarFlicker < 3){
                radarFlicker = 3;
            }
            array.recycle();//资源回收
        }
    }

    /**
     * 描述：初始化
     */
    private void init(){
        //圆圈的画笔
        mCirclePaint = new Paint();
        mCirclePaint.setColor(radarCicleColor);
        mCirclePaint.setStrokeWidth(1);;
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);//消除锯齿
        //水滴的画笔
        mRainDropPaint = new Paint();
        mRainDropPaint.setStyle(Paint.Style.FILL);
        mRainDropPaint.setAntiAlias(true);
        //扫描效果的画笔
        mSweepPaint = new Paint();
        mSweepPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置宽高，默认200
        int defaultSize = Info.dp2px(getContext(),200);
        setMeasuredDimension(measureValues(widthMeasureSpec,defaultSize,true),measureValues(heightMeasureSpec,defaultSize,false));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //计算圆的半径
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int radius = Math.min(width,height) / 2;
        //计算圆心
        int cx = getPaddingLeft() + width / 2;
        int cy = getPaddingTop() + height / 2;
        //画圆
        for (int i=0;i<radarCircleNum;i++){
            canvas.drawCircle(cx,cy,radius - (radius / radarCircleNum * i),mCirclePaint);
        }

        //画交叉线
        if (radarShowCross){
            //水平线
            canvas.drawLine(cx - radius,cy,cx + radius,cy,mCirclePaint);
            //垂直线
            canvas.drawLine(cx,cy - radius,cx,cy + radius,mCirclePaint);
        }

        //正在扫描
        if (isScanning){
            if (radarShowRainDrop){
                drawRainDrop(canvas,cx,cy,radius);//画雨滴
            }
            //画旋转颜色渐变效果
            drawSweep(canvas,cx,cy,radius);
            //计算雷达扫描的旋转角度
            mDegrees = (mDegrees + (360 / radarSpeed / 60)) % 360;
            //重新绘制
            invalidate();
        }
    }

    /**
     * 描述：画雨点
     * @param canvas 画布
     * @param cx 中点x的位置
     * @param cy 中点y的位置
     * @param radius 半径
     */
    private void drawRainDrop(Canvas canvas,int cx,int cy,int radius){
        generateRainDrop(cx,cy,radius);
        for (RainDrop drop : mRaindrops){
            mRainDropPaint.setColor(drop.changeAlpha());
            canvas.drawCircle(drop.x,drop.y,drop.radius,mRainDropPaint);
            //水滴的渐变效果
            drop.radius += 1.0f * 20 / 60 / radarFlicker;
            drop.alpha -= 1.0f * 255 / 60 / radarFlicker;
        }
        removeRainDrop();
    }

    /**
     * 描述：生成水滴
     * 作者：zzh
     * @param cx x点
     * @param cy y点
     * @param radius 半径
     */
    private void generateRainDrop(int cx,int cy,int radius){
        //最多只能同时存在固定最大的水滴数
        if (mRaindrops.size() < radarRainDropNum){
            //随机一个20以内的数字，如果这个数字刚好是0，就生成水滴，这样来控制水滴生成的概率
            boolean probability = (int) (Math.random() * 20) == 0;
            if (probability){
                //生成一个水滴
                int x = 0;
                int y = 0;
                int xoffset = (int) (Math.random() * (radius - 20));
                int yoffset = (int) (Math.random() * (int)Math.sqrt(1.0 * (radius - 20) * (radius - 20) - xoffset * xoffset));
                if ((int) (Math.random() * 2) ==0){
                    x = cx - xoffset;
                }else {
                    x = cx + xoffset;
                }

                if ((int) (Math.random() * 2) == 0){
                    y = cy - yoffset;
                }else {
                    y = cy + yoffset;
                }
                mRaindrops.add(new RainDrop(x,y,0,radarRainDropColor));
            }
        }
    }

    /**
     * 描述：删除雨滴
     * 作者：zzh
     */
    private void removeRainDrop() {
        Iterator<RainDrop> iterator = mRaindrops.iterator();
        while (iterator.hasNext()){
            RainDrop drop = iterator.next();
            if (drop.radius > 20 || drop.alpha < 0){
                iterator.remove();
            }
        }
    }

    /**
     * 描述：画扫描效果
     * @param canvas 画布
     * @param cx x
     * @param cy y点
     * @param radius  半径
     */
    private void drawSweep(Canvas canvas,int cx,int cy,int radius){
        //扇形的透明渐变效果
        SweepGradient gradient = new SweepGradient(cx,cy,new int[]{Color.TRANSPARENT,changeAlpha(radarSweepColor,0),changeAlpha(radarSweepColor,168),changeAlpha(radarSweepColor,255),changeAlpha(radarSweepColor,255)},
                new float[]{0.0f,0.6f,0.99f,0.998f,1f});
        mSweepPaint.setShader(gradient);
        //先旋转画布，再绘制扫描的颜色渲染，实现扫描时的旋转效果
        canvas.rotate(-90 + mDegrees,cx,cy);
        canvas.drawCircle(cx,cy,radius,mSweepPaint);
    }

    /**
     * 描述：改变颜色的透明度
     * 作者：zzh
     * @param color 颜色值
     * @param alpha 要改变的透明度
     * @return 新的颜色值
     */
    private static int changeAlpha(int color, int alpha){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha,red,green,blue);
    }

    /**
     * 描述;测量宽高
     * 作者：zzh
     * @param spec 参数
     * @param defaultSize 默认值
     * @param isWidth 是否计算长度
     * @return 返回值
     */
    private int measureValues(int spec,int defaultSize,boolean isWidth){
        int result = 0;
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);
        if (specMode == MeasureSpec.EXACTLY){//精确模式
            result = specSize;
        }else {
                result = isWidth ? (getPaddingLeft() + getPaddingRight()) : (getPaddingTop() + getPaddingBottom()) + defaultSize;
            if (specMode == MeasureSpec.AT_MOST){//填充模式
                result = Math.min(result,specSize);
            }
        }
        result = Math.max(result, isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight());
        return result;
    }

    private class RainDrop {
        int x;
        int y;
        float radius;
        int color;
        float alpha = 255;//完全显示

        public RainDrop(int x, int y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }

        /**
         * 描述：获取改变透明度后的颜色值
         * @return
         */
        public int changeAlpha(){
            return RadarView.changeAlpha(color,(int)alpha);
        }
    }

    /**
     * 描述：开始扫描
     */
    public void start(){
        if (!isScanning){
            isScanning = true;
            invalidate();
        }
    }
    /**
     * 描述：结束扫描，对外暴露方法
     */
    public void stop(){
        if (isScanning){
            isScanning = false;
            mRaindrops.clear();
            mDegrees = 0.0f;
        }
    }
}
