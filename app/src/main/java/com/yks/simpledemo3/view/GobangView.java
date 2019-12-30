package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yks.simpledemo3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：五子棋
 * 作者：zzh
 * time:2019/12/27
 * https://github.com/Brioal/GoBang/tree/master/app/src/main/java/com/brioal/gobang/view
 */
public class GobangView extends View {

    private Paint mPaint;//绘制线的画笔
    private Paint mPaintPoint;//绘制棋子的画笔
    private int MAX_VALUE = 10;//格子的数量
    private int MAX_IN_LINE = 5;//胜利的条件
    public static int WHITE_WIN = 0;//白棋胜利的标识
    public static int BLACK_WIN = 1;//黑棋胜利的标识
    private int panleWidth;//棋盘的宽度
    private float lineHeight;//方格的宽高
    private Bitmap mWhite;//白棋
    private Bitmap mBlack;//黑棋
    private int pieceWidth;//棋子要显示的高度
    private int offset;//棋盘离组件边距的偏移量
    private List<Point> mWhitePoints;//棋盘上白棋的位置集合
    private List<Point> mBlackPoints;//棋盘上黑棋的位置集合
    private boolean isWhite =false;//存储是否是白棋，默认黑棋先行
    private boolean isGameOver = false;//游戏是否结束
    private int mUnder;//底部的位置，显示dialog
    private onGameListener onGameListener;//监听器

    public GobangView(Context context) {
        this(context,null);
    }

    public GobangView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public GobangView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //todo 棋盘画笔
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.colorBlack2));//设置画笔颜色
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//设置图像抖动处理，使图像更平滑
        mPaint.setStyle(Paint.Style.STROKE);//绘制方式，描边
        mPaint.setStrokeWidth(2);//设置画笔宽度
        //todo 棋子画笔
        mPaintPoint = new Paint();
        mPaintPoint.setAntiAlias(true);;//抗锯齿
        mPaintPoint.setDither(true);//防抖动
        mWhite = BitmapFactory.decodeResource(getResources(),R.mipmap.stone_w2);//白棋图像
        mBlack = BitmapFactory.decodeResource(getResources(),R.mipmap.stone_b1);//黑棋图像
        //todo 位置存储数据
        mWhitePoints = new ArrayList<>();
        mBlackPoints = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//获取宽度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获取宽度的类型

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);//获取高度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获取高度的类型

        int width = Math.min(widthMeasureSpec,heightMeasureSpec);//获取宽高的最小值
        if (widthMode == MeasureSpec.UNSPECIFIED){
            //todo 宽度为wrap_parent，则最终宽度置高度
            width = heightSize;
        }else if (heightMode == MeasureSpec.UNSPECIFIED){
            //todo 如果高度为wrap_parent，最终高度置宽度
            width = widthSize;
        }
        setMeasuredDimension(width,width);//设置值
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        panleWidth = w;//棋盘的长度等于屏幕长度
        mUnder = h - (h - panleWidth) / 2;//对话框显示的位置
        lineHeight = panleWidth * 1.0f / MAX_VALUE;//计算出每个格子的高度
        offset = (int) (lineHeight / 2);//偏移量
        pieceWidth = (int) (lineHeight * 3 / 4);//todo 棋子的高度为格子的四分之三
        mWhite = Bitmap.createScaledBitmap(mWhite,pieceWidth,pieceWidth,false);//根据棋子宽度对图片进行缩放
        mBlack = Bitmap.createScaledBitmap(mBlack,pieceWidth,pieceWidth,false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver){
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){//todo 按下的时候
            Point point = new Point((int) ((event.getX() - offset) / lineHeight), (int) ((event.getY() - offset) / lineHeight));
            if (!mWhitePoints.contains(point) && !mBlackPoints.contains(point)){//如果没有记录这个点，则存入这个点
                if (isWhite){
                    mWhitePoints.add(point);
                    isWhite = false;
                }else {
                    mBlackPoints.add(point);
                    isWhite = true;
                }
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);//todo 绘制棋盘
        drawPoints(canvas);//todo 绘制棋子
        checkGameOver();//todo 检查游戏是否结束
    }

    /**
     * 描述：绘制棋盘
     * 作者：zzh
     * @param canvas 画布
     */
    private void drawBoard(Canvas canvas) {
        int start_x = offset;//起始x坐标
        int end_x = panleWidth - offset;//终止x坐标
        for (int i=0;i<MAX_VALUE;i++){
            float start_y = i * lineHeight + offset;//起始y坐标
            float end_y = i * lineHeight + offset;//终止的y坐标
            canvas.save();
            canvas.drawLine(start_x,start_y,end_x,end_y,mPaint);//todo 绘制横向的线
            canvas.drawLine(start_y,start_x,end_y,end_x,mPaint);//todo 绘制纵向的线，把横向的xy交换就行
            canvas.restore();
        }
    }

    /**
     * 描述：绘制棋子
     * 作者：zzh
     * @param canvas 画布
     */
    private void drawPoints(Canvas canvas) {
        canvas.save();
        //todo 绘制白棋
        for (Point point : mWhitePoints){
            canvas.drawBitmap(mWhite,offset + point.x * lineHeight - pieceWidth / 2,offset + point.y * lineHeight - pieceWidth / 2,mPaintPoint);
        }
        //todo 绘制黑棋
        for (Point point : mBlackPoints){
            canvas.drawBitmap(mBlack,offset + point.x * lineHeight - pieceWidth / 2,offset + point.y * lineHeight - pieceWidth / 2,mPaintPoint);
        }
        canvas.restore();
    }

    /**
     * 描述：判断游戏是否结束
     */
    public void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhitePoints);
        boolean blackWin = checkFiveInLine(mBlackPoints);
        if (whiteWin || blackWin){
            isGameOver = true;//游戏结束
            if (onGameListener != null){//返回游戏结果
                onGameListener.onGameOver(whiteWin ? WHITE_WIN : BLACK_WIN);
            }
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point point : points){
            int x = point.x;
            int y = point.y;
            //todo 水平方向检查
            boolean isWin1 = checkResult(x,y,points,"horizontal");
            boolean isWin2 = checkResult(x,y,points,"vertical");
            boolean isWin3 = checkResult(x,y,points,"diagonalleft");
            boolean isWin4 = checkResult(x,y,points,"diagonalright");
            if (isWin1 || isWin2 || isWin3 || isWin4){
                return true;
            }
        }
        return false;
    }

    /**
     * 描述：检查对应方向
     * 作者：zzh
     * @param x 当前棋子的x坐标
     * @param y 当前棋子的y坐标
     * @param points 坐标点集合
     * @param direction 检查的方向
     * @return 是否已经结束了（赢了）
     */
    private boolean checkResult(int x, int y, List<Point> points,String direction) {
        int count = 1;
        //todo 往左遍历
        for (int i=1;i<MAX_IN_LINE;i++){
            Point point = null;
            if (direction.equals("horizontal")){
                point = new Point(x-i,y);
            }else if (direction.equals("vertical")){
                point = new Point(x,y - i);
            }else if (direction.equals("diagonalleft")){
                point = new Point(x - i,y - i);
            }else if (direction.equals("diagonalright")){
                point = new Point(x + i,y - i);
            }

            if (points.contains(point)){//是否包含点
                count ++;
            }
        }
        if (count == MAX_IN_LINE){
            return true;
        }
        //todo 往左遍历
        for (int i=1;i<MAX_IN_LINE;i++){
            Point point = null;
            if (direction.equals("horizontal")){
                point = new Point(x+i,y);
            }else if (direction.equals("vertical")){
                point = new Point(x,y + i);
            }else if (direction.equals("diagonalleft")){
                point = new Point(x + i,y + i);
            }else if (direction.equals("diagonalright")){
                point = new Point(x - i,y + i);
            }
            if (points.contains(point)){//是否包含点
                count ++;
            }
        }
        if (count == MAX_IN_LINE){
            return true;
        }
        return false;
    }

    public interface onGameListener{
        void onGameOver(int i);
    }

    public void setOnGameListener(onGameListener onGameListener){
        this.onGameListener = onGameListener;
    }

    /**
     * 描述：重新开始
     * 作者：zzh
     */
    public void reStartGame(){
        mWhitePoints.clear();//清理白棋
        mBlackPoints.clear();//清理黑棋
        isGameOver = false;//游戏未结束
        isWhite = false;//设置黑棋先行
        invalidate();//重绘
    }
}
