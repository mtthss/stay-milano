package com.staymilano;

import java.util.ArrayList;
import java.util.List;

import visualization.MapLook;
import android.app.Activity;
import android.os.Bundle;
import communications.*;
import model.Direction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnMapLoadedCallback, CallBack {
  
  static final LatLng MILAN = new LatLng(45.4773, 9.1815);
  private GoogleMap map;
  private List<LatLng> POIsequence;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	
	// initialize
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    
    // set camera options
    map.addMarker(new MarkerOptions().position(MILAN).title("Milan").snippet("Ciao"));
	map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
    map.setOnMapLoadedCallback(this);
    map.getUiSettings().setZoomControlsEnabled(false);
  }

  @Override
  public void onMapLoaded() {
	  
	  // once map is loaded zoom on the city center
	  map.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
	  
	  // retrieve directions from google server
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
	  GoogleMapsUtils.getDirection(this, POIsequence, GoogleMapsUtils.MODE_WALKING);    
  }

  @Override
  public void onDirectionLoaded(List<Direction> directions) {
	  
	  // once the google server provides the directions, draw them on the map
	  MapLook.drawDirection(directions, map);
  }


} 