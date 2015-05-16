package twitter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by fuji on 2015/05/16.
 */
public class TwitterManager {
    public static final String TWITTER_KEY = "bDfewBtCuKTvXB2nFDLD5TsNv";
    public static final String TWITTER_SECRET = "dSoQt0jl8aV1k0EPdu9M5fuFUQ4TYQfMpyNfBazkPBZhbWqA5m";

    /**
     * TwitterLibraryの初期化処理
     * 利用するActivityのsetContentView前に行う
     * @param context
     */
    public static void initTwitter(Context context) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
    }

    /**
     * ユーザーを指定してツイートリストを取得する
     * @param userName 取得するユーザーのアカウント名
     * @param count 取得件数 (最大180)
     * @param callback コールバック List<Tweet>オブジェクトを受け取れる
     */
    public static void getTweetList(String userName, int count, Callback<List<Tweet>> callback) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
// Can also use Twitter directly: Twitter.getApiClient()
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.userTimeline(0L, userName, count, null, null, true, null, null, null, callback);
    }

    /**
     * ツイートをする
     * @param tweetMsg ツイートするメッセージ
     * @param callback コールバック Tweetオブジェクトを受け取れる
     */
    public static void sendTweet(String tweetMsg, Callback<Tweet> callback) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.update(tweetMsg, null, null, null, null, null, null, null, callback);
    }

    /**
     * 画像付きツイートをする
     * @param tweetMsg ツイートするメッセージ
     * @param imageBase64Data Base64でエンコードした画像データ
     */
    public static void sendImageTweet(final String tweetMsg, String imageBase64Data, final Callback<Tweet> callback) {
        TwitterUploadClient twitterApiClient = new TwitterUploadClient(Twitter.getSessionManager().getActiveSession());
        TwitterUploadClient.MediaService mediaService = twitterApiClient.getMediaService();
        mediaService.upload(imageBase64Data, new Callback<MediaEntity>() {
            @Override
            public void success(Result<MediaEntity> result) {
                CustomTwitterApiClient twitterApiClient = new CustomTwitterApiClient(Twitter.getSessionManager().getActiveSession());
                CustomTwitterApiClient.MyStatusesService statusesService = twitterApiClient.getMyStatusesService();

                statusesService.update(tweetMsg, null, result.data.mediaIdString, callback);

            }

            @Override
            public void failure(TwitterException e) {
                callback.failure(e);
            }

        });
    }

    /**
     * 画像データをBase64でエンコードする
     * @param image
     * @return
     */
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }
}
