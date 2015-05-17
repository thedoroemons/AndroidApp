package jp.co.spajam.androidapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import jp.co.spajam.androidapp.R;
import jp.co.spajam.androidapp.data.Job;
import jp.co.spajam.androidapp.fragment.PetModeFragment;

public class OnReceiveJobBroadcastReceiver extends BroadcastReceiver {

    private PetModeFragment petModeFragment;
    MediaPlayer mp = null;

    public OnReceiveJobBroadcastReceiver(PetModeFragment petModeFragment){
        this.petModeFragment = petModeFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Job> jobs = (ArrayList<Job>)intent.getSerializableExtra("JOBS");

        for (Job job : jobs) { // [HINT]for文をかいているけど実際は１個ずつjobが来ます
            int jobId = job.getJob_id();
            switch (jobId){
                case Job.LIGHT_JOB_ID:
                    //TODO ライトを光らせる
                    Log.d("job","ライトを光らせる");
                    break;
                case Job.VIB_JOB_ID:
                    // 振動させる
                    Log.d("job","振動させる");
                    Vibrator vibrator = (Vibrator) petModeFragment.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {300, 1000, 200, 1000, 300, 1000}; // OFF/ON/OFF/ON...
                    vibrator.vibrate(pattern, -1);
                    break;
                case Job.VOICE_JOB_ID:
                    // TODO 音を鳴らす
                    Log.d("job","音を鳴らす");
                    //音声再生
                    mp = MediaPlayer.create(petModeFragment.getActivity(), R.raw.sound);

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
                        mp.start();
                    }
                    break;
            }
        }
    }

}