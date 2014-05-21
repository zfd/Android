package cst.android.CrazyRun.scences;

import android.util.Log;
import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.main.GameView;
import cst.android.CrazyRun.utils.MatrixState;

/**
 * Created by D on 2014-05-17 017.
 */
public class Blood {

    TextureRect blood;

    public Blood(GameView gameView,float width,float height){
        this.blood = new TextureRect(gameView,1, width, height);
    }

    public void drawSelf(int bloodId){
        for(int i=1; i<=Constant.LIFES;i++) {
            MatrixState.pushMatrix();//保护现场
            MatrixState.translate(Constant.SCREEN_Z * Constant.WH_RATIO-(i-1) * Constant.BLOOD_SPAN + 0.8f, -10, -Constant.SCREEN_Z);//执行平移
            MatrixState.rotate(-90, 1, 0, 0);//执行旋转
            blood.drawSelf(bloodId);//绘制前
            MatrixState.popMatrix();//恢复现场
        }
    }

}
