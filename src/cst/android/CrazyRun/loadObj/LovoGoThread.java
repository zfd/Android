package cst.android.CrazyRun.loadObj;

import cst.android.CrazyRun.constants.Constant;
import cst.android.CrazyRun.tempBall.BallForControl;

public class LovoGoThread extends Thread{

    BallForControl al;//控制列表
	boolean flag=true;//线程控制标志位
	
	public LovoGoThread(BallForControl al)
	{
		this.al=al;
	}

	public void run()
	{
		while(flag)
		{
            al.gogo(al);
			try
			{
				sleep(Constant.TIME_SPAN);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
