package com.codepath.apps.restclienttemplate.models;

import androidx.room.Embedded;

import java.util.ArrayList;
import java.util.List;

//TweetWithUser object has the Tweet ans User fields in side of it but the tweet object doesnt
// have the fully materialized user inside of it (limitation of Room)
public class TweetWithUser {

    // @Embedded notation flattens the properties of the User object into the object, preserving encapsulation.
    @Embedded
    User user;

    // Prefix is needed to resolve ambiguity between fields: user.id and tweet.id, user.createdAt and tweet.createdAt
    @Embedded(prefix = "tweet_")
    Tweet tweet;

    public static List<Tweet> getTweetList(List<TweetWithUser> tweetWithUsers){
        List<Tweet> tweets = new ArrayList<>();
        for(int i=0; i<tweetWithUsers.size();i++){
            Tweet tweet = tweetWithUsers.get(i).tweet;
            // limitation of Room
            tweet.user = tweetWithUsers.get(i).user;
            tweets.add(tweet);
        }
        return tweets;
    }

}
