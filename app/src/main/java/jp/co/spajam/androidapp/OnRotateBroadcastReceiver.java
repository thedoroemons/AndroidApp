package jp.co.spajam.androidapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnRotateBroadcastReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;

    public OnRotateBroadcastReceiver(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Rotate rotate = (Rotate)intent.getSerializableExtra("ROTATE");

        if ( rotate.getRotate() == Rotate.FAST ){
            Log.d("sensor move","gyroValue:" + rotate.getGyroValue() + " \t ■■■非常に激しい回転");
            sendBroadcast();
        } else if ( rotate.getRotate() == Rotate.NORMAL ){
            Log.d("sensor move","gyroValue:" + rotate.getGyroValue() + " \t ■■激しい回転");
        } else if ( rotate.getRotate() == Rotate.SLOW ){
            Log.d("sensor move","gyroValue:" + rotate.getGyroValue() + " \t ■弱い回転");
        }
        //TODO カメラ起動とかを呼ぶ
        mainActivity.onRotate(new float[]{0f},10);
    }

    public void sendBroadcast(){
        Intent broadcast = new Intent();
        broadcast.setAction("TEST");
        mainActivity.sendBroadcast(broadcast);
    }
}