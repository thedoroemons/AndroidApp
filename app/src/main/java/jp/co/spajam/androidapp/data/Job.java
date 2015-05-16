package jp.co.spajam.androidapp.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Job implements Serializable{
    private static final long serialVersionUID = 6255752248513019028L;

    public static final int LIGHT_JOB_ID = 1;
    public static final int VIB_JOB_ID = 2;
    public static final int VOICE_JOB_ID = 3;
    public static final int NONE_VOIDE_ID = 0;
    public static final int DEFAULT_VOIDE_ID = 1;

    private String created_at;
    private int id;
    private boolean is_finished;
    private int job_id;
    private int message_id;
    private String updated_at;
    private int user_id;

    public Job(JSONObject job){
        try {
            created_at = job.getString("created_at");
            id = job.getInt("id");
            is_finished = job.getBoolean("is_finished");
            job_id = job.getInt("job_id");
            message_id = job.getInt("message_id");
            updated_at = job.getString("updated_at");
            user_id = job.getInt("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCreated_at(){
        return created_at;
    }

    public int getId(){
        return id;
    }

    public boolean getIs_finished(){
        return is_finished;
    }

    public int getJob_id(){
        return job_id;
    }

    public int getMessage_id(){
        return message_id;
    }

    public String getUpdated_at(){
        return updated_at;
    }

    public int getUser_id(){
        return user_id;
    }

}