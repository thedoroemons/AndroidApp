package jp.co.spajam.androidapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUi;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;
import jp.co.spajam.androidapp.MainActivity;
import jp.co.spajam.androidapp.util.PostJob;
import jp.co.spajam.androidapp.R;
import jp.co.spajam.androidapp.data.Job;
import jp.co.spajam.androidapp.Util;
import twitter.TwitterManager;

public class HumanModeFragment extends Fragment {

    private OnClickListener listener;
    private PostJob postJob;

    private ListView mListView;

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

        mListView = (ListView) view.findViewById(R.id.list);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setListener((MainActivity) activity);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(TwitterManager.TWITTER_KEY, TwitterManager.TWITTER_SECRET);
        Fabric.with(activity, new TwitterCore(authConfig), new TweetUi());
    }



    @Override
    public void onResume() {
        super.onResume();

        String name = Util.getSpPetAccountName(getActivity());
        final UserTimeline userTimeline = new UserTimeline.Builder().screenName(name).build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getActivity(), userTimeline);

        mListView.setAdapter(adapter);
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
