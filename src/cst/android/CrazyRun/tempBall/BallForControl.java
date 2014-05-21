package cst.android.CrazyRun.tempBall;

import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.loadObj.LoadedObjectVertexNormal;
import cst.android.CrazyRun.loadObj.LovoGoThread;
import cst.android.CrazyRun.main.GameView;
import cst.android.CrazyRun.scences.ObstacleForControl;
import cst.android.CrazyRun.utils.MatrixState;

import java.util.ArrayList;

//用于控制的球 
public class BallForControl {
    GameView gameView;
    LoadedObjectVertexNormal ball;
    LoadedObjectVertexNormal ball2;
    float translateX=0;
    float translateY=0;
    float translateZ=0;
    LovoGoThread go;
    BallCrashThread ballCrashThread; //球后退的线程
    float speed ;//z轴的速度

    public BallForControl(GameView mv, LoadedObjectVertexNormal lovn, LoadedObjectVertexNormal lovn2,float x,float y,float z) {
        this.gameView=mv;
        this.ball = lovn;
        this.ball2 = lovn2;
        this.translateX = x;
        this.translateY = y;
        this.translateZ = z;
        this.speed = 0.2f;
        go = new LovoGoThread(this);
        go.start();
    }


    public void drawSelf() {

        MatrixState.pushMatrix();
        MatrixState.translate(this.translateX, this.translateY, this.translateZ);
        if(Constant.isfirst==0) {
          //  Log.i("!!!!!!!!!!!!","ball1");
            ball.drawSelf();
        }else if(Constant.isfirst==1)
        {
            ball2.drawSelf();
          //  Log.i("!!!!!!!!!!!!!!","ball2");

        }
        Constant.times++;
        if( Constant.times ==10) {
            Constant.isfirst = 1 - Constant.isfirst;
            Constant.times=0;
        }
        MatrixState.popMatrix();
    }

    public float wallCrash(float nowX,float moveX) {
        if(nowX + moveX < -1f || nowX + moveX > 1f)
            return nowX;
        else
            return nowX + moveX;
    }

    //球前进的方法
    public void go(ArrayList<ObstacleForControl> obstacleForControls) {
        int whatToDo = 0;
        ArrayList<ObstacleForControl> removeList = new ArrayList<ObstacleForControl>();

        float tx = this.translateX + Constant.MOVE_SPAN;
        float ty = this.translateY;

        for (int i=0; i<=obstacleForControls.size(); i++) {
            if(i>10) break;
            ObstacleForControl bfc = null;
            try {
                bfc = obstacleForControls.get(i);
            }catch(Exception e){
                continue;
            }
            //箱子 前后碰撞会挂，左右碰撞没事
            if (bfc.obstacleId == 1) {
                if (Math.abs(tx - bfc.x) < 1f && Constant.DRAW_FLAG) {
                    //左右碰撞
                    if (Math.abs(ty - bfc.y) <= Constant.TEXCUBE_WID / 2 + Constant.BALL_R) {
                        whatToDo = 2;
                        break;
                    }
                    //前后碰撞
                    else if (bfc.y < 0 && ty - bfc.y <= Constant.TEXCUBE_WID / 2 + Constant.BALL_R + 2 * Constant.GO_SPAN) {
                        whatToDo = 1;
                        this.translateX = bfc.x;
                        bfc.out = true;
                        Constant.DRAW_FLAG = false;
                        Constant.CRASH_FLAG = true;
                        ballCrashThread = new BallCrashThread(gameView);
                        ballCrashThread.start();
                        break;
                    }
                }
                //标记即将走出跑到的障碍物
                if (bfc.y - ty >= Constant.TEXCUBE_WID / 2 + Constant.BALL_R + 10 * Constant.GO_SPAN) {
                    bfc.out = true;
                }
            }
            //金币 一碰到就捡了
            else if(bfc.obstacleId == 9){
                if (Math.abs(tx - bfc.x) < 1f && Constant.DRAW_FLAG) {
                    if(Math.abs(ty - bfc.y) <= Constant.GOLD_R + Constant.BALL_R + Constant.GO_SPAN){
                        gameView.activity.playSound(7, 0, 1f);//捡金币的声音
                        bfc.out = true;
                        Constant.SCORE += 100;//一个金币1000分
                    }
                }
                //标记即将走出跑到的障碍物
                if (bfc.y - ty >= Constant.GOLD_R + Constant.BALL_R + 10 * Constant.GO_SPAN) {
                    bfc.out = true;
                }
            }
        }

        switch (whatToDo) {
            case 0:
                this.translateX = wallCrash(this.translateX,Constant.MOVE_SPAN);// 左右
            default:
                break;
        }
        Constant.MOVE_SPAN = 0;
    }

    public void gogo(BallForControl al)
    {
        if(al.translateZ>3f)//检验碰撞
        {
            al.speed=-0.2f;//哪个方向的有速度，该方向上的速度置反
        }
        else if(al.translateZ<2.5f)
        {
            al.speed=0.2f;
        }
    }
}
