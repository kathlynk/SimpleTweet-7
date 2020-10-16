package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {

    @Query("SELECT Tweet.body AS tweet_body, Tweet.createdAt as tweet_createdAt,Tweet.mediaUrl AS" +
            "tweet_mediaUrl,User.* FROM Tweet INNER JOIN User ON Tweet.userId = User.id " +
            "ORDER BY Tweet.id DESC LIMIT 30")
    List<TweetWithUser> recentItem();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);
}
