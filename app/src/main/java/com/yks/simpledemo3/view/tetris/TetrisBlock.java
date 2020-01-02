package com.yks.simpledemo3.view.tetris;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：方块
 * 作者：zzh
 * time:2019/12/30
 */
public class TetrisBlock {
    private static final int TYPE_SUM = 7;//共七种类型的方块
    public int blockType;//方块类型
    public int blockDirection;//方块朝向
    private int color;//方块颜色
    private int x,y;//方块坐标

    public TetrisBlock() {
    }

    public TetrisBlock(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public List<BlockUnit> getUnits(int x,int y){
        this.x = x;
        this.y = y;
        return returnUnit();
    }

    private List<BlockUnit> returnUnit(){
        List<BlockUnit> units = new ArrayList<>();//方块组成部分
        blockType = (int) ((Math.random() * TYPE_SUM) + 1);//todo 随机生成一个种类
        blockDirection = 1;//默认初始方向
        color = (int) ((Math.random() * 4) + 1);//随机生成一个颜色
        units.clear();
        switch (blockType){
            case 1://todo 横线
                for (int i=0;i<4;i++){
                    units.add(new BlockUnit(x+(-2+i)*BlockUnit.UNIT_SIZE,y,color));
                }
                break;
            case 2:
                units.add(new BlockUnit(x+(-1+1)*BlockUnit.UNIT_SIZE,y-BlockUnit.UNIT_SIZE,color));
                for (int i=0;i<3;i++){
                    units.add(new BlockUnit(x+(-1+i)*BlockUnit.UNIT_SIZE,y,color));
                }
                break;
            case 3:
                for (int i=0;i<2;i++){
                    units.add(new BlockUnit(x+(i-1)*BlockUnit.UNIT_SIZE,y-BlockUnit.UNIT_SIZE,color));
                    units.add(new BlockUnit(x+(i-1)*BlockUnit.UNIT_SIZE,y,color));
                }
                break;
            case 4:
                units.add(new BlockUnit(x+(-1+0)*BlockUnit.UNIT_SIZE,y-BlockUnit.UNIT_SIZE,color));
                for (int i=0;i<3;i++){
                    units.add(new BlockUnit(x+(-1+i)*BlockUnit.UNIT_SIZE,y,color));
                }
                break;
            case 5:
                units.add(new BlockUnit(x+(-1+2)*BlockUnit.UNIT_SIZE,y-BlockUnit.UNIT_SIZE,color));
                for (int i=0;i<3;i++){
                    units.add(new BlockUnit(x+(-1+i)*BlockUnit.UNIT_SIZE,y,color));
                }
                break;
            case 6:
                for (int i=0;i<2;i++){
                    units.add(new BlockUnit(x+(-1+i)*BlockUnit.UNIT_SIZE,y-BlockUnit.UNIT_SIZE,color));
                    units.add(new BlockUnit(x+i*BlockUnit.UNIT_SIZE,y,color));
                }
                break;
            case 7:
                for (int i=0;i<2;i++){
                    units.add(new BlockUnit(x+i*BlockUnit.UNIT_SIZE,y-BlockUnit.UNIT_SIZE,color));
                    units.add(new BlockUnit(x+(-1+i)*BlockUnit.UNIT_SIZE,y,color));
                }
                break;
        }
        return units;
    }
}
