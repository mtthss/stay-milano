package com.staymilano;

import com.staymilano.database.DBHelper;
import com.staymilano.database.PointOfInterestDAO;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class POIDetail extends Activity {

	static final String AREA_EXTRA="area_extra";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_detail);
		
		SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
		String spoi=getIntent().getStringExtra(ItineraryCreationActivity.POI);
		Cursor cur=PointOfInterestDAO.getPOIById(db,spoi);
		PointOfInterest poi=new PointOfInterest(cur);
		
		TextView title=(TextView) findViewById(R.id.title);
		TextView type=(TextView) findViewById(R.id.type);
		ImageView image=(ImageView) findViewById(R.id.image);
		TextView description=(TextView) findViewById(R.id.description);
		
		title.setText(poi.getName());
		type.setText(poi.getType());
		description.setText(poi.getDescription());

	}
	
}
