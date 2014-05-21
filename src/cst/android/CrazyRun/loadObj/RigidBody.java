package cst.android.CrazyRun.loadObj;
import android.util.Log;
import cst.android.CrazyRun.utils.MatrixState;

public class RigidBody
{
	LoadedObjectVertexNormal renderObject;//渲染者
	boolean isStatic;
	Vector3f currLocation;
	Vector3f currV;

	public RigidBody(LoadedObjectVertexNormal renderObject, boolean isStatic, Vector3f currLocation, Vector3f currV)
	{
		this.renderObject=renderObject;
		this.isStatic=isStatic;
		this.currLocation=currLocation;
		this.currV=currV;
	}

	public void drawSelf()
	{
		MatrixState.pushMatrix();
        MatrixState.scale(1f, 1f, 1f);
		MatrixState.translate(currLocation.x, currLocation.y, currLocation.z);
		renderObject.drawSelf();
		MatrixState.popMatrix();
	}



}
