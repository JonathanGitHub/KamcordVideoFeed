package com.example.jianyang.kamcordvideofeed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter.FeedAdapter;
import NetworkUtils.HTTPDownloader;


public class MainActivity extends Activity {

    private ProgressDialog pDialog;
    private ListView mListView;

    // URL to get JSON
    private static String url = "https://www.kamcord.com/app/v2/videos/feed/?feed_id=0";

    // JSON Node names
    public static final String TAG_VIDEO_URL = "video_url";
    public static final String TAG_THUMBNAIL_JSON = "thumbnails";
    public static final String TAG_TITLE = "title";
    public static final String TAG_THUMBNAIL_REGULAR="REGULAR";

    public static final String TAG_VIDEO_LIST = "video_list";
    public static final String TAG_RESPONSE = "response";

    // feeds JSONArray
    JSONArray feeds = null;

    ArrayList<HashMap<String, String>> feedsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedsList = new ArrayList<HashMap<String, String>>();

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String videoUrl = ((TextView) view.findViewById(R.id.video_url)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra(TAG_VIDEO_URL, videoUrl);
                startActivity(intent);

            }
        });

        new GetFeeds().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Async task class to get json data
     * */
    private class GetFeeds extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Fetching data...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HTTPDownloader sh = new HTTPDownloader();
            String jsonStr = sh.makeHttpCall(url);
            Log.d("Response: ", "--->" + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonStr);
                    JSONObject jsonObj = jsonResponse.getJSONObject(TAG_RESPONSE);
                    // Getting JSON Array node
                    feeds = jsonObj.getJSONArray(TAG_VIDEO_LIST);
                    for (int i = 0; i < feeds.length(); i++) {
                        JSONObject c = feeds.getJSONObject(i);
                        String title = c.getString(TAG_TITLE);
                        String video_url = c.getString(TAG_VIDEO_URL);
                        // Thumbnail node is JSON Object
                        JSONObject regular = new JSONObject();
                        JSONObject thumbnails = c.getJSONObject(TAG_THUMBNAIL_JSON);
                        String thumbnail_regular = thumbnails.getString(TAG_THUMBNAIL_REGULAR);

                        HashMap<String, String> feed = new HashMap<String, String>();

                        feed.put(TAG_TITLE, title);
                        feed.put(TAG_THUMBNAIL_REGULAR, thumbnail_regular);
                        feed.put(TAG_VIDEO_URL, video_url);

                        feedsList.add(feed);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("HTTPDownloader", "Fetching data failed...");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            FeedAdapter adapter = new FeedAdapter(MainActivity.this, feedsList);
            mListView.setAdapter(adapter);
        }

    }
}
