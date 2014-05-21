package cst.android.CrazyRun.scences;

import cst.android.CrazyRun.main.GameView;
import cst.android.CrazyRun.utils.MatrixState;

/**
 * Created with IntelliJ IDEA.
 * User: CST
 * Date: 14-5-13
 * Time: 下午11:28
 * To change this template use File | Settings | File Templates.
 */
public class TexCube {
    TextureRect textureRect;//用于绘制各个面的纹理矩形
    float halfSize;//立方体的半边长
    int mProgram;
    GameView mv;
    public TexCube(GameView mv,float halfSize,float scale,float a,float b,int mProgram)
    {
        this.mv=mv;	//保存MySurfaceView引用
        textureRect=new TextureRect(mv, scale, a, b);//创建纹理矩形
        this.mProgram=mProgram;//保存着色器程序引用
        this.halfSize=halfSize;	//保存半长
    }
    public void drawSelf(int texId)
    {
        MatrixState.pushMatrix();//保护现场
        MatrixState.translate(0, halfSize, 0);//执行平移
        MatrixState.rotate(-90, 1, 0, 0);//执行旋转
        textureRect.drawSelf(texId);//绘制前
        MatrixState.popMatrix();//恢复现场

        MatrixState.pushMatrix();//保护现场
        MatrixState.translate(0, -halfSize, 0);//执行平移
        MatrixState.rotate(-90, 1, 0, 0);//执行旋转
        textureRect.drawSelf(texId);//绘制后
        MatrixState.popMatrix();//恢复现场

        MatrixState.pushMatrix();//保护现场
        MatrixState.translate(-halfSize, 0, 0);//执行平移
        MatrixState.rotate(90, 0, 1, 0);//执行旋转
        textureRect.drawSelf(texId);//绘制左面
        MatrixState.popMatrix();//恢复现场

        MatrixState.pushMatrix();//保护现场
        MatrixState.translate(halfSize, 0, 0);//执行平移
        MatrixState.rotate(90, 0, 1, 0);//执行旋转
        textureRect.drawSelf(texId);//绘制右面
        MatrixState.popMatrix();//恢复现场

        MatrixState.pushMatrix();//保护现场
        MatrixState.translate(0, 0, halfSize);//执行平移
        MatrixState.rotate(180, 0, 1, 0);//执行旋转
        textureRect.drawSelf(texId);//绘制前面
        MatrixState.popMatrix();//恢复现场

        MatrixState.pushMatrix();//保护现场
        MatrixState.translate(0, 0, -halfSize);//执行平移
        MatrixState.rotate(180, 0, 1, 0);//执行旋转
        textureRect.drawSelf( texId);//绘制后面
        MatrixState.popMatrix();//恢复现场
    }
}
