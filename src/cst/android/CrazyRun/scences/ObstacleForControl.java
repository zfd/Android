package cst.android.CrazyRun.scences;

import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.gold.Cylinder;
import cst.android.CrazyRun.main.GameView;
import cst.android.CrazyRun.utils.MatrixState;

/**
 * Created with IntelliJ IDEA.
 * User: CST
 * Date: 14-5-14
 * Time: 下午8:21
 * To change this template use File | Settings | File Templates.
 */
public class ObstacleForControl {

    GameView mv;
    TexCube texCube;//用于绘制的桌球
    Cylinder gold; //金币类
    public float x=0;
    public float y=0;
    public float z=0;
    public int obstacleId;
    public int goldId;//金币id
    public boolean out = false;

    public ObstacleForControl(GameView mv, float halfSize, float scale, float a, float b, int mProgram, int id, float x, float y) {
        this.mv=mv;
        this.obstacleId = id;
        this.texCube = new TexCube(mv,halfSize,scale,a,b,mProgram);
        this.x = x;
        this.y = y;
    }

    public ObstacleForControl(GameView mySurfaceView,float scale,float r, float h,int n,int topTexId, int BottomTexId, int sideTexId, int goldId, float x, float y)
    {
        this.mv=mv;
        this.obstacleId = goldId;
        this.gold = new  Cylinder(mySurfaceView,scale, r,h,n,topTexId,BottomTexId,sideTexId);
        this.x = x;
        this.y = y;
    }

    public void drawSelf(int texId) {
        if(Constant.DRAW_FLAG)
            y += Constant.GO_SPAN;
//        System.out.println(y);
        MatrixState.pushMatrix();
        MatrixState.translate(this.x, y, Constant.Z_INDEX);
        //绘制立方体
        if(texCube!=null) {
            texCube.drawSelf(texId);
        }
        else if(gold!=null){
            gold.drawSelf();
        }
        MatrixState.popMatrix();
    }

}
