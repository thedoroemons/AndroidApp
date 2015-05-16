package jp.co.spajam.androidapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity{

    private OnRotateBroadcastReceiver onRotateBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // センサー取得開始
        startService(new Intent(this, SensorService.class));

        // センサーが回転を検知したらブロードキャストレシーバーで知らせてもらう
        onRotateBroadcastReceiver = new OnRotateBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ROTATE");
        registerReceiver(onRotateBroadcastReceiver, intentFilter);

        // ペットモードボタン
        findViewById(R.id.petbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 人間モードボタン
        findViewById(R.id.humanbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        // 写真モードボタン
        findViewById(R.id.photoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, PhotoActivity.class);
                startActivity(it);
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this, SensorService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onRotate(float[] gyrovalues,int speed){
        Log.d("Main","onRotate:"+speed);
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
