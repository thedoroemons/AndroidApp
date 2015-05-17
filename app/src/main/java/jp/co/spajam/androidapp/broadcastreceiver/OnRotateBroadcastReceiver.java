package jp.co.spajam.androidapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import jp.co.spajam.androidapp.CameraActivity;
import jp.co.spajam.androidapp.MainActivity;
import jp.co.spajam.androidapp.data.Rotate;
import jp.co.spajam.androidapp.fragment.PetModeFragment;

public class OnRotateBroadcastReceiver extends BroadcastReceiver {

    private PetModeFragment petModeFragment;

    public OnRotateBroadcastReceiver(PetModeFragment petModeFragment){
        this.petModeFragment = petModeFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Rotate rotate = (Rotate)intent.getSerializableExtra("ROTATE");
//        long[] pattern = {0};

        if ( rotate.getRotate() == Rotate.FAST ){
            Log.d("sensor move","gyroValue:" + rotate.getGyroValue() + " \t ■■■非常に激しい回転");
//            pattern = new long[]{100,500,300,500,300,700}; // OFF/ON/OFF/ON...
        } else if ( rotate.getRotate() == Rotate.NORMAL ){
            Log.d("sensor move","gyroValue:" + rotate.getGyroValue() + " \t ■■激しい回転");
//            pattern = new long[]{100,500,300,700}; // OFF/ON/OFF/ON...
        } else if ( rotate.getRotate() == Rotate.SLOW ){
            Log.d("sensor move","gyroValue:" + rotate.getGyroValue() + " \t ■弱い回転");
//            pattern = new long[]{100,700}; // OFF/ON/OFF/ON...
        }

//        // テストで振動
//        Vibrator vibrator = (Vibrator) petModeFragment.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(pattern, -1);

        //TODO カメラ起動とかを呼ぶ
        Intent broadcast = new Intent();
        broadcast.putExtra(CameraActivity.EXTRA_ROTATE, rotate.getRotate());
        broadcast.setAction(CameraActivity.ACTION_ROTATE);
        context.sendBroadcast(broadcast);
    }

}