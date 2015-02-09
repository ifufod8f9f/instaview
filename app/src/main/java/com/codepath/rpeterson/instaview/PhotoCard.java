package com.codepath.rpeterson.instaview;

import org.json.JSONArray;

/**
 * Created by rohen on 2/7/15.
 */
public class PhotoCard {
    public String imageUrl;
    public String caption;
    public String username;
    public long timestamp;
    public String timeString;
    public int likeCount;
    public String userProfileImageUrl;
    public int commentCount;
    public JSONArray comments;
}
