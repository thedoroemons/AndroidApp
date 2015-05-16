package jp.co.spajam.androidapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;

/**
 * 端末の動きを監視するクラス
 */
public class SensorMonitor implements SensorEventListener{
    private SensorManager sensorManager;
    private OnRotate callback;

    //センサーのリスナーを登録したのでアプリ終了時に消すかどうか
    private boolean isMagSensor;
    private boolean isAccSensor;
    private boolean isGyroSensor;

    // 回転行列
    private static final int MATRIX_SIZE = 16;
    float[] inR = new float[MATRIX_SIZE];
    float[] outR = new float[MATRIX_SIZE];
    float[] I = new float[MATRIX_SIZE];

    // センサーの値
    float[] orientationValues = new float[3];
    float[] magneticValues = new float[3];
    float[] accelerometerValues = new float[3];
    float[] gyroValues = new float[3];

    /**
     * コンストラクタ
     * @param callback 回転したときに呼ぶ
     * @param sensorManager メインアクティビティなどで(SensorManager) getSystemService(SENSOR_SERVICE)で撮ってきて渡す
     */
    public SensorMonitor(OnRotate callback,SensorManager sensorManager){
        this.callback = callback;
        this.sensorManager = sensorManager;

        // センサーの取得
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // センサーマネージャーへリスナーを登録
        for ( Sensor sensor : sensors ){
            if ( sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD ){
                sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
                isMagSensor = true; //リスナー登録したのでアプリ終了時に消すフラグ
            }
            if ( sensor.getType() == Sensor.TYPE_ACCELEROMETER ){
                sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
                isAccSensor = true; //リスナー登録したのでアプリ終了時に消すフラグ
            }
            if ( sensor.getType() == Sensor.TYPE_GYROSCOPE ){
                sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
                isGyroSensor = true;
            }
        }
    }

    /**
     * デストラクタのようなもの。センサーを止めるときはこれを呼ぶ。
     */
    public void destruction(){
        // センサーマネージャーのリスナー登録破棄
        if ( isAccSensor || isMagSensor || isGyroSensor ){
            sensorManager.unregisterListener(this);
            isAccSensor = false;
            isMagSensor = false;
            isGyroSensor = false;
        }
    }

    @Override
    // SensorEventListenerのメソッド
    public void onAccuracyChanged(Sensor sensor,int accuracy){
        // 何もしなくて良い
    }

    @Override
    // SensorEventListenerのメソッド
    public void onSensorChanged(SensorEvent event){
        switch( event.sensor.getType() ){
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticValues = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = event.values.clone();
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroValues = event.values.clone();
                break;
        }

        if ( magneticValues != null && accelerometerValues != null ){
            SensorManager.getRotationMatrix(inR, I, accelerometerValues, magneticValues);

            // Activityの表示が縦固定の場合
            SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
            SensorManager.getOrientation(outR, orientationValues);

            float azmuth = radianToDegree(orientationValues[0]); //縦軸横回転
            float pitch = radianToDegree(orientationValues[1]); //横軸奥側回転(スマホ上向き:90度,　スマホ縦向き:0度、裏向き:-90度、スマホ下向き:0度
            float roll = radianToDegree(orientationValues[2]); //奥軸時計回り回転（９時の向き:-90度、３時の向き:90度

            // スマホの向きが取れる
            Log.d("sensor roll","azmuth:" + azmuth + " \tpitch:" + pitch + " \troll:" + roll);
        }

        if( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ){
            float accX = event.values[SensorManager.DATA_X]; // 左向きが+ m/s^2?
            float accY = event.values[SensorManager.DATA_Y]; // 下向きが+ m/s^2?
            float accZ = event.values[SensorManager.DATA_Z]; // 奥向きが+ m/s^2?
            double accValue = Math.sqrt(accX * accX + accY * accY + accZ * accZ);

            // スマホの加速度が取れる(重力加速度込み)
            Log.d("sensor acc","accX:" + accX + " \taccY:" + accY + " \taccZ:" + accZ + " \taccValue:" + accValue);
        }

        if ( event.sensor.getType() == Sensor.TYPE_GYROSCOPE ){
            float gyroX = event.values[SensorManager.DATA_X]; // スマホを縦から裏っ返す回転が+ スマホ横がX軸
            float gyroY = event.values[SensorManager.DATA_Y]; // スマホを縦で右(反時計)回転が+ スマホ縦がY軸
            float gyroZ = event.values[SensorManager.DATA_Z]; // スマホを縦で左に倒すと+ スマホ奥がZ軸
            double gyroValue = Math.sqrt(gyroX*gyroX + gyroY*gyroY + gyroZ*gyroZ);

            // スマホの回転加速度が取れる
            Log.d("sensor gyro","gyroX:" + gyroX + " \tgyroY:" + gyroY + " \tgyroZ:" + gyroZ + " \tgyroValue:" + gyroValue);

            if ( gyroValue > 7.5 ){
                callback.onRotate( new Rotate(gyroValues, gyroValue, Rotate.FAST) );
            } else if (gyroValue > 5 ){
                callback.onRotate( new Rotate(gyroValues,gyroValue,Rotate.NORMAL) );
            } else if ( gyroValue > 2.5){
                callback.onRotate( new Rotate(gyroValues,gyroValue,Rotate.SLOW) );
            }
        }
    }

    // 端末の傾き計算用
    private int radianToDegree(float rad){
        return (int) Math.floor( Math.toDegrees(rad) );
    }
}