package cst.android.CrazyRun.constants;

/**
 * Created by D on 2014-05-10 010.
 */
public class Constant {

    public static int isfirst =  0 ;
    public static int times=0;//次数
    //控制
    public static boolean DRAW_FLAG = true;
    public static boolean PAUSE_FLAG = false;// 是否暂停程序
    public static boolean CRASH_FLAG = false;// 是否开启碰撞效果
    public static int TIME_SPAN = 20;// 线程的时间间隔
    public static int TIME_WIN = 1000;// 游戏胜利停留时间
    public static long SCORE = 0;// 游戏得分

    //屏幕
    public static int SCREEN_WIDTH;// 屏幕的宽度
    public static int SCREEN_HEIGHT;// 屏幕的高度
    public static float WH_RATIO;// 屏幕宽高比
    public static float SCREEN_Z = 4;// 竖屏屏幕Z轴最大长度，向上为负

    //主角
    public static float BALL_R = 0.5f;
    public static float BEGIN_X = 0;// 开始X轴位置
    public static float BEGIN_Y = 0;// 开始Y轴位置
    public static float BEGIN_Z = 2.5f;// 开始Z轴位置
    public static float GO_SPAN = 0.2f;// 前后步进
    public static float MOVE_SPAN = 0;// 左右移动
    public static int LIFES;// 血条在MainActivity初始化3滴

    //场景
    public static float BLOOD_SPAN = 0.5f;// 血条的边长
    public static float NUMBER_SPAN = 0.3f;// 分数数字的宽
    public static float ROAD_LEN=100.0f;   //道路长度
    public static float EACH_ROAD_LEN=20.0f;   //每节道路长度
    public static float ROAD_WID=5.0f;    //道路宽度
    public static float Z_INDEX= 2.5f;//路的Z为3f
    public static float ENDPOINT_WIDTH=ROAD_WID;//路的Z为3f
    public static float ENDPOINT_LENGHT= 1f;//路的Z为3f
    public static float ROAD_CURRENT_Y=0;//路的终点当前y坐标
    public static float SKY_R=60.0f;

    //障碍物
    public static float TEXCUBE_LEN=1f;//长方体障碍物长
    public static float TEXCUBE_WID=1f;//长方体障碍物宽
    public static float TEXCUBE_HEI=1f;//长方体障碍物高
    public static float TEXCUBE_SPAN=EACH_ROAD_LEN/3;//障碍物相隔距离

    //金币
    public static float GOLD_R = 0.25f;//金币的半径
    public static float GOLD_H = 0.25f/3f;
    //2D界面自适应屏时的常量
    public static final int screenWidthTest=800;//测试机屏幕宽度
    public static final int screenHeightTest=480;//测试机屏幕高度

    //适应全屏的缩放比例
    public static float wRatio;
    public static float hRatio;

    //初始化常量的幂等方法
    public static boolean isInitFlag = false;//方法是否被调用过的标志位
    public static void initConst(int screenWidth,int screenHeight)
    {
        //如果方法已经执行过，则不再执行
        if(isInitFlag == true){
            return;
        }
        SCREEN_WIDTH=screenWidth;//屏幕的尺寸
        SCREEN_HEIGHT=screenHeight;
        //适应全屏的缩放比例
        wRatio=screenWidth/(float)screenWidthTest;
        hRatio=screenHeight/(float)screenHeightTest;

        //标记方法已被执行过
        isInitFlag = true;
    }

}
