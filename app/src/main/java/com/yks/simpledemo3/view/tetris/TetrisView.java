package com.yks.simpledemo3.view.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yks.simpledemo3.activity.TetrisActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：俄罗斯方块
 * 作者：zzh
 * time:2019/12/30
 * https://www.jb51.net/article/142669.htm
 */
public class TetrisView extends View {

    private Paint paintWall;//背景墙画笔
    private Paint paintBlock;//方块单元块画笔
    private int BOUND_WIDTH_OF_WALL = 2;
    /**方块下落的速度*/
    public static final int SPEED = 300;
    private TetrisActivity father = null;

    private List<BlockUnit> blockUnits = new ArrayList<>();//正在下落的方块
    private List<BlockUnit> blockUnitBufs = new ArrayList<>();//下一个要显示的方块
    private List<BlockUnit> routeBlockUnitBufs = new ArrayList<>();
    private List<BlockUnit> allBlockUnitBufs = new ArrayList<>();//全部的方块

    /** 网格开始坐标值，横纵坐标的开始值都是此值 */
    public static final int beginPoint = 10;
    private int[] map = new int[100];//保存每行网格中包含方块单元的个数
    private static final int[] color = {Color.parseColor("#FFFF6600"),Color.parseColor("#FFFB6D05"),Color.parseColor("#0078F0"),Color.parseColor("#FFFA031C"),Color.parseColor("#FF179209"),Color.parseColor("#FF3892")};
    private int xx,yy;//方块的中心方块单元的坐标
    private static  int max_x,max_y;//最大坐标
    private static int num_x = 0,num_y = 0;//行数和列数
    private boolean isStart = false;//游戏的状态，开始还是停止
    private boolean isRunning = false;//游戏状态，运行或暂停
    private TetrisBlock tetrisBlock;//方块，用户随机获取各种形状的方块
    private int score = 0;//当前分数
    private int blockType = 0;//当前方块的类型
    private Thread mainThread = null;//游戏主线程

    public TetrisView(Context context) {
        this(context,null);
    }

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        //背景墙画笔
        if (paintWall == null) {
            paintWall = new Paint();
            paintWall.setColor(Color.LTGRAY);
            paintWall.setStyle(Paint.Style.STROKE);
            paintWall.setStrokeWidth(BOUND_WIDTH_OF_WALL + 1);
        }
        //方块单元块画笔
        if (paintBlock == null) {
            paintBlock = new Paint();
            paintBlock.setColor(Color.parseColor("#FF6600"));
        }
        tetrisBlock = new TetrisBlock();
        routeBlockUnitBufs = tetrisBlock.getUnits(beginPoint, beginPoint);
        Arrays.fill(map, 0); // 每行网格中包含俄罗斯方块单元的个数全部初始化为0
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        max_x = getWidth();
        max_y = getHeight();
        RectF rel;
        // 绘制网格
        num_x = 0;
        num_y = 0;
        for (int i = beginPoint; i < max_x - BlockUnit.UNIT_SIZE; i += BlockUnit.UNIT_SIZE) {
            for (int j = beginPoint; j < max_y - BlockUnit.UNIT_SIZE; j += BlockUnit.UNIT_SIZE) {
                rel = new RectF(i, j, i + BlockUnit.UNIT_SIZE, j + BlockUnit.UNIT_SIZE);
                canvas.drawRoundRect(rel, 8, 8, paintWall);
                num_y++;
            }
            num_x++;
        }
        // 随机产生一个俄罗斯方块
        int len = blockUnits.size();
        // 绘制方块
        for (int i = 0; i < len; i++) {
            int x = blockUnits.get(i).x;
            int y = blockUnits.get(i).y;
            // 设置当前方块的颜色
//            paintBlock.setColor(color[blockUnits.get(i).color]);
            paintBlock.setColor(color[len-i]);
            rel = new RectF(x + BOUND_WIDTH_OF_WALL, y + BOUND_WIDTH_OF_WALL,
                    x + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL, y + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL);
            canvas.drawRoundRect(rel, 8, 8, paintBlock);
        }
        // 随机产生一个俄罗斯方块
        len = allBlockUnitBufs.size();
        // 绘制方块
        for (int i = 0; i < len; i++) {
            int x = allBlockUnitBufs.get(i).x;
            int y = allBlockUnitBufs.get(i).y;
//            paintBlock.setColor(color[allBlockUnitBufs.get(i).color]);
            paintBlock.setColor(color[len-i]);
            rel = new RectF(x + BOUND_WIDTH_OF_WALL, y + BOUND_WIDTH_OF_WALL,
                    x + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL, y + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL);
            canvas.drawRoundRect(rel, 8, 8, paintBlock);
        }
    }

    /**
     * 开始游戏
     */
    public void startGame() {
        isStart = true;
        isRunning = true;
        if (mainThread == null || !mainThread.isAlive()) {
            getNewBlock();
            mainThread = new Thread(new MainThread());
            mainThread.start();
        }
    }

    /**
     * 暂停游戏
     */
    public void pauseGame() {
        isRunning = false;
    }

    /**
     * 继续游戏
     */
    public void continueGame() {
        isRunning = true;
    }

    /**
     * 停止游戏
     */
    public void stopGame() {
        // 停止游戏,释放游戏主线程
        isRunning = false;
        isStart = false;
        mainThread.interrupt();
        blockUnits.clear();
        allBlockUnitBufs.clear();
        score = 0;
        invalidate();
    }

    /**
     * 向左滑动
     */
    public void toLeft() {
        if (BlockUnit.toLeft(blockUnits, max_x, allBlockUnitBufs)) {
            xx = xx - BlockUnit.UNIT_SIZE;
        }
        invalidate();
    }

    /**
     * 向右滑动
     */
    public void toRight() {
        if (BlockUnit.toRight(blockUnits, max_x, allBlockUnitBufs)) {
            xx = xx + BlockUnit.UNIT_SIZE;
        }
        invalidate();
    }

    /**
     * 按顺时针旋转
     */
    public void route() {
        if (blockType == 3) {// 如果当前正在下落的方块为正方形,则不进行旋转
            return;
        }
        if (routeBlockUnitBufs.size() != blockUnits.size()) {
            routeBlockUnitBufs = tetrisBlock.getUnits(xx, yy);
        }
        for (int i = 0; i < blockUnits.size(); i++) {
            routeBlockUnitBufs.get(i).x = blockUnits.get(i).x;
            routeBlockUnitBufs.get(i).y = blockUnits.get(i).y;
        }
        for (BlockUnit blockUnit : routeBlockUnitBufs) {
            int tx = blockUnit.x;
            int ty = blockUnit.y;
            blockUnit.x = -(ty - yy) + xx;
            blockUnit.y = tx - xx + yy;
        }
        routeTran(routeBlockUnitBufs);
        if (!BlockUnit.canRoute(routeBlockUnitBufs, allBlockUnitBufs)) {
            // Toast.makeText(father, "不可旋转", Toast.LENGTH_SHORT).show();
            return;
        }
        for (BlockUnit blockUnit : blockUnits) {
            int tx = blockUnit.x;
            int ty = blockUnit.y;
            blockUnit.x = -(ty - yy) + xx;
            blockUnit.y = tx - xx + yy;
        }
        routeTran(blockUnits);
        invalidate();
    }

    /**
     * 描述：设置当前游戏页面的父类activity
     * @param activity activity
     */
    public void setFather(TetrisActivity activity){
        this.father = activity;
    }
    /**
     * 如果方块处于边缘,则翻转过后,会出现方块部分处于边缘之外的情况, 因此,通过递归判断是否有超出边缘的部分,
     * 如果有,则进行左右平移,把处于边缘外的方块移动到边缘内
     */
    public void routeTran(List<BlockUnit> blockUnitsBuf) {
        boolean needLeftTran = false;
        boolean needRightTran = false;
        for (BlockUnit u : blockUnitsBuf) {
            if (u.x < beginPoint) {
                needLeftTran = true;
            }
            if (u.x > max_x - BlockUnit.UNIT_SIZE) {
                needRightTran = true;
            }
        }
        if (needLeftTran || needRightTran) {
            for (BlockUnit u : blockUnitsBuf) {
                if (needLeftTran) {
                    u.x = u.x + BlockUnit.UNIT_SIZE;
                } else if (needRightTran) {
                    u.x = u.x - BlockUnit.UNIT_SIZE;
                }
            }
            routeTran(blockUnitsBuf);
        } else {
            return;
        }

    }
    /**
     * 获取一个新的方块
     */
    private void getNewBlock() {
        // 新的方块的坐标，x坐标位于x轴的中间，y 位于起始位置
        this.xx = beginPoint + (num_x / 2) * BlockUnit.UNIT_SIZE;
        this.yy = beginPoint;
        if (blockUnitBufs.size() == 0) {
            // 当游戏第一次开始的时候，先初始化一个方块
            blockUnitBufs = tetrisBlock.getUnits(xx, yy);
        }
        blockUnits = blockUnitBufs;
        blockType = tetrisBlock.blockType;
        blockUnitBufs = tetrisBlock.getUnits(xx, yy);
        if (father != null) {// 显示出下一个要出现的方块
            father.setNextBlockView(blockUnitBufs, (num_x / 2) * BlockUnit.UNIT_SIZE);
        }
    }


    /**
     * 游戏的主线程
     *
     * @sign Created by wang.ao on 2017年1月16日
     */
    private class MainThread implements Runnable {

        @Override
        public void run() {
            while (isStart) {
                while (isRunning) {
                    if (BlockUnit.canMoveToDown(blockUnits, max_y, allBlockUnitBufs)) {
                        // 判断是否可以继续下落，如果可以下落，则下落
                        BlockUnit.toDown(blockUnits, max_y, allBlockUnitBufs);
                        yy = yy + BlockUnit.UNIT_SIZE;
                    } else {
                        /**
                         * 当不可以继续下落的时候，把当前的方块添加到allBlockUnits中，
                         * 并且判断是否有需要消除的方块，然后再产生一个新的方块
                         */
                        for (BlockUnit blockUnit : blockUnits) {
                            blockUnit.y = blockUnit.y + BlockUnit.UNIT_SIZE;
                            allBlockUnitBufs.add(blockUnit);
                        }
                        for (BlockUnit u : blockUnits) {
                            // 更新map，即更新每行网格中静止俄罗斯方块单元的个数
                            int index = (u.y - beginPoint) / 50; // 计算所在行数
                            map[index]++;
                        }
                        // 每行最大个数
                        int end = (max_y - 50 - beginPoint) / BlockUnit.UNIT_SIZE;
                        int full = ((max_x - 50 - beginPoint) / BlockUnit.UNIT_SIZE) + 1;
                        try {
                            Thread.sleep(SPEED);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i <= end; i++) {
                            /***
                             * 消除需要消除的方块（触发条件，某一行中被塞满了方块，没有空白）
                             * 注意顺序，先消除某一行，再移动这一行上边的方块
                             */
                            if (map[i] >= full) {
                                BlockUnit.remove(allBlockUnitBufs, i);
                                score += 100;
                                map[i] = 0;
                                for (int j = i; j > 0; j--)
                                    map[j] = map[j - 1];
                                map[0] = 0;
                                for (BlockUnit blockUnit : allBlockUnitBufs) {
                                    if (blockUnit.y < (i * BlockUnit.UNIT_SIZE + beginPoint)) {
                                        blockUnit.y = blockUnit.y + BlockUnit.UNIT_SIZE;
                                    }
                                }
                            }
                        }
                        father.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 * 刷新分数
                                 */
                                father.score.setText("" + score);
                                invalidate();
                            }
                        });
                        try {
                            Thread.sleep(SPEED * 3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        father.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getNewBlock();
                                score += 10;
                                father.score.setText("" + score);
                            }
                        });
                    }
                    father.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                    try {
                        Thread.sleep(SPEED);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }
}
