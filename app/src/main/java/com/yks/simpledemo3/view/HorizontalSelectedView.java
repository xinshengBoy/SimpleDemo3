package com.yks.simpledemo3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yks.simpledemo3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：横向滑动选择控件
 * 作者：zzh
 * time:2019/12/19
 * https://github.com/385841539/HorizontalScrollSelectedView/blob/master/horizontalselectedviewlibrary/src/main/java/com/example/horizontalselectedviewlibrary/HorizontalselectedView.java
 */
public class HorizontalSelectedView extends View {

    private TextPaint textPaint;
    private TextPaint selectedPaint;
    private int seeSize = 5;//可见数量
    private float selectedTextSize,noSelectTextSize;
    private int selectedTextColor,noSelectTextColor;
    private int width,height;//控件的宽高
    private int textWidth = 0,textHeight = 0;//文字的宽高
    private int anInt;//每个字母所占的大小
    private int n;//todo 当前选中文字的数组下标
    private float downX,anOffset;
    private boolean isFirstVisible = true;//是否第一次进来
    private List<String> mList = new ArrayList<>();//数据源
    private Rect rect = new Rect();

    public HorizontalSelectedView(Context context) {
        this(context,null);
    }

    public HorizontalSelectedView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalSelectedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setClickable(true);
        initAttrs(context,attrs);
        initPaint();
    }

    /**
     * 初始化配置参数
     * @param context 上下文
     * @param attrs 配置参数
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HorizontalSelectedView);
        seeSize = array.getInteger(R.styleable.HorizontalSelectedView_seeSize,5);//滑动时可见数量
        selectedTextSize = array.getFloat(R.styleable.HorizontalSelectedView_selectedTextSize,50);//已选文字大小
        selectedTextColor = array.getColor(R.styleable.HorizontalSelectedView_selectedTextColor,context.getResources().getColor(R.color.colorGreen));//已选文字颜色
        noSelectTextSize = array.getFloat(R.styleable.HorizontalSelectedView_noSelectedTextSize,40);//未选文字大小
        noSelectTextColor = array.getColor(R.styleable.HorizontalSelectedView_noSelectedTextColor,context.getResources().getColor(R.color.colorGray2));
        array.recycle();
    }

    /**
     * 描述：初始化画笔
     */
    private void initPaint() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(noSelectTextSize);
        textPaint.setColor(noSelectTextColor);

        selectedPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setTextSize(selectedTextSize);
        selectedPaint.setColor(selectedTextColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirstVisible){
            //todo 第一次绘制的时候得到控件的宽高
            width = getWidth();
            height = getHeight();
            anInt = width / seeSize;//每个文字的长度
            isFirstVisible = false;
        }

        if (n >= 0 && n <= mList.size() - 1){
            String s = mList.get(n);//得到选中的文字
            //绘制所需的宽高
            selectedPaint.getTextBounds(s,0,s.length(),rect);
            //从矩形区域中获取文本内容的宽高
            int centerTextWidth = rect.width();
            int centerTextHeight = rect.height();
            //todo 画选中的文字
            canvas.drawText(s,getWidth()/2 - centerTextWidth / 2 + anOffset,getHeight() / 2 + centerTextHeight / 2,selectedPaint);
            //todo 画未选中的文字
            for (int i=0;i<mList.size();i++){
                if (n > 0 && n < mList.size() - 1){//这里主要是因为strings数据源的文字长度不一样，为了让被选中两边文字距离中心宽度一样，我们取得左右两个文字长度的平均值
                    textPaint.getTextBounds(mList.get(n - 1),0,mList.get(n - 1).length(),rect);
                    int width1 = rect.width();
                    textPaint.getTextBounds(mList.get(n + 1),0,mList.get(n + 1).length(),rect);
                    int width2 = rect.width();
                    textWidth = (width1 + width2) / 2;//取左右两个文字长度的中间值
                }
                if (i == 0){//得到宽高一致时则无所谓
                    textPaint.getTextBounds(mList.get(0),0,mList.get(0).length(),rect);
                    textHeight = rect.height();
                }

                if (i != n){
                    canvas.drawText(mList.get(i),(i - n) * anInt + getWidth() / 2 - textWidth / 2 + anOffset,getHeight() / 2 + textHeight / 2,textPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();//记录点下去的x坐标
                break;
            case MotionEvent.ACTION_MOVE:
                float scrollX = event.getX();
                if (n != 0 && n != mList.size() - 1){
                    anOffset = scrollX - downX;//todo 滑动时的偏移量，用于计算每个文字的坐标值
                }else {
                    anOffset = (float) ((scrollX - downX) / 1.5);//todo 滑到两端时添加一点阻力
                }

                if (scrollX > downX){//todo 向右滑
                    //todo 当滑到距离大于每个单元的长度时，则改变选中的文字
                    if ((scrollX - downX >= anInt) && n > 0){
                        anOffset = 0;
                        n -= 1;//显示当前文字的上一个文字
                        downX = scrollX;//把按下的坐标点替换成当前滑动的坐标点
                    }
                }else {//todo 向左滑动
                    if ((downX - scrollX >= anInt) && n < mList.size() - 1){
                        anOffset = 0;
                        n += 1;
                        downX = scrollX;
                    }
                }
                invalidate();//todo 刷新视图
                break;
            case MotionEvent.ACTION_UP:
                //抬起手指时，偏移量归零
                anOffset = 0;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 描述：设置数据源
     * @param list 数据集合
     */
    public void setData(List<String> list){
        this.mList = list;
        n = mList.size() / 2;//默认展示此数据最中间的一个
        invalidate();
    }

    /**
     * 描述：获取当前选中的文字内容
     * @return 文字内容
     */
    public String getSelectedString(){
        if (mList.size() != 0){
            return mList.get(n);
        }
        return null;
    }

    /**
     * 描述：右移一个单元格
     */
    public void setAnRightOffset(){
        if (n < mList.size() - 1){
            n += 1;//todo 右移是要显示下一个
            invalidate();
        }
    }

    /**
     * 描述：左移一个单元格
     */
    public void setAnLeftOffset(){
        if (n > 0){
            n -= 1;//todo 左移是要显示上一个
            invalidate();
        }
    }

    /**
     * 描述：设置控件要显示的文字个数
     * @param size 个数
     */
    public void setSeeSize(int size){
        if (size > 0){
            seeSize = size;
            invalidate();
        }
    }
}
