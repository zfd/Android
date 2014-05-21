package cst.android.CrazyRun.main;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by KatzO on 14-5-12.
 */

//储存每一个Activity，并实现关闭所有Activity的操作
//需要退出应用时在代码中加 CloseActivities.getInstance().exit();
public class CloseActivities extends Application {

    private List<Activity> activityList = new LinkedList<Activity>();
    private static CloseActivities instance;

    private CloseActivities() {}

    //单例模式中获取唯一的MyApplication实例
    public static CloseActivities getInstance()
    {
        if(null == instance)
        {
            instance = new CloseActivities();
        }
        return instance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity)
    {
        activityList.add(activity);
    }
    //遍历所有Activity并finish
    public void exit()
    {
        for(Activity activity:activityList)
        {
            activity.finish();
        }
        System.exit(0);
    }

}
