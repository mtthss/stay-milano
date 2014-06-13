package com.staymilano;

import com.staymilano.model.PointOfInterest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class POIDetail extends Activity {

	static final String AREA_EXTRA="area_extra";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_detail);
		
		PointOfInterest poi=(PointOfInterest) getIntent().getSerializableExtra(ItineraryCreationActivity.POI);
		
		TextView title=(TextView) findViewById(R.id.title);
		TextView type=(TextView) findViewById(R.id.type);
		ImageView image=(ImageView) findViewById(R.id.image);
		TextView description=(TextView) findViewById(R.id.description);
		
		title.setText(poi.getName());
		type.setText(poi.getType());
		description.setText(poi.getDescription());

	}
	
}
