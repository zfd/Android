package cst.android.CrazyRun.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import cst.android.CrazyRun.R;
import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.constants.WhatView;

import java.util.HashMap;

/**
 * Created by D on 2014-05-10 010.
 */
public class MainActivity extends Activity {

    //界面

    public static boolean pauseFlag=false;//是否暂停程序
    int currView;
    WelcomeView welcomeView;
    MenuView menuView;
    GameView gameView;
    GameOverView gameOverView;
    MediaPlayer mMediaPlayer;
    MediaPlayer gameMusicPlayer;//游戏的背景音乐
    SoundPool soundPool;//音乐池
    HashMap<Integer,Integer> soundPoolMap;//声音池中声音ID与自定义声音ID的Map
    private boolean backGroundMusicOn=false;//背景音乐是否开启的标志
    private boolean soundOn=true;//音效是否开启的标志
    //传感器
    SensorManager sensorManager;
    Sensor sensor;
    //Handler负责跳转界面
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WhatView.WELCOME_VIEW:
                    gotoWelcomeView();
                    break;
                case WhatView.MENU_VIEW:
                    gotoMenuView();
                    break;
                case WhatView.GAME_VIEW:
                    gotoGameView();
                    break;
                case WhatView.GAME_OVER_VIEW:
                    gotoGameOverView();
                    break;
            }
        }
    };

    //向Handler发送信息的方法
    public void sendMessage(int what) {
        Message message = handler.obtainMessage(what);
        handler.sendMessage(message);
    }

    public boolean isBackGroundMusicOn() {
        return backGroundMusicOn;
    }
    public void setBackGroundMusicOn(boolean backGroundMusicOn) {
        this.backGroundMusicOn = backGroundMusicOn;
    }

    public boolean isSoundOn() {
        return soundOn;
    }
    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为横屏
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //获取屏幕的分辨率,判定屏幕比例
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int tempHeight = dm.heightPixels;
        int tempWidth = dm.widthPixels;
        if(tempHeight < tempWidth) {
            Constant.SCREEN_WIDTH = tempHeight;
            Constant.SCREEN_HEIGHT = tempWidth;
        }
        else {
            Constant.SCREEN_WIDTH = tempWidth;
            Constant.SCREEN_HEIGHT = tempHeight;
        }
        Constant.WH_RATIO = (float) Constant.SCREEN_WIDTH/ Constant.SCREEN_HEIGHT;
        //获得SensorManager对象
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //姿态传感器
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //初始化音乐
        initBackGroundSound();
        initSoundPool();
        //去欢迎界面

        gotoWelcomeView();
    }

    //SensorEventListener接口的传感器监听器
    private SensorEventListener sensorListener = new SensorEventListener() {

        private float previousX = 0;
        private float previousY = 0;
        private float previousZ = 0;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
			float []values = event.values;//获取三个轴方向上的值
            float length=(float) Math.sqrt(values[0]+values[1]+values[2]);
            length += length;
            if(Constant.PAUSE_FLAG) {
                //Constant.MOVE_SPAN = -values[2]/length;
            }
        }

    };
    public void initBackGroundSound()
    {
        mMediaPlayer=MediaPlayer.create(this,R.raw.op1);//创建背景音乐
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);//设置为循环
    }

    public void gameBackGroudSoundStart()//游戏背景音乐开始
    {
        gameMusicPlayer=MediaPlayer.create(this,R.raw.fire);//创建背景音乐
        gameMusicPlayer.start();
        gameMusicPlayer.setLooping(true);//设置为循环
    }

    public void gameBackGroudSoundStop()//游戏背景音乐结束
    {
        gameMusicPlayer.stop();
    }
    public void initSoundPool(){
        //音乐池
        soundPool=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundPoolMap=new HashMap<Integer,Integer>();
        //开局的音乐
        soundPoolMap.put(1,soundPool.load(this, R.raw.op1,1));//背景 音乐
        //球球碰撞的声音
        soundPoolMap.put(2, soundPool.load(this,R.raw.hit,1)); //撞击 的 声音

        soundPoolMap.put(3, soundPool.load(this,R.raw.dead,1)); //死亡 的 声音

        soundPoolMap.put(4, soundPool.load(this,R.raw.picture,1)); //死亡 弹出图片 的 的 声音)

        soundPoolMap.put(5, soundPool.load(this,R.raw.click,1)); //点击的声音

        soundPoolMap.put(7,soundPool.load(this,R.raw.pick,1));//捡到金币的声音

        //球壁碰撞,球进洞的声音
      //  soundPoolMap.put(3,soundPool.load(this,raw.ballin,1));
    }
    public void playSound(int sound, int loop, float ratio)
    {
        if(pauseFlag){return;}
        AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        //控制声音的大小
        volume *= ratio;
        soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
    }
    public void onOpen() //震动
    {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    @Override
    protected void onResume() {
        sensorManager.registerListener(			//注册监听器
            sensorListener, 					//监听器对象
            sensor,	//传感器类型
            SensorManager.SENSOR_DELAY_NORMAL		//传感器事件传递的频度
        );
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorListener);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent e) {
        switch(keyCode) {
            case 4:// 退出标志
                System.exit(0);
                break;
        }
        return true;
    }

    //去欢迎界面的方法
    private void gotoWelcomeView() {

        if(welcomeView==null)
            welcomeView = new WelcomeView(this);
        this.setContentView(welcomeView);
        currView= WhatView.WELCOME_VIEW;
    }

    //去游戏界面的方法
    public void gotoGameView() {
        Constant.LIFES = 3;
        Constant.SCORE = 0;
        Constant.GO_SPAN = 0.2f;
        Constant.PAUSE_FLAG = true;
        Constant.DRAW_FLAG = true;
        playSound(5 , 0, 1f);//点击的声音
        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        gameView = new GameView(this);
        this.setContentView(gameView);
        currView= WhatView.GAME_VIEW;

    }

    //加载菜单
    public void gotoMenuView() {
        if(!mMediaPlayer.isPlaying()){ this.mMediaPlayer.start();Log.v("","开始");}

        Constant.PAUSE_FLAG = false; //游戏线程，true表示正在玩
        if(menuView==null)
            menuView = new MenuView(this);
        this.setContentView(menuView);
        currView= WhatView.MENU_VIEW;
    }

    public void gotoGameOverView() {
        gameView.activity.playSound(4 , 0, 1f);//死亡的声音
        Constant.PAUSE_FLAG = false;
        Long score = Constant.SCORE;
        dialog("你的得分是："+String.valueOf(score));
        /*
        if(gameOverView==null)
            gameOverView = new GameOverView(this);
        this.setContentView(gameOverView);
        */
        currView= WhatView.GAME_OVER_VIEW;
    }

    public void dialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(str);
        builder.setIcon(R.drawable.icon);//设置图标
        builder.setTitle("游戏结束");
        builder.setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoGameView();
            }
        });
        builder.setNegativeButton("返回菜单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoMenuView();
            }
        });
        builder.create().show();
    }

}