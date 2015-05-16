package jp.co.spajam.androidapp.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import jp.co.spajam.androidapp.MainActivity;
import jp.co.spajam.androidapp.PostJob;
import jp.co.spajam.androidapp.R;
import jp.co.spajam.androidapp.data.Job;

public class HumanModeFragment extends Fragment {

    private OnClickListener listener;
    private PostJob postJob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_human_mode, container, false);
        postJob = new PostJob(getActivity());

        Button lightBtn = (Button)view.findViewById(R.id.btn_light);
        lightBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "lightBtn!", Toast.LENGTH_SHORT).show();
                //コメントアウト解除でMainActivityのOnclickが呼ばれます
                //listener.onClickMode(v.getId());
                postJob.postJobToWebAPI(Job.LIGHT_JOB_ID,Job.NONE_VOIDE_ID);

            }
        });

        Button voiceBtn = (Button)view.findViewById(R.id.btn_voice);
        voiceBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "voiceBtn!", Toast.LENGTH_SHORT).show();
                //listener.onClickMode(v.getId());
                postJob.postJobToWebAPI(Job.VOICE_JOB_ID,Job.DEFAULT_VOIDE_ID);
            }
        });

        Button vibBtn = (Button)view.findViewById(R.id.btn_vib);
        vibBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "vibBtn!", Toast.LENGTH_SHORT).show();
                //listener.onClickMode(v.getId());
                postJob.postJobToWebAPI(Job.VIB_JOB_ID,Job.NONE_VOIDE_ID);
            }
        });


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setListener((MainActivity)activity);
    }

    /**
     * リスナーを追加する
     * @param listener
     */
    public void setListener(OnClickListener listener){
        this.listener = listener;
    }

    /**
     * リスナーを削除する
     */
    public void removeListener(){
        this.listener = null;
    }

    public interface OnClickListener {
        void onClickMode(@IdRes int id);
    }
}
