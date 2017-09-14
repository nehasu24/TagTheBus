package com.example.neha.tagthebus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> StreetList;
    Button button;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button2);
        //We have a list view which shows all the streets and on clicking them we proceed to other actions
        //for the street
        StreetList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new Getstreets().execute();
        // listening to single list item on click where we call SingleListItem class which has the option
        //to take images for the class
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String title = (String) ((TextView) view.findViewById(R.id.street_name)).getText();
                Intent intent = new Intent(getApplicationContext(), SingleListItem.class);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
    }

    //we have getting a street as an asynchronous task beacuse otherwise the page may not lod untill all
    //the data has been downloaded but with this we can proceed
    private class Getstreets extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        //in the background we are requesting the streets from the url parser
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json/";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject data = jsonObj.getJSONObject("data");
                    // Getting JSON Array node
                    JSONArray nearstations = data.getJSONArray("nearstations");
                    // looping through All nearest stations
                    for (int i = 0; i < nearstations.length(); i++) {
                        JSONObject c = nearstations.getJSONObject(i);
                        String street_name = c.getString("street_name");
                        String lat = c.getString("lat");
                        String lon = c.getString("lon");
                        String buses = c.getString("buses");
                        String furniture = c.getString("furniture");
                        String utm_x = c.getString("utm_x");
                        String utm_y = c.getString("utm_y");
                        // tmp hash map for single street
                        HashMap<String, String> street = new HashMap<>();
                        // adding each child node to HashMap key => value
                        street.put("street_name", street_name);
                        street.put("lat", lat);
                        street.put("lon", lon);
                        street.put("buses", buses);
                        street.put("furniture", furniture);
                        street.put("utm_x", utm_x);
                        street.put("utm_y", utm_y);

                        // adding street to street list
                        StreetList.add(street);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        //when now set the details of the street to an adapter to view it in the list
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final ListAdapter adapter = new SimpleAdapter(MainActivity.this, StreetList,
                    R.layout.list_item, new String[]{"street_name", "lat", "lon", "buses", "furniture", "utm_x", "utm_y"},
                    new int[]{R.id.street_name, R.id.lat, R.id.lon, R.id.buses, R.id.furniture, R.id.utm_x, R.id.utm_y});
            lv.setAdapter(adapter);
            //We have another mode as map mode and when the user clicks on it he gets all the street
            //on the google map with some added functionality
            button.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("arraylist", StreetList);
                    startActivityForResult(intent, 500);
                }
            });
        }
    }
}