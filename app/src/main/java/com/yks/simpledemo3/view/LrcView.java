package com.yks.simpledemo3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.bean.LrcBean;
import com.yks.simpledemo3.tools.LrcUtil;

import java.util.List;

/**
 * 描述：自定义歌词滚动
 * 作者：zzh
 * time:2019/10/26
 * https://blog.csdn.net/u012702547/article/details/52891387
 * https://github.com/lenve/LrcView/blob/master/lrcview/src/main/java/org/sang/lrcview/LrcView.java
 */
public class LrcView extends View {

    private int lrcColor,lrcLineColor;
    private int mode = 0;//模式
    private List<LrcBean> list;
    private Paint lrcPaint,linePaint;
    private int width = 0,height = 0;
    private int currentPosition = 0;
    private int lastPosition = 0;
    private MediaPlayer player;

    public LrcView(Context context) {
        this(context,null);
    }

    public LrcView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取参数信息
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LrcView);
        lrcColor = array.getColor(R.styleable.LrcView_lrcColr,getResources().getColor(R.color.colorPrimary));//歌词颜色
        lrcLineColor = array.getColor(R.styleable.LrcView_lrcLineColor,getResources().getColor(R.color.colorGray3));//线的颜色
        mode = array.getInt(R.styleable.LrcView_lrcMode,mode);//模式
        array.recycle();//释放
        //todo 歌词paint
        lrcPaint = new Paint();
        lrcPaint.setAntiAlias(true);
        lrcPaint.setColor(lrcColor);
        lrcPaint.setTextSize(24);
        lrcPaint.setTextAlign(Paint.Align.CENTER);
        //todo 当前播放歌词颜色
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(lrcLineColor);
        linePaint.setTextSize(24);
        linePaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0 || height == 0){
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }
        if (list == null || list.size() == 0){
            canvas.drawText("暂无歌词",width / 2,height / 2,lrcPaint);
            return;
        }

        getCurrentPosition();

        int currentMillis = player.getCurrentPosition();
        drawLrc2(canvas,currentMillis);
        long start = list.get(currentPosition).getStart();
        float v = (currentMillis - start) > 500 ? currentPosition * 80 : lastPosition * 80 + (currentPosition - lastPosition) * 80 * ((currentMillis - start) / 500f);
        setScrollY((int) v);
        if (getScrollY() == currentPosition * 80){
            lastPosition = currentPosition;
        }
        postInvalidateDelayed(100);
    }

    /**
     * 描述：绘制歌词
     * 作者：zzh
     * @param canvas 画布
     * @param currentMills 当前毫秒
     */
    private void drawLrc2(Canvas canvas,int currentMills){
        if (mode == 0){//默认模式
            for (int i=0;i<list.size();i++){
                if (i == currentPosition){
                    canvas.drawText(list.get(i).getLrc(),width / 2,height / 2 + 80 * i,lrcPaint);
                }else {
                    canvas.drawText(list.get(i).getLrc(),width / 2,height / 2 + 80 * i,linePaint);
                }
            }
        }else {//卡拉OK模式
            for (int i=0;i<list.size();i++){
                canvas.drawText(list.get(i).getLrc(),width / 2,height / 2 + 80 * i,linePaint);
            }

            String lineLrc = list.get(currentPosition).getLrc();
            int lineWidth = (int) linePaint.measureText(lineLrc);
            int leftoffset = (width - lineWidth) / 2;
            LrcBean bean = list.get(currentPosition);
            long start = bean.getStart();
            long end = bean.getEnd();
            int i = (int) ((currentMills - start) * 1.0f / (end - start) * lineWidth);
            if (i > 0){
                Bitmap textBitmap = Bitmap.createBitmap(i,80, Bitmap.Config.RGB_565);
                Canvas textCanvas = new Canvas(textBitmap);
                textCanvas.drawText(lineLrc,lineWidth / 2,80,lrcPaint);
                canvas.drawBitmap(textBitmap,leftoffset,height / 2 + 80 * (currentPosition - 1),null);
            }
        }
    }

    /**
     * 描述：实时获取当前歌词应该所在的位置
     * 作者：zzh
     */
    private void getCurrentPosition(){
        try {
            int currentMills = player.getCurrentPosition();
            if (currentMills < list.get(0).getStart()){//小于开始时间，从开始时间开始
                currentPosition = 0;
                return;
            }

            if (currentMills > list.get(list.size()-1).getStart()){//大于最后一句的开始时间
                currentPosition = list.size() - 1;
                return;
            }

            for (int i=0;i<list.size();i++){
                if (currentMills >= list.get(i).getStart() && currentMills < list.get(i).getEnd()){
                    currentPosition = i;
                    return;
                }
            }
        }catch (Exception e){
            postInvalidateDelayed(100);
        }
    }

    /**
     * 描述：初始化参数
     * 作者：zzh
     */
    public void init(){
        currentPosition = 0;
        lastPosition = 0;
        setScrollY(0);
        invalidate();
    }

    /**
     * 描述：设置歌词颜色
     * 作者：zzh
     * @param lrcColor 歌词颜色
     */
    public void setLrcColor(int lrcColor){
        this.lrcColor = lrcColor;
    }

    /**
     * 描述：设置正在播放的歌词的颜色
     * 作者：zzh
     * @param lineColor 正在播放的歌词颜色
     */
    public void setLrcLineColor(int lineColor){
        this.lrcLineColor = lineColor;
    }

    /**
     * 描述：设置播放模式，0为默认模式，1为卡拉OK模式
     * @param mode
     */
    public void setMode(int mode){
        this.mode = mode;
    }

    /**
     * 描述：设置播放器
     * 作者：zzh
     * @param player 播放器
     */
    public void setPlayer(MediaPlayer player){
        this.player = player;
    }

    /**
     * 描述：设置歌词
     * 作者：zzh
     * @param lrc 歌词
     */
    public void setLrc(String lrc){
        list = LrcUtil.parseStr2List(lrc);
    }
}
