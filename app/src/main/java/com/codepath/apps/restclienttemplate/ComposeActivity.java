package com.codepath.apps.restclienttemplate;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.ParcelableSpan;
import android.text.TextWatcher;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivty";
    String countTxt;
    EditText etCompose;
    Button btnTweet;
    TextView tvCharCount;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvCharCount = findViewById(R.id.tvCharCount);
        countTxt = "0/"+MAX_TWEET_LENGTH;
        tvCharCount.setText(countTxt);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() == 0 ){
                    tvCharCount.setTextColor(getResources().getColor(R.color.medium_gray));
                    btnTweet.setEnabled(false);
                }else if (s.length() <= MAX_TWEET_LENGTH){
                    tvCharCount.setTextColor(getResources().getColor(R.color.medium_gray));
                    btnTweet.setEnabled(true);
                }else{
                    tvCharCount.setTextColor(Color.RED);
                    btnTweet.setEnabled(false);
                }
                countTxt= s.length()+"/"+MAX_TWEET_LENGTH;
                tvCharCount.setText(countTxt);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Set a click listener on the button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate length of tweet
                String tweetContent = etCompose.getText().toString();
                //empty
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet can not be empty!",
                           Toast.LENGTH_LONG).show();
                    return;
                }
                // too long
                if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                // make an API call to twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "OnSuccess to publish Tweet!");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says:"+ tweet.body);
                            // Prepare data intent
                            Intent intent = new Intent();
                            // Pass relevant data back as a result
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            // Activity finished ok, return the data
                            setResult(RESULT_OK, intent); // set result code and bundle data for
                            // response
                            finish(); // closes the activity, pass data to parent
                        } catch (JSONException e) {
                            Log.e(TAG, "Json Exception",e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG,"onFailure to publish tweet",throwable);
                    }
                });

            }
        });

    }
}