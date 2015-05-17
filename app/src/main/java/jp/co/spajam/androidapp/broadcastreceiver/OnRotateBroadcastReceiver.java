package jp.co.spajam.androidapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jp.co.spajam.androidapp.CameraActivity;
import jp.co.spajam.androidapp.MainActivity;
import jp.co.spajam.androidapp.data.Rotate;

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
        } else if ( rotate.getRotate() == Rotate.NORMAL ){
            Log.d("sensor move","gyroValue:" + rotate.getGyroValue() + " \t ■■激しい回転");
        } else if ( rotate.getRotate() == Rotate.SLOW ){
            Log.d("sensor move","gyroValue:" + rotate.getGyroValue() + " \t ■弱い回転");
        }
        //TODO カメラ起動とかを呼ぶ
        Intent broadcast = new Intent();
        broadcast.putExtra(CameraActivity.EXTRA_ROTATE, rotate.getRotate());
        broadcast.setAction(CameraActivity.ACTION_ROTATE);
        context.sendBroadcast(broadcast);
    }

}