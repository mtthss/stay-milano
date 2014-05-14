package com.staymilano;

import java.util.List;

import com.staymilano.database.DBHelper;
import com.staymilano.database.ItineraryDAO;
import com.staymilano.model.Itinerary;
import com.staymilano.model.UserInfo;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
	
	private class ItineraryCustomAdapter extends ArrayAdapter<Itinerary>{

		public ItineraryCustomAdapter(Context context, int resource,
				List<Itinerary> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}
		
		public View getView(int position,View convertView,ViewGroup parent){
			LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.rowitinerary, null);
			TextView date=(TextView) convertView.findViewById(R.id.textViewDate);
			Itinerary it=getItem(position);
			date.setText(it.getDate().toString());
			return convertView;
		}

	}

}
