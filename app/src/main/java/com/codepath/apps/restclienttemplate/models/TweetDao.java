// TweetDao is part of the Data Access Object (DAO) layer where SQL queries can be used

package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {

    @Query("SELECT Tweet.body AS tweet_body, Tweet.createdAt AS tweet_createdAt, Tweet.id AS tweet_id, User.* " +
            "FROM Tweet INNER JOIN User ON Tweet.userId = User.id ORDER BY Tweet.createdAt DESC LIMIT 25")
    List<TweetWithUser> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweet); // Parameter is any number of tweets, as an array, as indicated by "..."

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);
}
