package jp.co.spajam.androidapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import jp.co.spajam.androidapp.CameraActivity;
import jp.co.spajam.androidapp.R;


public class PetModeFragment extends Fragment {

    MediaPlayer mp = null;

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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mp = MediaPlayer.create(getActivity(), R.raw.sound);

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
    }
}
