package com.fang.tetris;

/**
 * Tetromino类:封装一个由四个小方块构成的四格方块
 *author:Fang
 */
public class Tetromino {
    //将四个方块视为一格数组
    protected Cell[] cells = new Cell[4];

    protected State[] states;

    private int count = 10000;//任意数值皆可

    //俄罗斯方块的四个行为：下落；左移，右移，顺时针旋转，逆时针旋转
    public void moveLeft(){
        for (Cell c:cells){
            c.left();
        }
    }
    public void moveRight(){
        for (Cell c:cells){
            c.right();
        }
    }
    public void moveDrop(){
        for (Cell c:cells){
            c.drop();
        }
    }
    public void rightRotate(){
        count++;
        State s = states[count%states.length];//I型方块只有两种状态，T型有四种,旋转一圈得循环回来所此处如此处理
        Cell c = cells[0];
        int row = c.getRow();
        int col = c.getCol();
        cells[1].setRow(row+s.row1);
        cells[1].setCol(col+s.col1);
        cells[2].setRow(row+s.row2);
        cells[2].setCol(col+s.col2);
        cells[3].setRow(row+s.row3);
        cells[3].setCol(col+s.col3);
    }
    public void leftRotate(){
        count--;
        State s = states[count%states.length];//I型方块只有两种状态，T型有四种,旋转一圈得循环回来所此处如此处理
        Cell c = cells[0];
        int row = c.getRow();
        int col = c.getCol();
        cells[1].setRow(row+s.row1);
        cells[1].setCol(col+s.col1);
        cells[2].setRow(row+s.row2);
        cells[2].setCol(col+s.col2);
        cells[3].setRow(row+s.row3);
        cells[3].setCol(col+s.col3);
    }
     //随机生成一种方块排列
    public static Tetromino randomOne(){
        Tetromino t = null;
        int num = (int) (Math.random()*7);
        switch (num){
            case 0:t = new O();break;
            case 1:t = new T();break;
            case 2:t = new I();break;
            case 3:t = new J();break;
            case 4:t = new L();break;
            case 5:t = new Z();break;
            case 6:t = new S();break;
        }
        return t;
    }
    //内部类方块：旋转的四个状态
    //核心点：将方块的旋转由四个子方块的相对位置描述
    public class State{
        int row0,col0,row1,col1,row2,col2,row3,col3;
        public State(){}

        public State(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3) {
            this.row0 = row0;
            this.col0 = col0;
            this.row1 = row1;
            this.col1 = col1;
            this.row2 = row2;
            this.col2 = col2;
            this.row3 = row3;
            this.col3 = col3;
        }

        public int getRow0() {
            return row0;
        }

        public void setRow0(int row0) {
            this.row0 = row0;
        }

        public int getCol0() {
            return col0;
        }

        public void setCol0(int col0) {
            this.col0 = col0;
        }

        public int getRow1() {
            return row1;
        }

        public void setRow1(int row1) {
            this.row1 = row1;
        }

        public int getCol1() {
            return col1;
        }

        public void setCol1(int col1) {
            this.col1 = col1;
        }

        public int getRow2() {
            return row2;
        }

        public void setRow2(int row2) {
            this.row2 = row2;
        }

        public int getCol2() {
            return col2;
        }

        public void setCol2(int col2) {
            this.col2 = col2;
        }

        public int getRow3() {
            return row3;
        }

        public void setRow3(int row3) {
            this.row3 = row3;
        }

        public int getCol3() {
            return col3;
        }

        public void setCol3(int col3) {
            this.col3 = col3;
        }

    }

}
