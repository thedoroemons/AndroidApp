package jp.co.spajam.androidapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
import jp.co.spajam.androidapp.data.Rotate;
import jp.co.spajam.androidapp.twitter.TwitterManager;


public class CameraActivity extends Activity {

    private static final int COUNT_PHOTO = 1;
    public static final String ACTION_ROTATE = "ACTION_MOVE";
    public static final String EXTRA_ROTATE =  "EXTRA_MOVE";

    CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ツイートするための初期化
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TwitterManager.TWITTER_KEY, TwitterManager.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_camera);
        // 画面をフルスクリーンに設定
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // カメラ用ビューの設定
        setContentView(cameraView = new CameraView(this));
        IntentFilter intentFilter = new IntentFilter(ACTION_ROTATE);
        registerReceiver(receiver,intentFilter);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        private int count = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(EXTRA_ROTATE, -1);
            Log.d("Test","TEST" + type);
            if(type == Rotate.FAST){
                count++;
                Log.d("Test","COUNT" + count);
                if(count >= COUNT_PHOTO){
                    cameraView.takePicture();
                    count = 0;

                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
