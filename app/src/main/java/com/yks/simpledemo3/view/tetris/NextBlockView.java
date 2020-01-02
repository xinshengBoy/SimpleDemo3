package com.yks.simpledemo3.view.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：下一个方块
 * 作者：zzh
 * time:2019/12/30
 */
public class NextBlockView extends View {

    public static final int beginPoint = 10;//网格开始坐标值，横纵坐标都是这个值
    private static int max_x,max_y;//最大坐标
    private Paint paintWall;//画背景墙笔
    private Paint paintBlock;//画方块画笔
    private int div_x = 0;
    private static final int BOUND_WIDTH_OF_WALL = 2;
    private static final int[] color = {Color.parseColor("#FFFF6600"),Color.parseColor("#FFFB6D05"),Color.parseColor("#0078F0"),Color.parseColor("#FFFA031C"),Color.parseColor("#FF179209"),Color.parseColor("#FF3892")};
    private List<BlockUnit> blockUnits = new ArrayList<>();

    public NextBlockView(Context context) {
        this(context,null);
    }

    public NextBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //背景墙画笔
        if (paintWall == null) {
            paintWall = new Paint();
            paintWall.setColor(Color.LTGRAY);
            paintWall.setStyle(Paint.Style.STROKE);
            paintWall.setStrokeWidth(BOUND_WIDTH_OF_WALL + 1);
        }
        //方块画笔
        if (paintBlock == null) {
            paintBlock = new Paint();
            paintBlock.setColor(Color.parseColor("#FF6600"));
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        max_x = getWidth();
        max_y = getHeight();
        RectF rel;
        //todo 绘制网格
        int len = blockUnits.size();
        for (int i = 0; i < len; i++) {
//            paintBlock.setColor(color[blockUnits.get(i).color]);
            paintBlock.setColor(color[i]);
            int x = blockUnits.get(i).x - div_x + BlockUnit.UNIT_SIZE * 2;
            int y = blockUnits.get(i).y + BlockUnit.UNIT_SIZE * 2;
            rel = new RectF(x + BOUND_WIDTH_OF_WALL, y + BOUND_WIDTH_OF_WALL,
                    x + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL, y + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL);
            canvas.drawRoundRect(rel, 8, 8, paintBlock);
            rel = new RectF(x, y, x + BlockUnit.UNIT_SIZE, y + BlockUnit.UNIT_SIZE);
            canvas.drawRoundRect(rel, 8, 8, paintWall);
        }
    }

    /**
     * 描述：设置方块属性
     * @param datas 方块list
     * @param x x
     */
    public void setBlockUnits(List<BlockUnit> datas,int x){
        this.blockUnits = datas;
        this.div_x = x;
        invalidate();
    }
}
