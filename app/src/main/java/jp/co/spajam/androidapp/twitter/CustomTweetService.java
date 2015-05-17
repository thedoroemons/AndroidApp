package jp.co.spajam.androidapp.twitter;

import com.twitter.sdk.android.core.models.Tweet;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by fuji on 2015/05/16.
 */
public interface CustomTweetService {
    @FormUrlEncoded
    @POST("/1.1/statuses/update.json")
    void update(@Field("status") String var1, @Field("in_reply_to_status_id") Long var2, @Field("possibly_sensitive") Boolean var3, @Field("lat") Double var4, @Field("long") Double var5, @Field("place_id") String var6, @Field("display_cooridnates") Boolean var7, @Field("trim_user") Boolean var8, @Field("media_ids") String var9, Callback<Tweet> var10);


    @FormUrlEncoded
    @POST("/1.1/media/upload.jcon")
    void upload(@Part("media_data") String mediaData, Callback<MediaEntity> callback);
}
