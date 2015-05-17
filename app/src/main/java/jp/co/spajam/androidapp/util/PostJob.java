package jp.co.spajam.androidapp.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import jp.co.spajam.androidapp.Const;

public class PostJob {

    private Context context;

    public PostJob(Context context){
        this.context = context;
    }

    /**
     *
     * @param jobId
     * @param messageId
     */
    public void postJobToWebAPI(final int jobId,final int messageId) {

        //queue
        RequestQueue queue = RequestQueueHolder.getRequestQueue(context);

        //url
        final String url = "http://128.199.201.83:9292/jobs/new";

        //Request
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("postJobToWebAPI", "RESPONSE=" + s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error
                        Log.e("error", "jobのPOSTでエラー");
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject body = new JSONObject();
                try {
                    body.put("job_id",String.valueOf(jobId));
                    body.put("user_id",String.valueOf(Const.USER_ID));
                    body.put("message_id",String.valueOf(messageId));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String httpPostBody = body.toString();;
                return httpPostBody.getBytes();
            }
        };

        queue.add(postRequest);
    }
}