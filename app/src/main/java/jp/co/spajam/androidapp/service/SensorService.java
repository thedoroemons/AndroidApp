package jp.co.spajam.androidapp.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;

import jp.co.spajam.androidapp.listener.OnRotate;
import jp.co.spajam.androidapp.util.SensorMonitor;
import jp.co.spajam.androidapp.data.Rotate;

public class SensorService extends Service implements OnRotate {

    private SensorMonitor sensorMonitor;

    public SensorService() {
    }

    @Override
    public void onCreate() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorMonitor = new SensorMonitor((OnRotate)this,sensorManager); // センサー取得開始
    }

    // OnRotateのメソッド。SensorMonitorから呼ばれる。
    public void onRotate(Rotate rotate){

        // 回転したことをブロードキャストする
        Intent intent = new Intent();
        intent.putExtra("ROTATE",rotate);
        intent.setAction("ROTATE");
        getBaseContext().sendBroadcast(intent);
    }

    @Override
    public void onDestroy(){
        sensorMonitor.destruction(); // センサーの取得終了
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
