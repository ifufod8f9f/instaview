package com.codepath.rpeterson.instaview;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotoActivity extends ActionBarActivity {
    private ArrayList<PhotoCard> photoCards;
    private PhotoCardArrayAdapter photoCardArrayAdapter;
    private SwipeRefreshLayout swipeContainer;
    ListView photoCardsListView;

    public static final String CLIENT_ID = "591e47b4c7844184ba7e934c793b3994";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        photoCards = new ArrayList<PhotoCard>();
        photoCardsListView = (ListView) findViewById(R.id.lvPhotoCards);

        photoCardArrayAdapter = new PhotoCardArrayAdapter(this, photoCards);
        photoCardsListView.setAdapter(photoCardArrayAdapter);
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                photoCards.clear();

                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {

                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        PhotoCard photoCard = new PhotoCard();

                        JSONObject commentsJSON = photoJSON.getJSONObject("comments");
                        photoCard.username = photoJSON.getJSONObject("user").getString("username");
                        photoCard.userProfileImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photoCard.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photoCard.likeCount = photoJSON.getJSONObject("likes").getInt("count");
                        photoCard.timestamp = photoJSON.getInt("created_time") * 1000L;
                        photoCard.commentCount = commentsJSON.getInt("count");
                        photoCard.comments = commentsJSON.getJSONArray("data");

                        if (photoJSON.has("caption")) {
                            if (!photoJSON.isNull("caption")) {
                                photoCard.caption = photoJSON.getJSONObject("caption").getString("text");
                            }
                        }

                        photoCards.add(photoCard);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                swipeContainer.setRefreshing(false);

                updateTimes();

                photoCardArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //something
            }
        });


    }

    private void updateTimes() {
        Long now = System.currentTimeMillis();
        Long hour = 1000L * 60L * 60L;
        Long day = 1000L * 60L * 60L * 24L;
        Long resolution;
        Long timeDiff;

        for (int i = 0; i < photoCards.size(); i++) {

            PhotoCard photoCard = photoCards.get(i);
            timeDiff = now - photoCard.timestamp;

            if (timeDiff < hour) {
                resolution = DateUtils.SECOND_IN_MILLIS;
            } else if (timeDiff < day) {
                resolution = DateUtils.HOUR_IN_MILLIS;
            } else {
                resolution = DateUtils.DAY_IN_MILLIS;
            }

            photoCard.timeString = DateUtils.getRelativeTimeSpanString(photoCard.timestamp, now, resolution).toString();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
