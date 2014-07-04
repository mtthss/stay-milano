package com.staymilano;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.staymilano.database.DBHelper;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;
import com.staymilano.model.UserInfo;

public class ItineraryListActivity extends ActionBarActivity {

	static Context ctx;
	static List<Itinerary> its;
	static SQLiteDatabase db;

	static final String CURRENT_ITINERARY = "current_itinerary";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary_list);
		db = DBHelper.getInstance(this).getWritableDatabase();
		ctx = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.show_itineraries, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			createNewIt();
			break;
		default:
			break;
		}

		return true;
	}

	public void createNewIt() {

		Intent intent = new Intent(ctx, ItineraryCreationActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
		Intent intent = new Intent(this, SplashActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);
		startActivity(intent);
	}

	public static class ItineraryListFragment extends ListFragment {
		
		ActionMode.Callback callback;
		ActionMode mode;
		
		ItineraryCustomAdapter adapter;
		int toDelete;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.fragment_itinerary_list, container);
			
			return view;
		}

		@Override
		public void onResume() {
			super.onResume();
			
			db = DBHelper.getInstance(getActivity()).getWritableDatabase();
			UserInfo user = UserInfo.getUserInfo(db);
			its = user.getItineraries();

			adapter = new ItineraryCustomAdapter(ctx,
					its);
			setListAdapter(adapter);
			adapter.notifyDataSetChanged();
			
			callback = new ActionMode.Callback() {

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;

				}

				@Override
				public void onDestroyActionMode(ActionMode mode) {
					mode = null;
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.poilist, menu);
					return true;
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
					switch (item.getItemId()) {
					case R.id.action_delete:
						UserInfo.deleteItinerary(db, adapter.getItem(toDelete).getID());
						adapter.remove(adapter.getItem(toDelete));;
						mode.finish(); // Action picked, so close the CAB
						return true;
					default:
						return false;
					}
				}
			};
			
			getListView().setOnItemLongClickListener(
					new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, int position, long id) {
							if (mode != null){
								toDelete=position;
								return false;
							}else{
								mode = getActivity().startActionMode(callback);
								toDelete=position;
							}return true;
						}
					});

		}
		

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Itinerary itinerary = its.get(position);
			Intent intent = new Intent(ctx, MainActivity.class);
			intent.putExtra(ItineraryCreationActivity.ITINERARY_ID, itinerary.getID());
			startActivity(intent);
		}

		public class ItineraryCustomAdapter extends ArrayAdapter<Itinerary> {

			private final Context context;
			private final List<Itinerary> itineraries;

			public ItineraryCustomAdapter(Context context,
					List<Itinerary> objects) {
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
				TextView title = (TextView) convertView.findViewById(R.id.date);
				TextView description= (TextView) convertView.findViewById(R.id.description);

				Itinerary it = itineraries.get(position);
				PointOfInterest p=it.getPois().get(0);
				
				String iconName=p.getName().replace(" ", "_");
				String iconName2=iconName.replace(")", "");
				String iconName3=iconName2.replace("(", "");
				int icon=getResources().getIdentifier("com.staymilano:drawable/"+iconName3.toLowerCase(), null, null);
				imageView.setImageResource(icon);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String stringDate = sdf.format(it.getDate().getTime());
				title.setText(stringDate);
				String subs = null;
				for(PointOfInterest poi:it.getPois()){
					if(subs==null){
						subs=poi.getName();
					}else{
						subs=subs+","+poi.getName();
					}
				}
				description.setText(subs);

				return convertView;
			}

		}
	}


}
