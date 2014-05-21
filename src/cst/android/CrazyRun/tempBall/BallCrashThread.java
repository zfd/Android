package cst.android.CrazyRun.tempBall;

import android.util.Log;
import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.constants.WhatView;
import cst.android.CrazyRun.main.GameOverView;
import cst.android.CrazyRun.main.GameView;

import java.util.ArrayList;

/*
 * 控制球运动的线程
 */
public class BallCrashThread extends Thread {

    GameView gameView;

    public BallCrashThread(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void run()//重写run方法
    {
        while (Constant.CRASH_FLAG) {
            gameView.activity.onOpen();//震动
            gameView.activity.playSound(2, 0, 1f);//撞击的声音
            Constant.LIFES--;//挂一次
            Constant.GO_SPAN = 0.2f;//初始化速度
            //Log.i(" Constant.LIFE-->",""+Constant.LIFES);
            //停留一下
            try{
                sleep(1000);
            }
            catch (Exception e){
                Log.e("BallCrashThread",e.toString());
            }
            if (Constant.LIFES <= 0) {
                Constant.PAUSE_FLAG = false;
                Constant.DRAW_FLAG = false;
                Constant.CRASH_FLAG = false;
                gameView.activity.gameBackGroudSoundStop();
                gameView.activity.playSound(3, 0, 1f);//死亡的声音
                gameView.mainActivity.sendMessage(WhatView.GAME_OVER_VIEW);
            }
            else{
                Constant.DRAW_FLAG = true;
                Constant.CRASH_FLAG = false;
            }
        }
    }

}