package jp.co.spajam.androidapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.spajam.androidapp.twitter.TwitterManager;

/**
 * Created by masaharu on 2015/05/16.
 */
public class CameraView extends SurfaceView
        implements SurfaceHolder.Callback, Camera.PictureCallback {

    private static final String TAG = CameraView.class.getSimpleName();
    private SurfaceHolder holder = null;
    private Camera camera = null;
    private static final String SDCARD_FOLDER = "/sdcard/CameraSample/";
    MediaPlayer mp = null;

    private static String tweetMessage = "ゴロゴロ気持ちいいニャー";

    private static final float VOICE_RATE = 3/4f;

    public void takePicture(Activity activity, String tweetText){

        // ツイート文を受け取る
        tweetMessage = tweetText;

        // 音を鳴らす
        ringShutterSound(activity);

        // カメラを撮って、撮れたらツイートする。
        camera.takePicture(null, null, CameraView.this);
    }

    private void ringShutterSound(Activity activity){

        // AudioManagerを取得する
        final AudioManager am = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);

        // 現在の音量を取得する
        // final int ringVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        // ストリームごとの最大音量を取得する
        int ringMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        // 音量を設定する
        am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (ringMaxVolume * VOICE_RATE), 0);

        //音声再生
        mp = MediaPlayer.create(activity, R.raw.shutter);

        if (mp.isPlaying()) { //再生中
            mp.stop();
            try {
                mp.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else { //停止中
            try {
                mp.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
        }
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
    public void sendImageTweet(String tweetText, String imageBase64data) {
        TwitterManager.sendImageTweet(tweetText, imageBase64data, new Callback<Tweet>() {
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

        String image64 = TwitterManager.encodeTobase64(bitmap);

        // 画像付きツイート送信
        //try {
            sendImageTweet(tweetMessage, image64);
        //}catch (Exception e){
            //認証が済んでいない
        //}

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
