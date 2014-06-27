package com.staymilano;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.staymilano.database.DBHelper;
import com.staymilano.database.StartPointDAO;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class StartingPointActivity extends ActionBarActivity implements LocationListener{

	  LocationManager lm;
	  String provider;
	  Location l;
	  LatLng latlng;
	  String itineraryId;
  	  SQLiteDatabase db;
  	  GoogleMap map;
  	  
  	  String mode;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.activity_starting_point);
		   
	       Intent intent = getIntent();
	       itineraryId = intent.getStringExtra("id");
	       mode=intent.getStringExtra("mode");
		   
		   //get location service
		   lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		   Criteria c = new Criteria();
		   
		   //criteria object will select best service based on Accuracy, power consumption, response, 
		   //bearing and monetary cost, set false to use best service 
		   provider = lm.getBestProvider(c, false);
		   l = lm.getLastKnownLocation(provider);
		   if(l!=null){
		       //get latitude and longitude of the location
		       double lng = l.getLongitude();
		       double lat = l.getLatitude();
		       latlng = new LatLng(lat, lng);
		       
		       //display on map
		       setUpMapIfIneed();
		       map.addMarker(new MarkerOptions().position(latlng).title("Here you are"));
			   map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
		       
		       
		   }
	}

	@Override
	public void onLocationChanged(Location arg0)
	{
		
		double lng = l.getLongitude();
	    double lat = l.getLatitude();
	    latlng = new LatLng(lat, lng);
	    
	    setUpMapIfIneed();
	    map.addMarker(new MarkerOptions().position(latlng).title("Here you are"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

	}
	
	@Override
	public void onProviderDisabled(String arg0) {
		//TODO
	}
	
	@Override
	public void onProviderEnabled(String arg0) {
		//do nothing
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		//do nothing
	}
	
	private void setUpMapIfIneed() {
		
		if (map == null) {
			MapFragment smf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			map = smf.getMap();
		}
	}
	
	public void takeCurrent(View view){
		
		saveCoordinates(latlng);
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("id", itineraryId);
		startActivity(intent);
		finish();
	}
	
	public void setChosenAddress(View view){
		
		EditText editText = (EditText) view.findViewById(R.id.editText1);
		String message = editText.getText().toString();
		//LatLng startCoord = GoogleMapsUtils.getGeoCode(message);
		//saveCoordinates(startCoord);
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("id", itineraryId);
		startActivity(intent);
	}
	
	private void saveCoordinates(LatLng startCoord){
		
		//TODO passo 1
		db = DBHelper.getInstance(ItineraryCreationActivity.ctx).getWritableDatabase();
		String startLat = "" + startCoord.latitude;
		String startLong = "" + startCoord.longitude;
		StartPointDAO.deleteStartPointPOI(db, itineraryId);
		StartPointDAO.insertStartingPoint(db, itineraryId, startLat , startLong);
		
	}
	

}
