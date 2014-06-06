package com.staymilano;

import java.util.List;

import com.staymilano.database.DBHelper;
import com.staymilano.database.ItineraryDAO;
import com.staymilano.model.Itinerary;
import com.staymilano.model.UserInfo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ItineraryListActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_itinerary_list);
		
		SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
		UserInfo user=UserInfo.getUserInfo(db);
		List<Itinerary> its=user.getItineraries();
		
		ListView listView = (ListView) findViewById(R.id.list);
		ItineraryCustomAdapter adapter = new ItineraryCustomAdapter(this,its);
		listView.setAdapter(adapter);
	}

	private class ItineraryCustomAdapter extends ArrayAdapter<Itinerary> {

		private final Context context;
		private final List<Itinerary> itineraries;

		public ItineraryCustomAdapter(Context context, List<Itinerary> objects) {
			super(context, R.layout.rowitinerary, objects);
			this.context = context;
			this.itineraries = objects;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.rowitinerary, null);

			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.icon);
			TextView date = (TextView) convertView
					.findViewById(R.id.textViewDate);
			Itinerary it = itineraries.get(position);
			date.setText(it.getDate().toString());

			return convertView;
		}

	}

}
