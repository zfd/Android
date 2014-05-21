package cst.android.CrazyRun.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;
import cst.android.CrazyRun.R;
import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.loadObj.LoadUtil;
import cst.android.CrazyRun.loadObj.LoadedObjectVertexNormal;
import cst.android.CrazyRun.scences.*;
import cst.android.CrazyRun.score.ScoreNumber;
import cst.android.CrazyRun.score.ScoreThread;
import cst.android.CrazyRun.tempBall.BallForControl;
import cst.android.CrazyRun.tempBall.BallGoThread;
import cst.android.CrazyRun.utils.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by D on 2014-05-10 010.
 */
public class GameView extends GLSurfaceView {

    public MainActivity mainActivity;
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    private float cameraX=0;//摄像机的位置
    private float cameraY=0;
    private float cameraZ=0;
    private float targetX=0;//看点
    private float targetY=0;
    private float targetZ=0;
    private float sightDis=15;//摄像机和目标的距离
    private float angdegElevation=100;//仰角
    private float angdegAzimuth=0;//方位角
    public MainActivity activity=(MainActivity)this.getContext();
    SceneRenderer renderer;
    BallGoThread ballGoThread;		//球运动的线程
    ScoreThread scoreThread;
    ArrayList<BallForControl> ballForControls = new ArrayList<BallForControl>();
    LoadedObjectVertexNormal lovn;
    LoadedObjectVertexNormal lovn2;
    boolean isLoadedOk=false;
    int loadStep = -4;

    int math[]={-1,0,1};

    //场景
    int bloodId;    //血条id
    int roadId;      //道路纹理id
    int goldId ;    //金币的id
    int[] numsId = new int[10];   //分数的数字ids
    int obstacleCubeId;   //立方体障碍物纹理id
    int endpointId; //终点纹理id
    ArrayList<ObstacleForControl> obstacleForControls = new ArrayList<ObstacleForControl>(); //障碍物
    ArrayList<Road> roads = new ArrayList<Road>();     //道路
    Blood blood;   //血条
    ScoreNumber score;   //分数
    Endpoint endpoint;    //终点
    Sky sky;
    int skyId;
    TextureRect processBar;
    int processBarId;
    TextureRect background;
    int backgroundId;

    public GameView(MainActivity activity) {
        super(activity);
        this.mainActivity = activity;
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        renderer = new SceneRenderer();    //创建场景渲染器
        setRenderer(renderer);                //设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
        requestFocus();                 //获取焦点
        setFocusableInTouchMode(true);      // 设置为可触控
        activity.gameBackGroudSoundStart();
    }

    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Constant.DRAW_FLAG) {
                    if (x < Constant.SCREEN_WIDTH / 2) {
                        Constant.MOVE_SPAN = -1f;
                    } else {
                        Constant.MOVE_SPAN = 1f;
                    }
                }
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }

    private class SceneRenderer implements Renderer {

        public void onDrawFrame(GL10 gl) {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            double angradElevation=Math.toRadians(angdegElevation);//仰角（弧度）
            double angradAzimuth=Math.toRadians(angdegAzimuth);//方位角
            cameraX=(float) (targetX+sightDis*Math.cos(angradElevation)*Math.sin(angradAzimuth));
            cameraY=(float) (targetY+sightDis*Math.sin(angradElevation));
            cameraZ=(float) (targetZ+sightDis*Math.cos(angradElevation)*Math.cos(angradAzimuth));
            //设置camera位置
            MatrixState.setCamera(

                    cameraX, //人眼位置的X
                    cameraY, //人眼位置的Y
                    cameraZ, //人眼位置的Z

                    targetX, //人眼球看的点X
                    targetY, //人眼球看的点Y
                    targetZ, //人眼球看的点Z

                    0,  //头的朝向
                    -1,
                    0
            );
            //光源
            MatrixState.setLightLocation(0, 20, -15);




            if(!isLoadedOk)//如过没有加载完成
            {
                drawOrthLoadingView();
            }
            else
            {
                ScencesDraw();
            }



        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            float ratio= (float) width / height;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, Constant.SCREEN_Z, 100);
            //初始化光源
            MatrixState.setLightLocation(0, 20, -15);
            //

//            ballGoThread=new BallGoThread(GameView.this,ballForControls,obstacleForControls);
//            //线程标志位设为true
//            Constant.PAUSE_FLAG = true;
//            //开启线程
//            ballGoThread.start();
//            //计算分数的线程
//            scoreThread = new ScoreThread();
//            scoreThread.start();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(1f,1f,1f, 1f);
            //启用深度测试
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //设置为打开背面剪裁
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //开启混合
            GLES20.glEnable(GLES20.GL_BLEND);
            //设置源混合因子与目标混合因子
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            //初始化变换矩阵
            MatrixState.setInitStack();
            //创建物体
            //创建物体
//            lovn = LoadUtil.loadFromFile("11.obj", GameView.this.getResources(), GameView.this);
//            lovn2 = LoadUtil.loadFromFile("23.obj", GameView.this.getResources(), GameView.this);
//            ballForControls.add(new BallForControl(GameView.this,lovn,lovn2 ,Constant.BEGIN_X, Constant.BEGIN_Y, Constant.BEGIN_Z));

            processBar = new TextureRect(GameView.this, 1, 5, 1);
            processBarId = initTexture(R.drawable.process_top);
            background = new TextureRect(GameView.this, 1,Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
            backgroundId =  initTexture(R.drawable.background);
        }

    }

    public void initNumbersTexTure(){
        numsId[0] = initTexture(R.drawable.number0);
        numsId[1] = initTexture(R.drawable.number1);
        numsId[2] = initTexture(R.drawable.number2);
        numsId[3] = initTexture(R.drawable.number3);
        numsId[4] = initTexture(R.drawable.number4);
        numsId[5] = initTexture(R.drawable.number5);
        numsId[6] = initTexture(R.drawable.number6);
        numsId[7] = initTexture(R.drawable.number7);
        numsId[8] = initTexture(R.drawable.number8);
        numsId[9] = initTexture(R.drawable.number9);
    }

    public int initTexture(int drawableId) {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId=textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        //通过输入流加载图片===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try
        {
            bitmapTmp = BitmapFactory.decodeStream(is);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end=====================

        //实际加载纹理
        GLUtils.texImage2D
                (
                        GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                        0,                      //纹理的层次，0表示基本图像层，可以理解为直接贴图
                        bitmapTmp,              //纹理图像
                        0                      //纹理边框尺寸
                );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
    }

    public void ScencesDraw(){
        //绘制物体
        MatrixState.pushMatrix();
        for(BallForControl ballForControl:ballForControls) {
            ballForControl.drawSelf();
        }
        MatrixState.popMatrix();

        Constant.ROAD_CURRENT_Y =  roads.get(roads.size()-1).y;
        System.out.println( roads.size());
        if ( roads.get(0).y<-Constant.EACH_ROAD_LEN){
            Random random = new Random();
            float x= (random.nextInt()%2==0?0:1)*(random.nextInt()%2==0?1:-1);
            float y= (random.nextInt()%2==0?-0.5f:-1)*(roads.get(roads.size()-1).y+ Constant.EACH_ROAD_LEN);
            int gx =gold((int)x,random) ;
          /*  Log.d("gx",""+gx);
            while(math[gx]==x)
            {
                gx =(int)(Math.random()*100)%3;
            }*/
            roads.remove(roads.get(0));
            roads.add(new Road(GameView.this, Constant.ROAD_WID, Constant.EACH_ROAD_LEN, roads.get(roads.size()-1).y+ Constant.EACH_ROAD_LEN));
            obstacleForControls.add(
                    new ObstacleForControl(
                            GameView.this, Constant.TEXCUBE_LEN/2,
                            Constant.TEXCUBE_LEN, Constant.TEXCUBE_WID, Constant.TEXCUBE_HEI,
                            1, 1,x, y)
            );

            obstacleForControls.add(
                    //     ObstacleForControl(GameView mySurfaceView,float scale,float r, float h,int n,int topTexId, int BottomTexId, int sideTexId, int goldId, float x, float y)
                    // float h,
                    // int n,int topTexId, int BottomTexId, int sideTexId, int goldId, float x, float y
                    new ObstacleForControl(
                            GameView.this,//GameView mySurfaceView,
                            Constant.TEXCUBE_LEN, // float scale,
                            Constant.GOLD_R, // float r,
                            Constant.GOLD_H,  // float h
                            36,  // int n
                            goldId, goldId, goldId, 9,math[gx] , y
                    )
            );

        }


        MatrixState.pushMatrix();
        for(Road road:roads) {
            road.drawSelf(roadId, obstacleCubeId);
        }


        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        endpoint.drawSelf(endpointId);
        MatrixState.popMatrix();

        ArrayList<ObstacleForControl> removeList = new ArrayList<ObstacleForControl>();
        //绘制障碍物
        for(ObstacleForControl obstacleForControl:obstacleForControls) {
            if(!obstacleForControl.out)
                obstacleForControl.drawSelf(obstacleCubeId);
            else
                removeList.add(obstacleForControl);
        }
        obstacleForControls.removeAll(removeList);

        MatrixState.pushMatrix();
        MatrixState.rotate(-90, 1, 0, 0);
        MatrixState.translate(0, -10f, 3);
        sky.drawSelf(skyId);
        MatrixState.popMatrix();


        //将分数跟血条置顶
        //关闭深度测试
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        //绘制血
        MatrixState.pushMatrix();
        blood.drawSelf(bloodId);
        MatrixState.popMatrix();
        //绘制分数
        MatrixState.pushMatrix();
        score.drawSelf(numsId);
        MatrixState.popMatrix();
        //启用深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    }

    //正交投影绘制加载界面
    public void drawOrthLoadingView(){
        MatrixState.pushMatrix();
        MatrixState.rotate(-90, 1, 0, 0);//执行旋转
        MatrixState.translate(0, -3, 0);//执行平移
        background.drawSelf(backgroundId);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.rotate(-180, 1, 0, 0);//执行旋转
        MatrixState.translate(loadStep, -2, -3);//执行平移
        processBar.drawSelf(processBarId);
        MatrixState.popMatrix();
        ScencesObjCreate();
    }

    public void ScencesObjCreate(){
        switch (loadStep)  {
            case -4:
                //GameView mv, float halfSize, float scale, float a, float b, int mProgram, int id, float x, float y
                goldId = initTexture(R.drawable.gold);//金币的id
                Log.d("",""+goldId);
                for(int i = 0; i < Constant.ROAD_LEN- Constant.TEXCUBE_SPAN; i+= 2* Constant.TEXCUBE_SPAN){  //创建障碍物
                    if(i < 1){
                        continue;
                    }
                }
                for(int i = 0; i < Constant.ROAD_LEN+ Constant.EACH_ROAD_LEN; i+= Constant.EACH_ROAD_LEN){  //创建道路
                    roads.add(new Road(GameView.this, Constant.ROAD_WID, Constant.EACH_ROAD_LEN, 1.0f * i));
                }
                endpoint = new Endpoint(GameView.this, Constant.ENDPOINT_WIDTH, Constant.ENDPOINT_LENGHT, Constant.ROAD_LEN) ;  //(mv, 终点线宽度，终点线长度，终点线位置)
                roadId =  initTexture(R.drawable.tex_floor);   //创建道路纹理id
                obstacleCubeId = initTexture(R.drawable.stonec);  //创建立方体障碍物id
                goldId = initTexture(R.drawable.gold);//金币的id
                endpointId = initTexture(R.drawable.barrel_red);
                sky=new Sky(GameView.this);
                skyId=initTexture(R.drawable.sky);
                //创建血条
                blood = new Blood(GameView.this, Constant.BLOOD_SPAN, Constant.BLOOD_SPAN);//一滴血
                bloodId = initTexture(R.drawable.blood);
                //创建分数
                score = new ScoreNumber(GameView.this, Constant.NUMBER_SPAN, Constant.NUMBER_SPAN+0.1f);//一个数字
                initNumbersTexTure();
                loadStep += 1;
                break;
            case -3:
                lovn = LoadUtil.loadFromFile("11.obj", GameView.this.getResources(), GameView.this);

                loadStep += 1;
                break;
            case -2:
                lovn2 = LoadUtil.loadFromFile("23.obj", GameView.this.getResources(), GameView.this);

                loadStep += 1;
                break;
            case -1:
                ballForControls.add(new BallForControl(GameView.this,lovn,lovn2 , Constant.BEGIN_X, Constant.BEGIN_Y, Constant.BEGIN_Z));
                loadStep += 1;
                break;
            case 0:
                ballGoThread=new BallGoThread(GameView.this,ballForControls,obstacleForControls);
                //线程标志位设为true
                Constant.PAUSE_FLAG = true;
                //开启线程
                ballGoThread.start();
                //计算分数的线程
                scoreThread = new ScoreThread();
                scoreThread.start();
                isLoadedOk = true;
                break;
        }
    }

    public int gold(int x, Random random ) {
        switch(x)
        {
            case -1:
                return  random.nextInt()%2==0?1:2;

            case 0:
                return random.nextInt()%2==0?0:2;
            case 1:
                return random.nextInt()%2==0?0:1;
        }
        return 0;
    }

}