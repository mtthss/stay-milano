package com.staymilano;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android2ee.formation.librairies.google.map.utils.direction.*;
import com.android2ee.formation.librairies.google.map.utils.direction.model.GDirection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnMapLoadedCallback, DCACallBack {
  static final LatLng MILAN = new LatLng(45.4773, 9.1815);
  private GoogleMap map;

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

  private void getDirections() {
	  LatLng start = new LatLng(45.56,9.3);
	  LatLng end = new LatLng(45.4773, 9.1815);
      GDirectionsApiUtils.getDirection(this, start, end, GDirectionsApiUtils.MODE_WALKING);
  }
  
  @Override
  public void onMapLoaded() {
	  // Zoom in, animating the camera.
	  map.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
	  getDirections();
  }

  @Override
  public void onDirectionLoaded(List<GDirection> directions) {
	  // draw routes
      for(GDirection direction:directions) {
          Log.e("MainActivity", "onDirectionLoaded : Draw GDirections Called with path " + directions);
          GDirectionsApiUtils.drawGDirection(direction, map);
      }
  }


} 