package com.staymilano;

import android.app.Activity;
import android.os.Bundle;

public class POIListActivity extends Activity {

	static final String AREA_EXTRA="area_extra";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poilist);
	}
	
	
}
