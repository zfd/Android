package cst.android.CrazyRun.scences;

import cst.android.CrazyRun.main.GameView;
import cst.android.CrazyRun.utils.MatrixState;
import cst.android.CrazyRun.constants.Constant;

/**
 * Created with IntelliJ IDEA.
 * User: CST
 * Date: 14-5-15
 * Time: 下午9:13
 * To change this template use File | Settings | File Templates.
 */
public class Road {
    TextureRect road;
    public float y;
    public Road(GameView mv, float width, float height, float y){
        this.road = new TextureRect(mv, 1, width, height);
        this.y = y;
    }

    public void drawSelf(int roadId, int wallId){
        if(Constant.DRAW_FLAG)
            y -= Constant.GO_SPAN;
//        System.out.println(Constant.ROAD_CURRENT_Y);
        MatrixState.pushMatrix();//保护现场
        MatrixState.rotate(-180, 1, 0, 0);//执行旋转
        MatrixState.translate(0, y, -3);//执行平移
        road.drawSelf(roadId);//绘制前
        MatrixState.popMatrix();//恢复现场

//        MatrixState.pushMatrix();//保护现场
//        MatrixState.rotate(-90, 0, 1, 0);//执行旋转
//        MatrixState.translate(-Constant.ROAD_WID/2, -y, -3);//执行平移
//        road.drawSelf(wallId);//绘制左面
//        MatrixState.popMatrix();//恢复现场
//
//        MatrixState.pushMatrix();//保护现场
//        MatrixState.rotate(90, 0, 1, 0);//执行旋转
//        MatrixState.translate(Constant.ROAD_WID/2, -y, -3);//执行平移
//        road.drawSelf(wallId);//绘制右面
//        MatrixState.popMatrix();//恢复现场
    }
}
