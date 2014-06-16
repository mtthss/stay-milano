package com.staymilano;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.staymilano.database.DBHelper;
import com.staymilano.model.Itinerary;
import com.staymilano.model.UserInfo;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ItineraryListActivity extends ListActivity {
	
	Context ctx;
	List<Itinerary> its;
	
	static final String CURRENT_ITINERARY="current_itinerary";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary_list);
		ctx=this;
	}
	
	@Override
	public void onResume(){
		super.onResume(); 
		
		SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
		UserInfo user = UserInfo.getUserInfo(db);
		its = user.getItineraries();
		
		ItineraryCustomAdapter adapter = new ItineraryCustomAdapter(this,its);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onListItemClick(ListView l,View v,int position,long id){
		Itinerary itinerary = its.get(position);
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("id", itinerary.getID());
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		this.moveTaskToBack(true);
	}
	
	public void createNewIt(View view){
		
		Intent intent = new Intent(ctx, ItineraryCreationActivity.class);
    	startActivity(intent);
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
					.findViewById(R.id.date);
			
			Itinerary it = itineraries.get(position);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String stringDate = sdf.format(it.getDate().getTime());
			date.setText(stringDate);

			return convertView;
		}
		
	}

}
