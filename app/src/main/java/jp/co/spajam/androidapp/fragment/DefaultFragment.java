package jp.co.spajam.androidapp.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import jp.co.spajam.androidapp.MainActivity;
import jp.co.spajam.androidapp.R;

public class DefaultFragment extends Fragment {

    private OnClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button humanBtn = (Button)getActivity().findViewById(R.id.humanbtn);
        humanBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "HumanMode!", Toast.LENGTH_SHORT).show();
                listener.onClickMode(v.getId());
            }
        });

        Button petBtn = (Button)getActivity().findViewById(R.id.petbtn);
        petBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "PetMode!", Toast.LENGTH_SHORT).show();
                listener.onClickMode(v.getId());
            }
        });

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


