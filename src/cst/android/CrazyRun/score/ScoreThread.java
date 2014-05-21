package cst.android.CrazyRun.score;

import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.main.GameView;

import java.util.ArrayList;

/*
 * 控制球运动的线程
 */
public class ScoreThread extends Thread {

    float upScore = 10000;
    float levelScore = 0;
    @Override
    public void run()//重写run方法
    {
        while(Constant.PAUSE_FLAG) {
            if(Constant.DRAW_FLAG)//碰撞停留的时候不加分
                Constant.SCORE += Constant.GO_SPAN * 10; //分数换算公式 0.2 X 100 = 20 每个时间间隔加20分
            //if(Constant.SCORE - levelScore >= upScore){
                Constant.GO_SPAN += 0.0005f;
            //    levelScore += upScore;
            //}
            try{
                Thread.sleep(Constant.TIME_SPAN);//停留一段时间
            }
            catch(Exception e){
                e.printStackTrace();//打印异常
            }
        }
    }

}
