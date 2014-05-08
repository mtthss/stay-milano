package com.staymilano;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import communications.layer.*;
import model.Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnMapLoadedCallback, CallBack {
  
  static final LatLng MILAN = new LatLng(45.4773, 9.1815);
  private GoogleMap map;
  private List<LatLng> POIsequence;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	  
    super.onCreate(savedInstanceState);
    // initialize map
    setContentView(R.layout.activity_main);
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    // create classic red marker with a title and a description
    Marker milan = map.addMarker(new MarkerOptions()
        .position(MILAN)
        .title("Milan")
        .snippet("Ciao"));
    // center on milan coordinates
	map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
	// zoom when loaded
    map.setOnMapLoadedCallback(this);
    // set control
    map.getUiSettings().setZoomControlsEnabled(false);
  }

  @Override
  public void onMapLoaded() {
	  
	  // Zoom in, animating the camera.
	  map.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
	  getDirections();
  }
  
  private void getDirections() {
	  
	  // fetch ordered sequence of POI from database
	  LatLng start = new LatLng(45.56,9.3);
	  LatLng intermediate = new LatLng(45.45,9.21);
	  LatLng end = new LatLng(45.4773, 9.1815);
	  POIsequence = new ArrayList<LatLng>();
	  POIsequence.add(start);
	  POIsequence.add(intermediate);
	  POIsequence.add(end);
	  
	  // get directions for the specified itinerary
	  GoogleMapsUtils.getDirection(this, start, intermediate, GoogleMapsUtils.MODE_WALKING);  
	  GoogleMapsUtils.getDirection(this, intermediate, end, GoogleMapsUtils.MODE_WALKING);	  
  }

  @Override
  public void onDirectionLoaded(List<Direction> directions) {
	  GoogleMapsUtils.drawDirection(directions, map);
  }


} 