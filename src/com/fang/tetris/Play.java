package com.fang.tetris;

import javax.swing.*;

public class Play {
    public static void main(String[] args) {
        //创建一个窗口对象
        JFrame frame = new JFrame("俄罗斯方块");
        //创建游戏面板
        Tetris panel = new Tetris();
        //将面板嵌入窗口
        frame.add(panel);
        //设置为可见
        frame.setVisible(true);
        //设置窗口的尺寸
        frame.setSize(535,580);
        //设置窗口居中
        frame.setLocationRelativeTo(null);
        //设置窗口关闭，游戏即终止
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //游戏的主要逻辑封装在start()方法中
        panel.start();
    }
}
