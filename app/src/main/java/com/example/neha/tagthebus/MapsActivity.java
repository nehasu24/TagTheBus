package com.example.neha.tagthebus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    ArrayList<HashMap<String, String>> arl;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // we have arl as the street list
        arl = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("arraylist");
        System.out.println("...serialized data.." + arl);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String lat;
        String lon, street;
        LatLng streetLatLon = new LatLng(0, 0);
        List<Marker> markers = new ArrayList<Marker>();
        //iterating through all the streets in the list to add a marker to them
        for (int i = 0; i < arl.size(); i++) {
            HashMap<String, String> s = arl.get(i);
            lat = s.get("lat");
            lon = s.get("lon");
            street = s.get("street_name");
            System.out.println("the street " + street + " has lat " + lat + " has long " + lon);
            streetLatLon = new LatLng(Float.parseFloat(lat), Float.parseFloat(lon));
            //moving the camera to the location with some zoom ins
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(streetLatLon, 14.0f));
            //adding marker to each street with the specified latitude and longitude
            Marker marker = mMap.addMarker(new MarkerOptions().position(streetLatLon).title(street));
            markers.add(marker);
        }
        markers.size();
        //when the marker is clicked
        mMap.setOnMarkerClickListener(MapsActivity.this);
        //when the info window at the marker has been clicked
        mMap.setOnInfoWindowClickListener(MapsActivity.this);

    }

    //when the info window is clicked we go to our activity which we can have images of that particular street
    public void onInfoWindowClick(Marker marker) {

        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) { // Setting up the infoWindow with current's marker info
                // Setting a custom info window adapter for the google map
                View infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.custominfo, null);
                TextView infoTitle = (TextView) infoWindow.findViewById(R.id.textView3);
                infoTitle.setText(marker.getTitle() + "  >  ");
                System.out.print("the text is" + infoTitle);
                return infoWindow;
            }
        });
        captureNewImage(marker.getTitle());
    }

    /**
     * Called when the user clicks a marker.
     */
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the data from the marker.
        return false;
    }

    //calling the SingleListItem class for other queries of the particular street
    public void captureNewImage(String infoTitle) {
        Intent intent = new Intent(getApplicationContext(), SingleListItem.class);
        intent.putExtra("title", infoTitle);
        startActivity(intent);
    }
}
