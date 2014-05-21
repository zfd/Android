package cst.android.CrazyRun.main;


import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cst.android.CrazyRun.R;
import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.constants.WhatView;
import cst.android.CrazyRun.utils.PicLoadUtil;

/**
 * Created by D on 2014-05-14 014.
 */
public class GameOverView extends SurfaceView implements SurfaceHolder.Callback {

    MainActivity activity;
    Paint paint;//画笔
    int screenWidth = Constant.SCREEN_WIDTH;//屏幕宽度
    int screenHeight = Constant.SCREEN_HEIGHT;//屏幕高度
    Bitmap bitMap;//背景图片
    Bitmap currentBitMap;//当前图片引用
    int currentX;
    int currentY;

    public GameOverView(MainActivity activity) {
        super(activity);
        this.activity = activity;
        this.getHolder().addCallback(this);//设置生命周期回调接口的实现者
        paint = new Paint();//创建画笔
        paint.setAntiAlias(true);//打开抗锯齿
        bitMap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.dead);
        bitMap = PicLoadUtil.scaleToFitFullScreen(bitMap, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_UP:
                //跳转到主菜单
                activity.sendMessage(WhatView.MENU_VIEW);
        }
        return true;
    }

    public void onDraw(Canvas canvas) {
        //绘制黑填充矩形清背景
        paint.setColor(Color.BLACK);//设置画笔颜色
        paint.setAlpha(255);
        canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
        canvas.drawBitmap(bitMap, currentX, currentY, paint);//进行平面贴图
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    public void surfaceCreated(SurfaceHolder holder) {//创建时被调用
        //计算图片位置
        currentX = screenWidth / 2 - bitMap.getWidth() / 2;
        currentY = screenHeight / 2 - bitMap.getHeight() / 2;
        SurfaceHolder myholder = GameOverView.this.getHolder();
        Canvas canvas = myholder.lockCanvas();//获取画布
        try {
            synchronized (myholder) {
                onDraw(canvas);//绘制
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                myholder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {

    }

}
