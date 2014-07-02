package com.staymilano;

import java.util.List;

import model.Stallo;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bikemiutils.BikeMiCallBack;
import com.example.bikemiutils.BikeMiUtils;
import com.google.android.gms.maps.model.LatLng;
import com.staymilano.database.DBHelper;
import com.staymilano.database.PointOfInterestDAO;
import com.staymilano.model.BikeStation;
import com.staymilano.model.PointOfInterest;

public class POIDetailActivity extends Activity implements BikeMiCallBack{

	static final String AREA_EXTRA="area_extra";
	
	public static final String TYPE="type";
	public static final String NAME="name";
	public static final String POSITION = "position";
	
	String name;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_detail);
		
		SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
		Cursor cur;

		String type=getIntent().getStringExtra(TYPE);
		name=getIntent().getStringExtra(NAME);
		Bundle bundle=getIntent().getParcelableExtra("bundle");
		LatLng myposition=bundle.getParcelable(POSITION);
		
		if(type.equals(BikeStation.BIKE_STATION)){
			BikeMiUtils.getBikeMiStations(this, myposition);
		}else{
			cur=PointOfInterestDAO.getPOIByName(db,name);
			PointOfInterest poi=new PointOfInterest(cur);
			
			setTitle(poi.getName());
			
			TextView v_title=(TextView) findViewById(R.id.title);
			TextView v_type=(TextView) findViewById(R.id.type);
			TextView v_description=(TextView) findViewById(R.id.description);
			
			v_title.setText(poi.getName());
			v_type.setText(poi.getType());
			v_description.setText(poi.getDescription());
		}

	}


	@Override
	public void onBikeMiStationsComputed(List<Stallo> result) {

	}
	
}
