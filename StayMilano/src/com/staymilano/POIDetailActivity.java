package com.staymilano;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.staymilano.database.DBHelper;
import com.staymilano.database.PointOfInterestDAO;
import com.staymilano.model.PointOfInterest;

public class POIDetailActivity extends Activity {

	static final String AREA_EXTRA="area_extra";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_detail);
		
		SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
		Cursor cur;

		String spoi=getIntent().getStringExtra(ItineraryCreationActivity.POI);
		if(spoi!=null){
			cur=PointOfInterestDAO.getPOIById(db,spoi);
		}else{
			String poi=getIntent().getStringExtra(ItineraryCreationActivity.POI_NAME);
			cur=PointOfInterestDAO.getPOIByName(db,poi);
		}
		PointOfInterest poi=new PointOfInterest(cur);
		
		setTitle(poi.getName());
		
		TextView title=(TextView) findViewById(R.id.title);
		TextView type=(TextView) findViewById(R.id.type);
		TextView description=(TextView) findViewById(R.id.description);
		
		title.setText(poi.getName());
		type.setText(poi.getType());
		description.setText(poi.getDescription());

	}
	
}
