package com.staymilano;

import com.staymilano.database.ItineraryAdapter;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;

public class ItineraryListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary_list);
		
		mCursor= ItineraryAdapter.getAllItinerary(db);
	}

}
