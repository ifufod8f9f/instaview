package com.codepath.rpeterson.instaview;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
    ListView photoCardsListView;

    public static final String CLIENT_ID = "591e47b4c7844184ba7e934c793b3994";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

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

                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {

                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        PhotoCard photoCard = new PhotoCard();

                        photoCard.username = photoJSON.getJSONObject("user").getString("username");
                        photoCard.userProfileImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photoCard.caption = photoJSON.getJSONObject("caption").getString("text");
                        photoCard.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photoCard.likeCount = photoJSON.getJSONObject("likes").getInt("count");
                        photoCard.timestamp = photoJSON.getInt("created_time");

                        photoCards.add(photoCard);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                photoCardArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //something
            }
        });


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
