package cst.android.CrazyRun.main;

import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import cst.android.CrazyRun.R;
import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.constants.WhatView;
import cst.android.CrazyRun.utils.PicLoadUtil;
import cst.android.CrazyRun.utils.VirtualButton2D;

/**
 * Created by KatzO on 14-5-11.
 */
public class MenuView extends SurfaceView implements SurfaceHolder.Callback {

    MainActivity activity;//activity的引用
    Paint paint;//画笔引用
    private ImageView img = null;
    //虚拟按钮图片
    Bitmap menuBtnBmp0;
    Bitmap menuBtnBmp1;
    //主菜单上虚拟按钮对象引用
    VirtualButton2D startGameBtn;
    VirtualButton2D exitGameBtn;
    //背景图片
    Bitmap bgBmp;

    public MenuView(MainActivity activity) {
        super(activity);
        this.activity=activity;
        //获得焦点并设置为可触控
        this.requestFocus();
        this.setFocusableInTouchMode(true);
        getHolder().addCallback(this);//注册回调接口
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bgBmp, 0, 0, paint);
        //绘制虚拟按钮
        exitGameBtn.drawSelf(canvas, paint);
        startGameBtn.drawSelf(canvas, paint);
    }

    //加载图片
    public void initBitmap() {
        menuBtnBmp0=BitmapFactory.decodeResource(this.getResources(), R.drawable.play);
        menuBtnBmp1=BitmapFactory.decodeResource(this.getResources(), R.drawable.exit);
        bgBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);

        menuBtnBmp0= PicLoadUtil.scaleToFit(menuBtnBmp0, 1f);
        menuBtnBmp1= PicLoadUtil.scaleToFit(menuBtnBmp1, 1f);
        bgBmp= PicLoadUtil.scaleToFitFullScreen(bgBmp, Constant.wRatio, Constant.hRatio);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //点击在哪个按钮上开启哪个按钮
                if(startGameBtn.isActionOnButton(x, y)){
                    startGameBtn.pressDown();
                }
                else if(exitGameBtn.isActionOnButton(x, y)){
                    exitGameBtn.pressDown();
                }
                break;
            case MotionEvent.ACTION_UP:
                //点击在哪个按钮上开启哪个按钮
                if(startGameBtn.isActionOnButton(x, y) && startGameBtn.isDown())
                {
                    startGameBtn.releaseUp();
                    activity.sendMessage(WhatView.GAME_VIEW);
                }
                else if(exitGameBtn.isActionOnButton(x, y) && exitGameBtn.isDown())
                {
                    exitGameBtn.releaseUp();
                    CloseActivities.getInstance().exit(); //退出游戏
                }
                //抬起时关掉所有按钮
                startGameBtn.releaseUp();
                exitGameBtn.releaseUp();
                break;
        }
        //重绘界面
        repaint();
        return true;
    }

    //重新绘制的方法
    public void repaint() {
        Canvas canvas=this.getHolder().lockCanvas();
        try {
            synchronized(canvas)
            {
                onDraw(canvas);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(canvas!=null) {
                this.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        paint=new Paint();//创建画笔
        paint.setAntiAlias(true);//打开抗锯齿
        initBitmap();//初始化位图资源
        //创建虚拟按钮对象
        float xBlank = 0.5f*(Constant.SCREEN_WIDTH-menuBtnBmp0.getWidth());//设置变量，使按钮在屏幕内居中
        float yBlank = 250;
        float yLine = 30;
        startGameBtn=new VirtualButton2D(menuBtnBmp0, xBlank, yBlank);
        exitGameBtn=new VirtualButton2D(menuBtnBmp1, xBlank, yBlank+menuBtnBmp0.getHeight()+yLine);

        repaint();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

}
