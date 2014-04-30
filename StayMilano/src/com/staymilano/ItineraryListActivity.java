package com.staymilano;

import com.staymilano.adapter.ItineraryCustomAdapter;
import com.staymilano.database.DBHelper;
import com.staymilano.database.ItineraryDAO;
import com.staymilano.model.UserInfo;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

public class ItineraryListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary_list);
		
		UserInfo user=new UserInfo();
		user.setItineraries(DBHelper.getInstance(this).getReadableDatabase());
		
		ListView listView=(ListView)findViewById(R.id.listViewItinerary);
		ItineraryCustomAdapter adapter=new ItineraryCustomAdapter(this, R.layout.rowitinerary, user.getItineraries());
		listView.setAdapter(adapter);
	}

}
