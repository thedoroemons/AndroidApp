package jp.co.spajam.androidapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Timer;

import jp.co.spajam.androidapp.task.PollingWebAPITask;
import jp.co.spajam.androidapp.data.Job;
import jp.co.spajam.androidapp.listener.OnReceiveJob;

public class PetPollingWebAPIService extends Service implements OnReceiveJob {

    private Timer timer;
    private static final int PERIOD_MS = 10000; //このミリ秒おきにWebAPIをポーリングする

    public PetPollingWebAPIService() {
    }

    @Override
    public void onCreate() {
        // サーバーにポーリング開始 onCreateのタイミングでポーリング開始で良い？
        timer = new Timer();
        timer.schedule(new PollingWebAPITask(getApplicationContext(),(OnReceiveJob)this),0,PERIOD_MS);
    }

    @Override
    public void onReceiveJob(ArrayList<Job> jobList){
        //JSONObject[] jobs = (JSONObject[])jobList.toArray(new JSONObject[0]);
        // jobが来たことをブロードキャストする
        Intent intent = new Intent();
        intent.putExtra("JOBS",jobList);
        intent.setAction("JOBS");
        getBaseContext().sendBroadcast(intent);
    }

    @Override
    public void onDestroy(){
        // ポーリング終了
        timer.cancel();
        timer = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
