package jp.co.spajam.androidapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.co.spajam.androidapp.CameraActivity;
import jp.co.spajam.androidapp.broadcastreceiver.OnReceiveJobBroadcastReceiver;
import jp.co.spajam.androidapp.service.PetPollingWebAPIService;
import jp.co.spajam.androidapp.R;


public class PetModeFragment extends Fragment {

    MediaPlayer mp = null;
    private Camera camera = null;
    private OnReceiveJobBroadcastReceiver onReceiveJobBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_mode, container, false);

        view.findViewById(R.id.photoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), CameraActivity.class);
                startActivity(it);
            }
        });

        // WebAPIのポーリングを開始
        getActivity().startService(new Intent(getActivity(), PetPollingWebAPIService.class));

        // WebAPIからJobが来たらブロードキャストレシーバーで知らせてもらう
        onReceiveJobBroadcastReceiver = new OnReceiveJobBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("JOBS");
        getActivity().registerReceiver(onReceiveJobBroadcastReceiver, intentFilter);

        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(), PetPollingWebAPIService.class));
        getActivity().unregisterReceiver(onReceiveJobBroadcastReceiver);
    }

    @Override
    public void onStart() {
        super.onStart();


        //バイブ
        //Vibrator vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        //vibrator.vibrate(10);

        //Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        //long[] pattern = {300, 1000, 200, 1000, 300, 1000}; // OFF/ON/OFF/ON...
        //vibrator.vibrate(pattern, -1);
        //バイブ

        /*
        //音声再生
        //mp = MediaPlayer.create(getActivity(), R.raw.sound);

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
            //mp.start();
        }
        //音声再生
        */
    }
}
