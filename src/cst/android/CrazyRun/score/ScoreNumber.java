package cst.android.CrazyRun.score;

import android.util.Log;
import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.main.GameView;
import cst.android.CrazyRun.scences.TextureRect;
import cst.android.CrazyRun.utils.MatrixState;

/**
 * Created by D on 2014-05-20 020.
 */
public class ScoreNumber {

    TextureRect score;
    String numStr;//数字串

    public ScoreNumber(GameView gameView,float width,float height){
        this.score = new TextureRect(gameView,1, width, height);
    }

    //绘制一个数字
    public void drawSelf(int numId,int i) {
        MatrixState.pushMatrix();//保护现场
        MatrixState.translate(-Constant.SCREEN_Z * Constant.WH_RATIO + (i - 1) * Constant.NUMBER_SPAN - 0.9f, -10, -Constant.SCREEN_Z);//执行平移
        MatrixState.rotate(-90, 1, 0, 0);//执行旋转
        score.drawSelf(numId);//绘制前
        MatrixState.popMatrix();//恢复现场
    }

    public void drawSelf(int[] numsId){
        //把分数转换成字符串
        this.numStr = Constant.SCORE+"";
        //循环绘制数字总分
        for(int i=0; i<numStr.length(); i++) {
            //获得每一位数字
            int n = numStr.charAt(i) - '0';
            //绘制相应的数字
            drawSelf(numsId[n],i+1);
        }
    }

}
