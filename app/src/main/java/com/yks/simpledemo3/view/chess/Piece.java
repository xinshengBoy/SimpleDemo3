package com.yks.simpledemo3.view.chess;

/**
 * 描述：棋子的信息
 * Created by Jimmy on 2016/9/19 0019.
 */
public class Piece {

    public int x;//x坐标
    public int y;//y坐标
    public PieceView.PIECE_TYPE type;//类型：红棋、黑棋
    public String name;//棋子文字描述

    Piece(int x, int y, PieceView.PIECE_TYPE type, String name) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.name = name;
    }
}
