package jp.co.spajam.androidapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.co.spajam.androidapp.data.Job;
import jp.co.spajam.androidapp.fragment.PetModeFragment;

public class OnReceiveJobBroadcastReceiver extends BroadcastReceiver {

    private PetModeFragment petModeFragment;

    public OnReceiveJobBroadcastReceiver(PetModeFragment petModeFragment){
        this.petModeFragment = petModeFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Job> jobs = (ArrayList<Job>)intent.getSerializableExtra("JOBS");

        for (Job job : jobs) {
            int jobId = job.getJob_id();
            Log.d("test", "jobId:" + jobId);
            //TODO jobIDによってカメラ起動したり光らせたりする
        }
    }

}