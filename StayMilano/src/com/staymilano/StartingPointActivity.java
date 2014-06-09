package com.staymilano;

import com.google.android.gms.maps.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class StartingPointActivity extends ActionBarActivity implements LocationListener{

	  LocationManager lm;
	  TextView lt, ln;
	  String provider;
	  Location l;
	  LatLng latlng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.activity_starting_point);
		   ln=(TextView)findViewById(R.id.lng);
		   lt=(TextView)findViewById(R.id.lat);
		   //get location service
		   lm=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		   Criteria c=new Criteria();
		   //criteria object will select best service based on Accuracy, power consumption, response, 
		   //bearing and monetary cost, set false to use best service 
		   provider=lm.getBestProvider(c, false);
		   l=lm.getLastKnownLocation(provider);
		   if(l!=null)
		   {
		       //get latitude and longitude of the location
		       double lng=l.getLongitude();
		       double lat=l.getLatitude();
		       latlng = new LatLng(lat, lng);
		       //display on text view
		       ln.setText(""+lng);
		       lt.setText(""+lat);
		   }
		   else
		   {
		      ln.setText("No Provider");
		      lt.setText("No Provider");
		   }
	}

	@Override
	public void onLocationChanged(Location arg0)
	{
		double lng=l.getLongitude();
	    double lat=l.getLatitude();
	    latlng = new LatLng(lat, lng);
	    ln.setText(""+lng);
	    lt.setText(""+lat);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.starting_point, menu);
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

	@Override
	public void onProviderDisabled(String arg0) {
		//do nothing
	}
	
	@Override
	public void onProviderEnabled(String arg0) {
		//do nothing
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		//do nothing
	}
	
	public void takeCurrent(View view){
		
		//TODO implementare salvataggio della variabile latlng in database e poi chiamare mainActivity
	}
	
	public void setChosenAddress(View view){
		
		//TODO implementare chiamata a GeoCode e salvataggio del risultato in database e poi chiamare mainActivity
	}
	

}
