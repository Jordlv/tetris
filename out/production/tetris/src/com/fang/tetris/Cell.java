package com.fang.tetris;

import java.awt.image.BufferedImage;

/**
 *cell类：定义了本游戏最基本的元素：小方块
 * author:Fang
 */
public class Cell {
    private int row;//行数
    private int col;//列数
    private BufferedImage image;//图片

    public Cell(){};

    public Cell(int row, int col, BufferedImage image) {
        this.row = row;
        this.col = col;
        this.image = image;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    //本游戏最基本的行为：左移一格，右移一格，下移动一格
    public void left(){
        col--;
    }
    public void right(){
        col++;
    }
    public void drop(){
        row++;
    }
}
