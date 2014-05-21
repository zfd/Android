package cst.android.CrazyRun.scences;

import cst.android.CrazyRun.main.GameView;
import cst.android.CrazyRun.utils.MatrixState;

/**
 * Created with IntelliJ IDEA.
 * User: CST
 * Date: 14-5-15
 * Time: 下午10:17
 * To change this template use File | Settings | File Templates.
 */
public class Endpoint {
    TextureRect endpoint;
    float y;
    public Endpoint(GameView mv, float width, float lenght, float y){
         this.y = y;
        endpoint = new TextureRect(mv, 1, width, lenght);
    }
    public void drawSelf(int texId){
        MatrixState.pushMatrix();//保护现场
        MatrixState.rotate(-180, 1, 0, 0);//执行旋转
        MatrixState.translate(0, y, -3);//执行平移
        endpoint.drawSelf(texId);//绘制前
        MatrixState.popMatrix();//恢复现场
    }
}
