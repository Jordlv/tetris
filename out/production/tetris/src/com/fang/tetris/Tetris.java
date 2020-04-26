package com.fang.tetris;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tetris extends JPanel {
    //游戏内的图片资源
    public static BufferedImage T;
    public static BufferedImage I;
    public static BufferedImage O;
    public static BufferedImage J;
    public static BufferedImage L;
    public static BufferedImage S;
    public static BufferedImage Z;
    public static BufferedImage background;
    public static BufferedImage game_over;

    static {
        try {
            T = ImageIO.read(Tetris.class.getResource("images/T.png"));
            O = ImageIO.read(Tetris.class.getResource("images/O.png"));
            I = ImageIO.read(Tetris.class.getResource("images/I.png"));
            J = ImageIO.read(Tetris.class.getResource("images/J.png"));
            L = ImageIO.read(Tetris.class.getResource("images/L.png"));
            S = ImageIO.read(Tetris.class.getResource("images/S.png"));
            Z = ImageIO.read(Tetris.class.getResource("images/Z.png"));
            background = ImageIO.read(Tetris.class.getResource("images/tetris.png"));
            game_over = ImageIO.read(Tetris.class.getResource("images/game-over.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //定义正在下落和即将下落的四格方块
    private Tetromino currentOne = Tetromino.randomOne();
    private Tetromino nextOne = Tetromino.randomOne();

    //定义一个叫做墙的二维数组作为游戏界面
    private Cell[][] wall = new Cell[20][10];//20行10列
    //定义分数池，作为消除0,1,2,3,4 列的得分
    int[] scoresPool = {0,1,2,5,10};
    private int totalScore = 0;
    private int totalLine = 0;

    public static final int PLAYING = 0;
    public static final int PAUSE = 1;
    public static final int GAMEOVER = 2;

    private int gameState;

    //定义一个用来显示游戏状态文字的数组
    String[] showState = {"P[pause]","C[continue]","Enter[replay]"};

    //定义小方格的边长常量为26
    private static final int CELL_SIZE = 26;

    //接下来绘制我们的游戏窗口
    public void paint(Graphics g){
        g.drawImage(background,15,15,null);
        g.translate(15,15);
        paintWall(g);
        paintCurrentOne(g);
        paintNextOne(g);
        paintScore(g);
        paintState(g);
    }

    //绘制游戏状态到相应区域展示
    private void paintState(Graphics g) {
        if (gameState == GAMEOVER){
            g.drawImage(game_over,0,0,null);
            g.drawString(showState[GAMEOVER],285,265);
        }
        if (gameState == PLAYING){
            g.drawString(showState[PLAYING],285,265);
        }
        if (gameState == PAUSE){
            g.drawString(showState[PAUSE],285,265);
        }
    }

    //在右侧相应区域绘制游戏得分
    private void paintScore(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF,Font.ITALIC,26));
        g.drawString("SCORES:"+totalScore,285,165);
        g.drawString("LINES:"+totalLine,285,215);
    }

    /*
    绘制下一个将要下落的四格方块到面板的右上角相应区域
     */
    private void paintNextOne(Graphics g) {
        Cell[] cells = nextOne.cells;
        for (Cell c : cells){
            int row = c.getRow();
            int col = c.getCol();

            int x = col*CELL_SIZE + 260;
            int y = row*CELL_SIZE + 26;

            g.drawImage(c.getImage(),x,y,null);
        }
    }
    /*
    绘制正在下落的四格方块，去除数组的元素绘制元素的版
     */
    private void paintCurrentOne(Graphics g) {
        Cell[] cells = currentOne.cells;
        for (Cell c:cells){
            int x = c.getCol()*CELL_SIZE;
            int y = c.getRow()*CELL_SIZE;
            g.drawImage(c.getImage(),x,y,null);
        }
    }

    /*
     墙是20行十列的表格，就是四格方块下落的背景框
     */
    private void paintWall(Graphics g) {
        for(int i=0;i<20;i++){
            for (int j=0;j<10;j++){
                int x = j * CELL_SIZE;
                int y = i* CELL_SIZE;
                Cell cell = wall[i][j];//每一块小正方形对应一个cell对象
                if (cell == null){
                    g.drawRect(x,y,CELL_SIZE,CELL_SIZE);
                }else {
                    g.drawImage(cell.getImage(),x,y,null);
                }
            }
        }
    }

    //判断游戏是否结束
    public boolean isGameOver(){
        Cell[] cells = nextOne.cells;
        for (Cell c : cells){
            int row = c.getRow();
            int col = c.getCol();
            if (wall[row][col] != null){ //若方块已经到达第二十行 游戏结束
                return true;
            }
        }
        return false;
    }

    //判断一行是否填满以进行消除
    public boolean isFullLine(int row){
        Cell[] line = wall[row];
        for (Cell c : line){
            if (c == null){
                return false;//遍历到一行中有空的cell就返回false,表示这行没有满
            }
        }
        return true;
    }

    int lines = 0;//定义一个变量表示需要清空的行数

    /**
     * 消除占满行的方法，类似队列的清空列的模式。第一个出列，后面的往前依次补，最后一个再进一个到列
     */
    public void destroyLine(){
        Cell[] cells = currentOne.cells;
        for (Cell c : cells){
            int row = c.getRow();
            while (row<20){
                if (isFullLine(row)){
                    lines++;
                    wall[row] = new Cell[10];
                    for (int i=row;i>0;i++){
                        System.arraycopy(wall[i-1],0,wall[i],0,10);
                    }
                    wall[0] = new Cell[10];
                }
                row++;
            }
        }
        //得到对应的分数加入总分数
        totalScore += scoresPool[lines];
        totalLine += lines;
    }

    //定义判断可以下落方法
    public boolean canDrop(){
        Cell[] cells = currentOne.cells;
        for (Cell c : cells){
            int row = c.getRow();
            int col = c.getCol();
            if (row == 19){//到wall底部
                return false;
            }
            if (wall[row+1][col] != null){ //cell的下一方块不为空
                return false;
            }
        }

        return true;
    }

    //四格方块不再下落之后，位置固定，把它镶嵌到wall中，即赋值给wall[][]
    public void landToWall(){
        Cell[] cells = currentOne.cells;
        for (Cell c : cells){
            // 获取最终的行号和列号
            int row = c.getRow();
            int col = c.getCol();
            wall[row][col] = c;
        }
    }

    //防止游戏错误，设计两个方法判定
    public boolean outOfBounds(){//方块越界
        Cell[] cells = currentOne.cells;
        for (Cell c : cells){
            int row = c.getRow();
            int col = c.getCol();
            if (col<0||col>9||row>19||row<0){
                return true;
            }
        }
        return false;
    }

    public boolean coincide(){//两个方块重合
        Cell[] cells = currentOne.cells;
        for (Cell c : cells){
            int row = c.getRow();
            int col = c.getCol();
            if (wall[row][col] != null ){ //说明此wall方块已经被占用
                return true;
            }
        }
        return false;
    }

    //游戏的五种操作：左移 右移 正常下落 直接到底 旋转
    //左移
    public void moveLeftAction(){
        currentOne.moveLeft();
        if (outOfBounds() || coincide()){//左移出了界或跟方块已被占用
            currentOne.moveRight();
        }
    }

    //右移
    public void moveRightAction(){
        currentOne.moveRight();
        if (outOfBounds() || coincide()){//左移出了界或跟方块已被占用
            currentOne.moveLeft();
        }
    }

    //正常下落
    public void DropAction(){
        if (canDrop()){
            currentOne.moveDrop();
        }else {
            landToWall();
            destroyLine();
            currentOne = nextOne;//把下一个方块变成这一个方块
            nextOne = Tetromino.randomOne();//再随机生成下一个四格方块
        }
    }

    //四格方块直接落到底
    public void dropToBottomAction(){
        for (;;){
            if (canDrop()){
                currentOne.moveDrop();
            }else {
                break;
            }
        }
        landToWall();
        destroyLine();
        if (!isGameOver()){
            currentOne = nextOne;
            nextOne = Tetromino.randomOne();
        }else {
            gameState = GAMEOVER;
        }
    }

    public void rotateAction(){
        currentOne.rightRotate();
        if (outOfBounds() || coincide()){
        currentOne.leftRotate();
        }
    }

    //将以上方法封装到start()方法
    public void start(){
        //将游戏状态设置为playing
        gameState = PLAYING;
        //开启键盘监听事件
        KeyListener kl = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_P) {
                    if (gameState == PLAYING) {
                        gameState = PAUSE;
                    }
                }
                    if (code == KeyEvent.VK_C){
                        if (gameState == PAUSE){
                            gameState = PLAYING;
                        }
                    }
                    if (code == KeyEvent.VK_ENTER){
                        gameState = PLAYING;
                        wall = new Cell[20][10];//画一个新的wall
                        currentOne = Tetromino.randomOne();
                        nextOne = Tetromino.randomOne();
                        totalScore = 0;
                        totalLine = 0;
                    }

                    //上下左右键来操作方块
                    switch (code){
                        case KeyEvent.VK_UP : rotateAction();//按上变形
                            break;
                        case KeyEvent.VK_DOWN:dropToBottomAction();//按下直接到底
                            break;
                        case KeyEvent.VK_RIGHT:moveRightAction();//按右向右移
                            break;
                        case KeyEvent.VK_LEFT:moveLeftAction();//按左向左移
                            break;
                    }
                    repaint();//每操作一次都要重新绘制方块
            }
        };
        this.addKeyListener(kl);
        this.requestFocus();

        //CPU执行程序速度快，我们不能看清方块的下落，设置延时
        while (true){
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (gameState == PLAYING){
                if (canDrop()){
                    currentOne.moveDrop();
                }else {
                    landToWall();
                    destroyLine();
                    if (!isGameOver()){
                        currentOne = nextOne;
                        nextOne = Tetromino.randomOne();
                    }else {
                        gameState = GAMEOVER;
                    }
                }
                repaint();//下落之后要进行绘制才能看到下落后的位置
            }
        }
    }
}
