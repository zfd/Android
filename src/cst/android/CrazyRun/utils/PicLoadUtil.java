package cst.android.CrazyRun.utils;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import cst.android.CrazyRun.constants.Constant;

public class PicLoadUtil {

	// 缩放旋转图片的方法
	public static Bitmap scaleToFit(Bitmap bm, float ratio) {
		float width = bm.getWidth(); // 图片宽度
		float height = bm.getHeight();// 图片高度
		Matrix m1 = new Matrix();
		m1.postScale(ratio, ratio);
		// 声明位图
		Bitmap bmResult = Bitmap.createBitmap
		(
				bm, 
				0, 0, 
				(int) width, (int) height, 
				m1, 
				true
		);
		return bmResult;
	}

	// 缩放旋转图片的方法,使图片全屏,不等比缩放
	public static Bitmap scaleToFitFullScreen(Bitmap bm, float wRatio,float hRatio) {
		float width = bm.getWidth(); // 图片宽度
		float height = bm.getHeight();// 图片高度

		Matrix m1 = new Matrix();
        wRatio = Constant.SCREEN_WIDTH/width;
        hRatio = Constant.SCREEN_HEIGHT/height;
		m1.postScale(wRatio, hRatio);
		// 声明位图
		Bitmap bmResult = Bitmap.createBitmap
		(
				bm, 
				0, 0, 
				(int) width, (int) height, 
				m1, 
				true
		);
		return bmResult;
	}

}
