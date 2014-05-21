package cst.android.CrazyRun.tempBall;

import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.constants.WhatView;
import cst.android.CrazyRun.main.GameView;
import cst.android.CrazyRun.main.MainActivity;
import cst.android.CrazyRun.scences.ObstacleForControl;
import java.util.ArrayList;

/*
 * 控制球运动的线程
 */
public class BallGoThread extends Thread {

    GameView gameView;
    ArrayList<BallForControl> ballForControls = new ArrayList<BallForControl>();//声明AllBalls的引用
    ArrayList<ObstacleForControl> obstacleForControls = new ArrayList<ObstacleForControl>();

    public BallGoThread(GameView gv,ArrayList<BallForControl> ballForControls,ArrayList<ObstacleForControl> obstacleForControls) {
        this.gameView = gv;
        this.ballForControls=ballForControls;//成员变量赋值
        this.obstacleForControls=obstacleForControls;
    }

    @Override
    public void run()//重写run方法
    {
        while(Constant.PAUSE_FLAG) {
            BallForControl mainBall = ballForControls.get(0);
            synchronized(this) {
                mainBall.go(obstacleForControls);
            }
            try{
                Thread.sleep(Constant.TIME_SPAN);//一段时间后再运动
            }
            catch(Exception e){
                e.printStackTrace();//打印异常
            }
        }
    }

}
