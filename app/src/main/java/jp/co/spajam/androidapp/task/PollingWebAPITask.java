package jp.co.spajam.androidapp.task;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TimerTask;

import jp.co.spajam.androidapp.Const;
import jp.co.spajam.androidapp.util.RequestQueueHolder;
import jp.co.spajam.androidapp.data.Job;
import jp.co.spajam.androidapp.listener.OnReceiveJob;

public class PollingWebAPITask extends TimerTask{

    Context context;
    OnReceiveJob callback;

    public PollingWebAPITask(Context context,OnReceiveJob callback){
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        getCurrentJobs();
    }

    /**
     * 未処理のジョブを取得
     * あればcallbackを呼ぶ
     */
    private void getCurrentJobs(){
        String url = "http://128.199.201.83:9292/jobs/current"; //未処理のジョブ

        RequestQueue mQueue = RequestQueueHolder.getRequestQueue(context);
        mQueue.add(new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Job> jobList = new ArrayList<Job>();
                        try {
                            if ( response.has("job") ) {
                                JSONObject job = response.getJSONObject("job");
                                Job jobData = new Job(job);
                                if (jobData.getUser_id() == Const.USER_ID) {
                                    jobList.add(jobData);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if ( jobList.size() > 0 ){
                            callback.onReceiveJob(jobList);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // エラー処理 error.networkResponseで確認
                        // エラー表示など
                        Log.e("error", error.toString());
                    }
                }));
    }

    /**
     * 過去含めジョブ全件取得
     * あればcallbackを呼ぶ
     */
    private void testGetAllJobs(){
        String url = "http://128.199.201.83:9292/jobs"; //ジョブ全件

        RequestQueue mQueue = RequestQueueHolder.getRequestQueue(context);
        mQueue.add(new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Job> jobList = new ArrayList<Job>();
                        try {
                            JSONArray jobs = response.getJSONArray("jobs");
                            for (int i = 0; i < jobs.length(); i++) {
                                JSONObject jobObj = jobs.getJSONObject(i);
                                JSONObject job = jobObj.getJSONObject("job");
                                Job jobData = new Job(job);
                                if ( jobData.getUser_id() == Const.USER_ID ) {
                                    jobList.add(jobData);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if ( jobList.size() > 0 ){
                            callback.onReceiveJob(jobList);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // エラー処理 error.networkResponseで確認
                        // エラー表示など
                        Log.e("error", error.toString());
                    }
                }));
    }
}