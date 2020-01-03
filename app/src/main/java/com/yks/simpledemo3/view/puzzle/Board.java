package com.yks.simpledemo3.view.puzzle;

import java.util.Random;

/**
 * 描述：
 * 作者：zzh
 * time:2020/01/02
 */
public class Board {

    private int[][] array = null;
    private int row = 0;
    private int col = 0;
    private int [][] dir = {
            {0,-1},//上
            {0,1},//下
            {-1,0},//左
            {1,0}//右
    };

    /**
     * 描述：创建坐标数组
     * @param row 行
     * @param col 列
     */
    private void createIntegerArray(int row,int col){
        array = new int[row][col];
        int idx = 0;
        for (int i=0;i<row;i++){
            for (int k=0;k<col;k++){
                array[i][k] = idx++;
            }
        }
    }

    /**
     * 描述：移动块的位置
     * @param srcX 图块的x位置
     * @param srcY 图块的y位置
     * @param xOffset 图块的x偏移值
     * @param yOffset 图块的y偏移值
     * @return 新的位置，错误返回（-1，-1）
     */
    private Point move(int srcX,int srcY,int xOffset,int yOffset){
        int x = srcX + xOffset;
        int y = srcY + yOffset;
        if (x < 0 || y < 0 || x >= col || y >=row){
            return new Point(-1,-1);
        }

        int temp = array[y][x];
        array[y][x] = array[srcY][srcX];
        array[srcY][srcX] = temp;
        return new Point(x,y);
    }

    /**
     * 描述：得到下一个可以移动的位置
     * @param src 当前位置
     * @return 下一个位置
     */
    private Point getNextPoint(Point src){
        Random random = new Random();
        int ids = random.nextInt(4);//产生0-3之间的随机数
        int xOffset = dir[ids][0];
        int yOffset = dir[ids][1];
        Point point = move(src.getX(),src.getY(),xOffset,yOffset);
        if (point.getX() != -1 && point.getY() != -1){
            return point;//todo 不是错误位置则返回下一个可以移动的位置
        }
        return getNextPoint(src);
    }

    /**
     * 描述：产生随机拼图
     * @param row  行
     * @param col 列
     * @return 拼图数据
     */
    public int[][] createRandomBoard(int row,int col){
        if (row < 2 || col < 2){
            throw new IllegalArgumentException("行和列不能小于2");
        }

        this.row  = row;
        this.col = col;
        createIntegerArray(row,col);
        int count = 0;
        Point point = new Point(col-1,row-1);
        Random random = new Random();
        int num = random.nextInt(100) + 20;//产生20-119的随机数
        while (count < num){
            point = getNextPoint(point);
            count ++;
        }
        return array;
    }

    /**
     * 判断是否拼图成功
     * @param arr 拼图数据
     * @return 是否成功
     */
    public boolean isSuccess(int[][]arr){
        int idx = 0;
        for (int i=0;i<arr.length;i++){
            for (int k=0;k<arr[i].length && idx < row*col - 1;k++){
                if (arr[idx/row][idx%col] > arr[(idx+1) / row][(idx+1)%col]){
                    return false;
                }
                idx++;
            }
        }
        return true;
    }
}
