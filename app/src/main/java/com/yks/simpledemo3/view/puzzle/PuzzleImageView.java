package com.yks.simpledemo3.view.puzzle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.yks.simpledemo3.R;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

/**
 * 描述：拼图
 * 作者：zzh
 * time:2020/01/02
 * https://github.com/luoyesiqiu/PuzzleGame/blob/master/app/src/main/java/com/luoye/pintu/MainView.java
 */
public class PuzzleImageView extends View {

    private Context context;
    private Activity activity;
    private Paint paint;
    private Bitmap back;
    private Bitmap[] bitmapTiles;
    private int[][] dataTiles;
    private final int COL = 3;//行数
    private final int ROW = 3;//列数
    private int tileWidth,tileHeight;//每个小方块的宽高
    private int screenWidth,screenHeight;
    private int[][] dir = {//方向
            {-1,0},//左
            {0,-1},//右
            {1,0},//上
            {0,1}//下
    };
    private boolean isSuccess;
    private Board tilesBoard;
    private int offset = 3;//偏移量
    private long downTime;

    public PuzzleImageView(Context context,Activity activity) {
        super(context);
        this.context = context;
        this.activity = activity;
        paint = new Paint();
        paint.setAntiAlias(true);//去锯齿
        init();
        startGame();
    }

    private void init() {
        //todo 载入图片，并切成块
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.puzzles);
        screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        back = Bitmap.createScaledBitmap(bitmap, screenWidth,screenHeight,true);
        tileWidth = back.getWidth() / COL;
        tileHeight = back.getHeight() / ROW;
        bitmapTiles = new Bitmap[COL*ROW];
        int idx = 0;
        for (int i=0;i<ROW;i++){
            for (int k=0;k<COL;k++){
                bitmapTiles[idx++] = Bitmap.createBitmap(back,k*tileWidth,i*tileHeight,tileWidth-offset,tileHeight-offset);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = widthMeasureSpec;
        screenHeight = heightMeasureSpec;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        for (int i=0;i<ROW;i++){
            for (int k=0;k<COL;k++){
                int idx = dataTiles[i][k];
                if (idx == ROW * COL - 1 && !isSuccess){
                    continue;
                }
                canvas.drawBitmap(bitmapTiles[idx],k*tileWidth,i*tileHeight,paint);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isSuccess){//按下
            downTime = event.getDownTime();
            Point point = xyToIndex((int)event.getX(), (int) event.getY());
            for (int i=0;i<dir.length;i++){
                int newX = point.getX() + dir[i][0];
                int newY = point.getY() + dir[i][1];
                if (newX >= 0 && newX < COL && newY >= 0 && newY < ROW){
                    if (dataTiles[newY][newX] == COL * ROW - 1){
                        int temp = dataTiles[point.getY()][point.getX()];
                        dataTiles[point.getY()][point.getX()] = dataTiles[newY][newX];
                        dataTiles[newY][newX] = temp;
                        invalidate();
                        if (tilesBoard.isSuccess(dataTiles)){
                            isSuccess = true;
                            invalidate();
                            LemonHello.getSuccessHello("成功","拼图成功！要重新开始么？").addAction(new LemonHelloAction("重新开始", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                                    startGame();
                                    lemonHelloView.hide();
                                }
                            })).addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                                    lemonHelloView.hide();
                                }
                            })).show(context);
                        }
                    }
                }
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP){//抬起
            long time = event.getEventTime();
            if (time - downTime > 1500){
                LemonHello.getInformationHello("提示","重新开始").addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        lemonHelloView.hide();
                    }
                })).addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        startGame();
                        lemonHelloView.hide();
                    }
                })).show(context);
            }
        }

        return true;
    }

    /**
     * 描述：开始游戏
     */
    private void startGame(){
        tilesBoard = new Board();
        dataTiles = tilesBoard.createRandomBoard(ROW,COL);
        isSuccess = false;
        invalidate();
    }

    /**
     * 描述：将屏幕上的点转换成对应拼图的索引
     * @param x 屏幕上的点的x
     * @param y 屏幕上的点的y
     * @return 索引
     */
    private Point xyToIndex(int x,int y){
        int extraX = x % tileWidth > 0 ? 1 : 0;
        int extraY = x % tileWidth > 0 ? 1 : 0;
        int col = x / tileWidth + extraX;
        int row = y / tileHeight + extraY;
        return new Point(col -1,row - 1);
    }
}
