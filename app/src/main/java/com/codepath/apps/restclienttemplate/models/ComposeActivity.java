package com.codepath.apps.restclienttemplate.models;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.RED;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_lENGTH = 280;

    EditText etCompose;
    Button btnTweet;
    TextView tvCharCount;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvCharCount = findViewById(R.id.tvCharCount);

        client = TwitterApp.getRestClient(this);

        // Set click listener on btnTweet
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry, tweet cannot be empty", Toast.LENGTH_LONG).show(); // For debugging
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_lENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry, tweet is too long", Toast.LENGTH_LONG).show(); // For debugging
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show(); // For debugging
                // Make an API call to Twitter to publish tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet)); // Convert tweet into a Parcel object
                            setResult(RESULT_OK, intent);
                            finish(); // Closes the activity, passes data to parent
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                });
            }
        });

        // NOTE: METHOD BELOW MAY NOT BE NEEDED
        // Set text changed listener on etCompose
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                // Editable s is the tweetContent in etCompose, use s.toString() to convert to string
                int newCharCount = s.length(); // s.length() is the number of chars in Editable s
                String newTvCharCount = Integer.toString(newCharCount) + " / " + Integer.toString(MAX_TWEET_lENGTH);
                tvCharCount.setText(newTvCharCount);
                if (newCharCount > MAX_TWEET_lENGTH) {
                    tvCharCount.setTextColor(RED);
                } else {
                    tvCharCount.setTextColor(GRAY);
                }
            }
        });

    }
}