package com.staymilano;

import java.util.List;

import model.Stallo;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bikemiutils.BikeMiCallBack;
import com.example.bikemiutils.BikeMiUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.staymilano.database.DBHelper;
import com.staymilano.database.PointOfInterestDAO;
import com.staymilano.model.BikeStation;
import com.staymilano.model.PointOfInterest;

public class POIDetailActivity extends Activity implements BikeMiCallBack{

	static final String AREA_EXTRA="area_extra";
	
	public static final String TYPE="type";
	public static final String NAME="name";
	public static final String POSITION_LAT = "position_lat";
	public static final String POSITION_LNG = "position_lng";
	
	String name;
	private GoogleMap mMap;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_detail);
		
		setUpMapIfIneed();
		
		SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
		Cursor cur;

		String type=getIntent().getStringExtra(TYPE);
		name=getIntent().getStringExtra(NAME);
		Double lat=getIntent().getDoubleExtra(POSITION_LAT, 0);
		Double lng=getIntent().getDoubleExtra(POSITION_LNG, 0);
		
		LatLng myposition=new LatLng(lat, lng);
		
	    Marker mMarker = mMap.addMarker(new MarkerOptions().position(myposition).title("name"));
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myposition, 15));
		
		setTitle(name);
		TextView v_title=(TextView) findViewById(R.id.title);
		v_title.setText(name);
		
		if(type.equals(BikeStation.BIKE_STATION)){
			BikeMiUtils.getBikeMiStations(this, myposition);
		}else{
			cur=PointOfInterestDAO.getPOIByName(db,name);
			PointOfInterest poi=new PointOfInterest(cur);
			
			TextView v_type=(TextView) findViewById(R.id.type);
			TextView v_description=(TextView) findViewById(R.id.description);
			ImageView image=(ImageView) findViewById(R.id.image);
			
			String iconName=name.replace(" ", "_");
			int icon=getResources().getIdentifier("com.staymilano:drawable/"+iconName.toLowerCase(), null, null);
			v_type.setText(poi.getType());
			v_description.setText(poi.getDescription());
			image.setImageResource(icon);
		}

	}


	private void setUpMapIfIneed() {
		
		if (mMap == null) {
			MapFragment smf = (MapFragment) getFragmentManager().findFragmentById(R.id.mapMain);
			mMap = smf.getMap();
		}
	}
	
	
	@Override
	public void onBikeMiStationsComputed(List<Stallo> result) {
		BikeStation bike = new BikeStation();
		bike.setName(name);
		bike.getPosition();
		
		String description="not available";
		
		for(Stallo st:result){
			if(st.getName().equals(name)){
				description=st.getEmptySlots()+" empty slots \n"+st.getFreeBikes()+" free bikes";
			}
		}
		
		TextView v_type=(TextView) findViewById(R.id.type);
		TextView v_description=(TextView) findViewById(R.id.description);
		ImageView image=(ImageView) findViewById(R.id.image);
		
		v_type.setText(bike.getType());
		v_description.setText("This Bike Station now has:\n"+description);
		image.setImageDrawable(getResources().getDrawable(R.drawable.markerbikepurple));
	}
	
}
