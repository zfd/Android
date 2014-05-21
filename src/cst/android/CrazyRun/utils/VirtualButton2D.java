package cst.android.CrazyRun.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import cst.android.CrazyRun.constants.Constant;

/**
 * 
 * 虚拟按钮类
 *
 */
public class VirtualButton2D {
	public float x;
	float y;
	int width;
	int height;
	Bitmap upBmp;
	boolean isDown=false;
	public boolean isDown() {
		return isDown;
	}
	//图片的a值（透明度）
	int aDown = 150;//按下去时图片的a值
	int aUp = 255;//抬起时图片的a值
	int currA = aUp;//当前a值
	//扩展的触控范围
    float addedTouchScaleX = 10;//扩展的x方向触控范围
    float addedTouchScaleY = 5;//扩展的Y方向触控范围
    //动画时，图片缩放比例
    public float ratio = 1;
	public VirtualButton2D(Bitmap upBmp, float x, float y)
	{
		this.upBmp=upBmp;
		this.x=x;//将相对位置转换成实际位置
		this.y=y;
		this.width=upBmp.getWidth();
		this.height=upBmp.getHeight();
	}
	public void drawSelf(Canvas canvas,Paint paint)
	{
		//设置画笔的透明度
		paint.setAlpha(currA);
		//绘制图片
        canvas.drawBitmap(upBmp, x, y, paint);
		//恢复画笔的透明度
		paint.setAlpha(255);
	}
	public void pressDown()
	{
		currA = aDown;
		isDown=true;
	}
	public void releaseUp()
	{
		currA = aUp;
		isDown=false;
	}
	//判断按钮是否有触控事件的方法
	public boolean isActionOnButton(float pressX,float pressY)
	{
		if(
                pressX > (x - addedTouchScaleX) &&
                pressX < (x + addedTouchScaleX) + width &&
                pressY > (y - addedTouchScaleY) &&
                pressY < (y + addedTouchScaleY) + height
		)
		{
			return true;			
		}
		return false;
	}
}
