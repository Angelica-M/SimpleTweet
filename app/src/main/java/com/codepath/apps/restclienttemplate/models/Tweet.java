// View Twitter Api documentation here https://developer.twitter.com/en/docs/twitter-api/v1
package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="userId"))
public class Tweet {

    @ColumnInfo
    public String body, createdAt;

    @ColumnInfo
    @PrimaryKey
    public long id;

    @Ignore
    public User user;

    @ColumnInfo
    public long userId;

    // Empty constructor needed by the Parceler library
    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getLong("id");
        User user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user = user;
        tweet.userId = user.id;
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getFormattedTimestamp() {
        // Returns the correct time for the tweet returning as described in https://hackmd.io/@nesquena/HJZhnNLSE?type=view
        // createdAt is the actual date string pulled from the JSON for the tweet (created_at field)
        return TimeFormatter.getTimeDifference(createdAt);
    }
}
