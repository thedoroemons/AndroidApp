package twitter;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.models.Tweet;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by fuji on 2015/05/16.
 */
public class CustomTwitterApiClient extends TwitterApiClient {

    public CustomTwitterApiClient(Session session) {
        super(session);
    }

    public MyStatusesService getMyStatusesService() {
        return this.getService(MyStatusesService.class);
    }

    public interface MyStatusesService {
        @FormUrlEncoded
        @POST("/1.1/statuses/update.json")
        void update(@Field("status") String status, @Field("in_reply_to_status_id") Long inReplyToStatusId, @Field("media_ids") String mediaIds, Callback<Tweet> cb);
    }
}
