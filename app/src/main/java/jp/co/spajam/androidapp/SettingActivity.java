package jp.co.spajam.androidapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.ByteArrayOutputStream;
import java.util.List;

import twitter.TwitterManager;


public class SettingActivity extends ActionBarActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    TwitterLoginButton mLoginButton;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterManager.initTwitter(this);
        setContentView(R.layout.activity_setting);

        mLoginButton = (TwitterLoginButton)findViewById(R.id.login_button);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.i(TAG, "success");
                getTweetList();
            }

            @Override
            public void failure(TwitterException e) {
                Log.i(TAG, "success");
            }
        });

        ((Button)findViewById(R.id.get_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTweetList();
            }
        });

        ((Button)findViewById(R.id.update_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweet();
            }
        });

        ((Button)findViewById(R.id.update_image_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImageTweet();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            // Pass the activity result to the login button.
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void getTweetList() {
        TwitterManager.getTweetList("PleasureCalling", 180, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                for (Tweet tweet:result.data) {
                    Log.i(TAG,tweet.text);
                }
            }

            @Override
            public void failure(TwitterException e) {
                Log.i(TAG,"ツイートリスト取得失敗");
            }
        });

    }

    public void sendTweet() {
        TwitterManager.sendTweet("文章投稿テスト", new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Log.i(TAG, "文章投稿成功");
            }

            @Override
            public void failure(TwitterException e) {
                Log.i(TAG, "文章投稿失敗");
            }
        });
    }

    public void sendImageTweet() {
        String imageData = "/9j/4AAQSkZJRgABAQEAAQABAAD/2wBDAAcFBgYGBQcGBgYICAcJCxIMCwoKCxcQEQ0SGxccHBoXGhkdISokHR8oIBkaJTIlKCwtLzAvHSM0ODQuNyouLy7/2wBDAQgICAsKCxYMDBYuHhoeLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi7/wAARCAAgACADASIAAhEBAxEB/8QAGQABAQADAQAAAAAAAAAAAAAABgcAAwQF/8QALRAAAQMDAwIEBQUAAAAAAAAAAQIDBAAFEQYSIUFhEzEyUQcUgZGhNHLB0fD/xAAXAQEBAQEAAAAAAAAAAAAAAAAFBgME/8QAJBEAAQMDBAIDAQAAAAAAAAAAAQIDBAARIQUTMXFBkRQjgaH/2gAMAwEAAhEDEQA/AL7f73CsUAzJi/M7W20+pxXsKnErXt0lOEtLRGb6IQkE/Ums+Ja13K6OxAf0qQGv3EZP3yBRuxxYDMNiVcGfmHpAKkNLUQhtAUU5IGCSSD2AHeuL7pD2014pXTpOnNxlvP5Uk2tz1Ye/VIY+vrtEcCnVtym+qHEgH6EeVUnT18g3+AJkJR4O1xtXqbV7H++tTSBAsFzW40Lc00+2nfhClbVpzg8E8EZFKNKQoVquOITAaD42LwTzjkdf9mj3tSVBmphSAbqtY+M4B91s+uDMj7sZJBHX9zXha8iri6hW8oHw5SAtJ7gYI/A+9HJbAmR2Q0+2xIZBSPEB2OJJJ5IBwQSemDmrNfLREvUFUSUCOdyHE+ptXuKms7R2oYjpSw03MZzw42sJOO6T/GaRG9He3majZMVYWVIFwa1aQgmMZMl2Ql95SfCy2DsQMgkAkAqPA6YHemdibL1zQU+loFSj+BXHbbDcg03HSwI7aRgrcIyfc4FL7bAZgMeE3lSjytZ81GgUw5mpal86WLJTwOuMd5qgZCIsUMpyTz+81//Z";
        TwitterManager.sendImageTweet("画像投稿テスト", imageData, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Log.i(TAG, "画像投稿成功");
            }

            @Override
            public void failure(TwitterException e) {
                Log.i(TAG, "画像投稿失敗");
            }
        });
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
}
