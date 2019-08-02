package com.yks.simpledemo3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yks.simpledemo3.R;

/**
 * 描述：自定义圆形滑动菜单
 * 作者：zzh
 * time:2019/08/01
 * https://github.com/hongyangAndroid/Android-CircleMenu/blob/master/library_zhy_CircleMenu/src/com/zhy/view/CircleMenuLayout.java
 */
public class CircleMenuView extends ViewGroup {
    //外圈半径
    private int mRadius;
    //todo 该容器的内边距,无视padding属性，如需边距请用该变量
    private float mPadding;
    //todo 该容器内child item的默认尺寸
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
    // todo 菜单的中心child的默认尺寸
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;
    //todo 该容器的内边距,无视padding属性，如需边距请用该变量
    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;
    //todo 当每秒移动角度达到该值时，认为是快速移动
    private static final int FLINGABLE_VALUE = 300;
    private int mFlingableValue = FLINGABLE_VALUE;
    /**
     * 如果移动角度达到该值，则屏蔽点击
     */
    private static final int NOCLICK_VALUE = 3;
    //todo 开始的角度
    private double mStartAngle = 0;
    //todo 菜单的文本
    private String [] mItemTexts;
    //todo 菜单的图标
    private int [] mItemImages;
    //todo 菜单的个数
    private int mMenuItemCount;
    //todo 检测按下到抬起时旋转的角度
    private float mTmpAngle;
    //todo 检测按下到抬起时使用的时间
    private long mDownTime;
    //todo 判断是否正在自动滚动
    private boolean isFling;
    private int mMenuItemLayoutId = R.layout.circle_menu_item;
    private AutoFlingRunnable mFlingRunnable;
    //todo 记录上一次的坐标
    private float mLastX,mLastY;

    public CircleMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0,0,0,0);
    }

    /**
     * 描述：设置布局的宽高，并策略menu item的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;
        //todo 根据传入的参数，分别获取测量模式和测量值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //todo 根据测量模式确定精确值
        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY){
            //非精确模式
            //设置背景图的高度
            resWidth = getSuggestedMinimumWidth();
            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;
            resHeight = getSuggestedMinimumHeight();
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        }else {
            //精确模式，直接取小值
            resWidth = resHeight = Math.min(width,height);
        }

        setMeasuredDimension(resWidth,resHeight);
        //获得半径
        mRadius = Math.max(getMeasuredWidth(),getMeasuredHeight());
        //menu item的数量
        final int count = getChildCount();
        //menu item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        //menu item的测量模式
        int childMode = MeasureSpec.EXACTLY;
        //迭代测量
        for (int i=0;i<count;i++){
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE){
                continue;
            }
            //计算尺寸
            int makeMeasureSpec = -1;
            if (child.getId() == R.id.id_circle_menu_item_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION), childMode);
            }else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,childMode);
            }
            child.measure(makeMeasureSpec,makeMeasureSpec);
        }
        mPadding = RADIO_PADDING_LAYOUT * mRadius;
    }

    /**
     * 描述：获取默认宽高
     * 作者：zzh
     * @return 默认宽高
     */
    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return Math.min(metrics.widthPixels,metrics.heightPixels);
    }
    /**
    * 描述：设置位置
     */
     @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutRadius = mRadius;
        final int childCount = getChildCount();
        int left,top;
        //menu item的尺寸
        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        //根据menu item的个数，计算角度
        float angleDelay = 360 / (getChildCount() - 1);
        //遍历去设置每个menu item的位置
        for (int i=0;i<childCount;i++){
            final  View child = getChildAt(i);
            if (child.getId() == R.id.id_circle_menu_item_center) {
                continue;
            }
            if (child.getVisibility() == GONE){
                continue;
            }
            mStartAngle %= 360;
            //计算中心点到menu item中心的距离
            float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;
            //menu item的横坐标
            left = layoutRadius / 2 + (int)Math.round(tmp * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f * cWidth);
            //menu item的纵坐标
            top = layoutRadius / 2 + (int)Math.round(tmp * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f * cWidth);
            child.layout(left,top,left + cWidth,top + cWidth);
            //叠加尺寸
            mStartAngle += angleDelay;
        }

        //找到中心的view，如果存在就设置点击事件
        View cView = findViewById(R.id.id_circle_menu_item_center);
        if (cView != null){
            cView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null){
                        mOnMenuItemClickListener.itemCenterClick(v);
                    }
                }
            });
            //设置中间的位置
            int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
            int cr = cl + cView.getMeasuredWidth();
            cView.layout(cl,cl,cr,cr);
        }
    }
    //todo 定义点击事件
    private OnMenuItemClickListener mOnMenuItemClickListener;
    public interface OnMenuItemClickListener{
        void itemClick(View view,int pos);
        void itemCenterClick(View view);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener){
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    /**
     * 描述：移动事件
     * 作者：zzh
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN://todo 手指按下时
                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngle = 0;
                //如果当前已经在快速滚动
                if (isFling){
                    //移除快速滚动的回调
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE://todo 手指按下移动时
                //获得开始角度
                float start = getAngle(mLastX,mLastY);
                //获得当前角度
                float end = getAngle(x,y);
                //一、四象限，end - start，角度值为正值
                if (getQuadrant(x,y) == 1 || getQuadrant(x,y) == 4){
                    mStartAngle += (end - start);
                    mTmpAngle += (end - start);
                }else {
                    //二、三象限，角度值是负值
                    mStartAngle += (start - end);
                    mTmpAngle += (start - end);
                }
                //重新载入布局
                requestLayout();

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP://todo 抬起手指时
                //计算每秒移动的角度
                float anglePerSecond = mTmpAngle * 1000 / (System.currentTimeMillis() - mDownTime);
                //如果是快速移动
                if (Math.abs(anglePerSecond) > mFlingableValue && !isFling){
                    //post一个任务，去自动滚动
                    post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));
                    return true;
                }

                //如果当前旋转角度超过则屏蔽点击
                if (Math.abs(mTmpAngle) > NOCLICK_VALUE){
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //主要是为了action_down时，返回true
        return true;
    }

    private class AutoFlingRunnable implements Runnable{
        private float angelPerSecond;
        public AutoFlingRunnable(float velocity){
            this.angelPerSecond = velocity;
        }
        @Override
        public void run() {
            //如果小于20就停止
            if ((int)Math.abs(angelPerSecond) < 20){
                isFling = false;
                return;
            }
            isFling = true;//正在滚动中
            //todo 不断改变角度，让其滚动起来，/30是为了避免滚动太快了
            mStartAngle += (angelPerSecond / 30);
            //逐渐减小这个值
            angelPerSecond /= 1.0666f;
            postDelayed(this,30);//30毫秒刷新一次
            //重新布局
            requestLayout();

        }
    }

    /**
     * 描述：根据触摸的位置，计算角度
     * 作者：zzh
     * @param xTouch 触摸的x点
     * @param yTouch 触摸的y点
     * @return 计算的角度
     */
    private float getAngle(float xTouch,float yTouch){
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float)(Math.asin(y / Math.hypot(x,y)) * 180 / Math.PI);
    }

    /**
     * 描述：根据当前位置计算象限
     * @param x x坐标
     * @param y y坐标
     * @return 象限
     */
    private int getQuadrant(float x,float y){
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0){
            return tmpY >= 0 ? 4 : 1;
        }else {
            return tmpY >= 0 ? 3 : 2;
        }
    }

    /**
     * 描述：设置item的布局文件
     * @param id 布局文件
     */
    public void setMenuItemLayoutId(int id){
        this.mMenuItemLayoutId = id;
    }

    /**
     * 描述：设置公开方法，设置menu item图标和文字
     * 作者：zzh
     * @param resIds 图标资源集合
     * @param texts 菜单文字集合
     */
    public void setMenuItemIconAndText(int[] resIds,String[] texts){
        //参数检查
        if (resIds == null && texts == null){
            Toast.makeText(getContext(),"菜单项文本和图片需至少设置一项",Toast.LENGTH_LONG).show();
            return;
        }
        mItemImages = resIds;
        mItemTexts = texts;
        //初始化菜单数量
        mMenuItemCount = resIds == null ? texts.length : resIds.length;
        if (resIds != null && texts != null){
            mMenuItemCount = Math.min(resIds.length,texts.length);
        }
        addMenuItems();

    }

    /**
     * 描述：添加菜单项
     */
    private void addMenuItems() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i=0;i<mMenuItemCount;i++){
            final  int j = i;
            View view = inflater.inflate(mMenuItemLayoutId,this,false);
            ImageView iv = view.findViewById(R.id.id_circle_menu_item_image);
            TextView tv = view.findViewById(R.id.id_circle_menu_item_text);
            if (iv != null){
                iv.setVisibility(VISIBLE);
                iv.setImageResource(mItemImages[i]);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMenuItemClickListener != null){
                            mOnMenuItemClickListener.itemClick(v,j);
                        }
                    }
                });
            }
            if (tv != null){
                tv.setVisibility(VISIBLE);
                tv.setText(mItemTexts[i]);
            }
            //添加到view容器中
            addView(view);
        }
    }

    /**
     * 描述：设置旋转角度达到这个值就认为是自动滚动
     * 作者：zzh
     * @param value 角度值
     */
    public void setFlingableValue(int value){
        this.mFlingableValue = value;
    }

    /**
     * 描述：设置内边距
     * 作者：zzh
     * @param padding 边距值
     */
    public void setPadding(float padding){
        this.mPadding = padding;
    }
}
