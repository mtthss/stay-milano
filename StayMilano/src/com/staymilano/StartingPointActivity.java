package com.staymilano;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.staymilano.database.DBHelper;
import com.staymilano.database.StartPointDAO;
import com.staymilano.model.UserInfo;

import communications.GoogleMapsUtils;
import communications.GeoCodeCallBack;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class StartingPointActivity extends ActionBarActivity implements LocationListener, GeoCodeCallBack{

	  LocationManager lm;
	  String provider;
	  Location l;
	  LatLng latlng;
	  String itineraryId;
  	  SQLiteDatabase db;
  	  GoogleMap map;
  	  Marker mMarker;  	  
  	  String mode;
  	  
  	  public static final String ORIGIN="origin";
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.activity_starting_point);
		   
	       Intent intent = getIntent();
	       itineraryId = intent.getStringExtra("id");
	       mode=intent.getStringExtra("mode");
	       db = DBHelper.getInstance(this).getWritableDatabase();
		   
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
		       mMarker = map.addMarker(new MarkerOptions().position(latlng).title("Here you are"));
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
	    mMarker.remove();
	    mMarker = map.addMarker(new MarkerOptions().position(latlng).title("Here you are"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

	}
	
	@Override
	public void onProviderDisabled(String arg0) {
		//TODO implementa dialog che dice di attivare il gps
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
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.address);
		EditText editText = (EditText) layout.getChildAt(0);
		String message = editText.getText().toString();
		GoogleMapsUtils.getGeoCode(this,message);
	}
	
	private void saveCoordinates(LatLng startCoord){
		String startLat = "" + startCoord.latitude;
		String startLong = "" + startCoord.longitude;
		StartPointDAO.deleteStartPointPOI(db, itineraryId);
		StartPointDAO.insertStartingPoint(db, itineraryId, startLat , startLong);
		
	}

	@Override
	public void onGeoCodeComputed(LatLng result) {
		
		if(result!=null){
			latlng = result;
		}else{
			Toast.makeText(this, R.string.invalidAddr, Toast.LENGTH_SHORT).show();
		}
		
		setUpMapIfIneed();
	    mMarker.remove();
		mMarker = map.addMarker(new MarkerOptions().position(latlng).title("ChosenAddress"));
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
	}
	
	@Override
	public void onBackPressed() {
		if (getIntent().getBooleanExtra(ORIGIN, false)) {
			new AlertDialog.Builder(this)
					.setTitle("Warning!")
					.setMessage(
							"Do you want to cancel the creation of this itinerary?\n"
									+ "If you don't, set the starting point. You will be able to modify your itinerary later.")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									UserInfo.deleteItinerary(db, itineraryId);
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}
	}


}
