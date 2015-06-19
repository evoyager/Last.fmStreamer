package com.igusar.lastfmstreamer2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    ArrayList<HashMap<String, String>> arraylist;
    ListView listview;
    ListViewAdapter adapter;
    JSONObject jsonobject;
    JSONArray jsonarray;
    static String NAME = "name";
    static String COVER = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        new DownloadJSON().execute();
    }


    private class DownloadJSON extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            arraylist = new ArrayList<HashMap<String, String>>();
            String jsonUrl = "http://ws.audioscrobbler.com/2.0/?method=artist.search&api_key=a0548a8e4bf4b1785a60ecf1494a0af8&artist=coldplay&format=json";
            jsonobject = JSONfunctions.getJSONfromURL(jsonUrl);

            try {
                jsonobject = jsonobject.getJSONObject("results").getJSONObject("artistmatches");
                jsonarray = jsonobject.getJSONArray("artist");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    map.put(NAME, jsonobject.getString("name"));
                    String image = jsonobject.getJSONArray("image").getJSONObject(0).getString("#text");
                    map.put(COVER, image);
                    arraylist.add(map);
                }

            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            listview = (ListView) findViewById(R.id.listview);
            adapter = new ListViewAdapter(MainActivity.this, arraylist);
            listview.setAdapter(adapter);
        }
    }
}
