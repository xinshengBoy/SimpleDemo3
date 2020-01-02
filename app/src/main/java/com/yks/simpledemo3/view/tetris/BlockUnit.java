package com.yks.simpledemo3.view.tetris;

import java.util.List;

/**
 * 描述：
 * 作者：zzh
 * time:2019/12/30
 */
public class BlockUnit {

    public static final int UNIT_SIZE = 50;//最高50格
    public static final int BEGIN = 10;
    public int color;
    public int x,y;//坐标

    public BlockUnit() {
    }

    public BlockUnit(int color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    /**
     * 判断方块是否可以向左移动,1是否在边缘,2是否会与其他方块重合
     * @param blockUnits 当前正在下落的方块
     * @param max_x 游戏主界面X轴的最大值 ,下同
     * @param allBlockUnits 所有的方块
     * @return 能移动true;不能移动false
     */
    public static boolean canMoveToLeft(List<BlockUnit> blockUnits, int max_x, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : blockUnits) {
            int x = blockUnit.x;
            if (x - UNIT_SIZE < BEGIN) {
                return false;
            }
            int y = blockUnit.y;
            if (isSameUnit(x - UNIT_SIZE, y, allBlockUnits)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断方块是否可以向右移动,1是否在边缘,2是否会与其他方块重合
     * @param blockUnits 当前正在下落的方块
     * @param max_x 游戏主界面X轴的最大值 ,下同
     * @param allBlockUnits 所有的方块
     * @return 能移动true;不能移动false
     */
    public static boolean canMoveToRight(List<BlockUnit> blockUnits, int max_x, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : blockUnits) {
            int x = blockUnit.x;
            if (x + UNIT_SIZE > max_x - UNIT_SIZE) {
                return false;
            }
            int y = blockUnit.y;
            if (isSameUnit(x + UNIT_SIZE, y, allBlockUnits)) {
                return false;
            }
        }
        return true;
    }
    /**
     * 判断方块是否可以向下移动,1是否在边缘,2是否会与其他方块重合
     * @param blockUnits 当前正在下落的方块
     * @param max_y 游戏主界面X轴的最大值 ,下同
     * @param allBlockUnits 所有的方块
     * @return 能移动true;不能移动false
     */
    public static boolean canMoveToDown(List<BlockUnit> blockUnits, int max_y, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : blockUnits) {
            int x = blockUnit.x;
            int y = blockUnit.y + UNIT_SIZE * 2;
            if (y > max_y - UNIT_SIZE) {
                return false;
            }
            if (isSameUnit(x, y, allBlockUnits)) {
                return false;
            }
        }
        return true;
    }
    public static boolean canRoute(List<BlockUnit> blockUnits, List<BlockUnit> allBlockUnits){
        for (BlockUnit blockUnit: blockUnits) {
            if(isSameUnit(blockUnit.x, blockUnit.y, allBlockUnits)){
                return false;
            }
        }
        return true;
    }

    /**
     * 把当前方块向左移动一格
     * @param blockUnits
     * @param max_x
     * @param allBlockUnits
     * @return 是否成功移动一格,是:true,否:false
     */
    public static boolean toLeft(List<BlockUnit> blockUnits, int max_x, List<BlockUnit> allBlockUnits) {
        if (canMoveToLeft(blockUnits, max_x, allBlockUnits)) {
            for (BlockUnit blockUnit : blockUnits) {
                blockUnit.x = blockUnit.x - UNIT_SIZE;
            }
            return true;
        }
        return false;
    }
    /**
     * 把当前方块向右移动一格
     * @param blockUnits
     * @param max_x
     * @param allBlockUnits
     * @return 是否成功移动一格,是:true,否:false
     */
    public static boolean toRight(List<BlockUnit> blockUnits, int max_x, List<BlockUnit> allBlockUnits) {
        if (canMoveToRight(blockUnits, max_x, allBlockUnits)) {
            for (BlockUnit blockUnit : blockUnits) {
                blockUnit.x = blockUnit.x + UNIT_SIZE;
            }
            return true;
        }
        return false;
    }

    /**
     * 把当前方块下落一格
     * @param blockUnits
     * @param allBlockUnits
     * @return 是否成功移动一格,是:true,否:false
     */
    public static void toDown(List<BlockUnit> blockUnits, int max_Y, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : blockUnits) {
            blockUnit.y = blockUnit.y + BlockUnit.UNIT_SIZE;
        }
    }

    /**
     * 判断 方块单元是否和所有方块有重合
     * @param x
     * @param y
     * @param allBlockUnits
     * @return
     */
    public static boolean isSameUnit(int x, int y, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : allBlockUnits) {
            if (Math.abs(x - blockUnit.x) < UNIT_SIZE && Math.abs(y - blockUnit.y) < UNIT_SIZE) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除在第j行上的方块单元
     * @param allBlockUnits
     * @param j 需删除行标
     */
    public static void remove(List<BlockUnit> allBlockUnits, int j) {
        for (int i = allBlockUnits.size() - 1; i >= 0; i--) {
            /*
             * ①逆向遍历 ②根据y坐标计算单元所在行，若为j行则从units中删除
             */
            if ((int) ((allBlockUnits.get(i).y - BEGIN) / 50) == j)
                allBlockUnits.remove(i);
        }
    }
}
