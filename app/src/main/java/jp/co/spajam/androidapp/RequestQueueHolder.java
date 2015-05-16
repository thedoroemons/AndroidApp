package jp.co.spajam.androidapp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueHolder{
    private static RequestQueue queue;

    private RequestQueueHolder(){}

    public static RequestQueue getRequestQueue(Context context){
        if ( queue == null ){
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }
}