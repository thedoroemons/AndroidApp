package jp.co.spajam.androidapp;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by masaharu on 2015/05/16.
 */
public class CameraView extends SurfaceView
        implements SurfaceHolder.Callback, Camera.PictureCallback {
    private SurfaceHolder holder = null;
    private Camera camera = null;
    private static final String SDCARD_FOLDER = "/sdcard/CameraSample/";

    //MyTimerTask timerTask = null;
    //Timer   mTimer   = null;
    //Handler mHandler = new Handler();

    public void takePicture(){
        camera.takePicture(null, null, CameraView.this);
    }


    public CameraView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 保存用フォルダ作成
        File dirs = new File(SDCARD_FOLDER);
        if(!dirs.exists()) {
            dirs.mkdir();
        }

//        //タイマーの初期化処理
//        timerTask = new MyTimerTask();
//        mTimer = new Timer(true);
//        mTimer.schedule( timerTask, 0, 3000);

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        // TODO Auto-generated method stub
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_kkmmss");
        String datName = "P" + date.format(new Date()) + ".jpg";
        try {
            // データ保存
            savePhotoData(datName, data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("debug","保存失敗");
            if(camera != null) {
                camera.release();
                camera = null;
            }
        }
        // プレビュー再開
        camera.startPreview();
    }

    private void savePhotoData(String datName, byte[] data) throws Exception {
        // TODO Auto-generated method stub
        FileOutputStream outStream = null;

        try {
            outStream = new FileOutputStream(SDCARD_FOLDER + datName);
            outStream.write(data);
            outStream.close();
        } catch (Exception e) {
            if(outStream != null) {
                outStream.close();
            }
            throw e;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            camera.takePicture(null, null, this);
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if(camera != null) {
            return;
        }
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            if(camera != null) {
                camera.release();
                camera = null;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        Log.v("Camera", "format=" + format + ", width=" + width + ", height=" + height);
        Camera.Parameters params = camera.getParameters();
        //params.setPreviewSize(width, height);
        camera.setDisplayOrientation(90);
        camera.setParameters(params);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera.stopPreview();
        camera.release();
        camera = null;
//        mTimer.cancel();
//        mTimer=null;
    }

//    // タイマータスク用のクラス
//    class MyTimerTask extends TimerTask {
//
//        @Override
//        public void run() {
//            mHandler.post( new Runnable() {
//                public void run() {
//                    if(mTimer == null){
//                        return;
//                    }
//                    camera.takePicture(null, null, CameraView.this);
//                }
//            });
//        }
//    }

}
