package jp.co.spajam.androidapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter.TwitterManager;

/**
 * Created by masaharu on 2015/05/16.
 */
public class CameraView extends SurfaceView
        implements SurfaceHolder.Callback, Camera.PictureCallback {

    private static final String TAG = CameraView.class.getSimpleName();
    private SurfaceHolder holder = null;
    private Camera camera = null;
    private static final String SDCARD_FOLDER = "/sdcard/CameraSample/";

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

    }

    // SettingActivityより拝借
    public void sendImageTweet(String imageBase64data) {
        TwitterManager.sendImageTweet("画像投稿テスト", imageBase64data, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Log.i(TAG, "画像投稿成功");
            }

            @Override
            public void failure(TwitterException e) {
                Log.i(TAG, "画像投稿失敗");
            }
        });
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        // byte data[] =>Bitmap bitmap =>String base64変換をする
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        byte[] _bArray = bos.toByteArray();
        String image64 = Base64.encodeToString(_bArray, Base64.DEFAULT);
        // String imageBinary = "data:image/jpeg;base64,"+image64;

        // 画像付きツイート送信
        try {
            sendImageTweet(image64);
        }catch (Exception e){
            //認証が済んでいない
        }

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

            //パラメータ取得
            Camera.Parameters params = camera.getParameters();
            //フラッシュモードを点灯に設定
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            //パラメータ設定
            camera.setParameters(params);

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
    }

}
