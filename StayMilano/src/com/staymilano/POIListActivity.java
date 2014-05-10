package com.staymilano;

import com.staymilano.database.DBHelper;
import com.staymilano.model.City;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class POIListActivity extends Activity {

	static final String AREA_EXTRA="area_extra";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poilist);
		
		SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
		City city=City.getCity(db);
		Bundle extras=getIntent().getExtras();
		
		if (extras != null) {
			if (extras.containsKey(AREA_EXTRA)) {
				String area = (String) extras.getString(AREA_EXTRA);

				ListView listView = (ListView) findViewById(R.id.listViewPOI);
				POICustomAdapter adapter = new POICustomAdapter(this,
						R.layout.row_point, city.getPOIbyArea(area));
				listView.setAdapter(adapter);
			}
		}
	
	}
}
