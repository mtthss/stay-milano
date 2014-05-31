package com.staymilano;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import visualization.MapLook;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;
import com.staymilano.database.DBHelper;
import com.staymilano.model.Area;
import com.staymilano.model.City;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class AreaSelectionActivity extends Activity implements OnMapLoadedCallback{
	
	private Boolean detail=false;
	
	private GoogleMap map;
	List<MarkerOptions> markers;
	
	ListView listView;
	LinearLayout myGallery;
	//CustomAdapter adapter;
	List<PointOfInterest> selectedPOI=new ArrayList<PointOfInterest>();
	Itinerary it;

	private SQLiteDatabase db=DBHelper.getInstance(this).getWritableDatabase();
	static final LatLng MILAN = new LatLng(45.4773, 9.1815);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_selection);
		
		detail=false;
		//creation of the map object and set up
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setOnMapLoadedCallback(this);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
		map.animateCamera(CameraUpdateFactory.zoomTo((float) 12.5), 1000, null);
		map.setOnMapClickListener(listener);
		//set up list view
		//listView=(ListView)findViewById(R.id.poi_gallery);
		//adapter=new CustomAdapter(this, R.layout.band_layout, selectedPOI);
		//listView.setAdapter(adapter);
		/*listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		myGallery=(LinearLayout)findViewById(R.id.mygallery);
		
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.area_selection, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case R.id.actionok:
			Toast.makeText(this, "ok selected", Toast.LENGTH_SHORT).show();
			it =new Itinerary();
			showDialog(0);
			it.setPois(selectedPOI);
			Intent intent = new Intent(this, StartingPointActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

		return true;
	}
	
	protected Dialog onCreateDialog(int id) {

		return new DatePickerDialog(this,mDateSetListener,Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);
	}

	protected DatePickerDialog.OnDateSetListener mDateSetListener =new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
			Calendar c=Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			it.setData(c);
		}

	};



	@Override
	public void onMapLoaded() {
		MapLook.drawAreas(City.getCity(db).getPolygons(), map);
	}
	
	private final OnMapClickListener listener = new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng point) {
			if (detail) {
				// TODO implement remove poi in MapLook
				// MapLook.removePOI();
				map.clear();
				map.animateCamera(CameraUpdateFactory.zoomTo((float) 12.5), 1000, null);
				MapLook.drawAreas(City.getCity(db).getPolygons(), map);
				detail = false;
			} else {
				List<Area> areas = City.getCity(db).getAllAreas();
				Area a = PointInPolygon(point, areas);
				if (a != null) {
					detail = true;
					markers = new ArrayList<MarkerOptions>();
					for (PointOfInterest poi : a.getPois()) {
						MarkerOptions marker = new MarkerOptions();
						marker.title(poi.getName());
						marker.position(poi.getPosition());
						markers.add(marker);
						
					}
					MapLook.drawPOI(markers, map);
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(
							a.getCenter(), (float) 12.5));
					map.animateCamera(CameraUpdateFactory.zoomTo((float) 15.5), 1000,
							null);
					map.setOnMarkerClickListener(markerListener);
				}else{
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(
							point, (float) 12.5));
				}
			}
		}

		private Area PointInPolygon(LatLng point, List<Area> areas) {
			for(Area a:areas){
				List<LatLng> points = a.getPolygon().getPoints();
				int i, j, nvert = points.size();
				boolean c = false;

				for (i = 0, j = nvert - 1; i < nvert; j = i++) {
					if ((((points.get(i).longitude) >= point.longitude) != (points
							.get(j).longitude >= point.longitude))
							&& (point.latitude <= (points.get(j).latitude - points
									.get(i).latitude)
									* (point.longitude - points.get(i).longitude)
									/ (points.get(j).longitude - points.get(i).longitude)
									+ points.get(i).latitude))
						c = !c;
				}

				if(c)
					return a;
				
			}
			return null;
			
		}
	};
	
	
	private OnMarkerClickListener markerListener=new OnMarkerClickListener() {
		
		@Override
		public boolean onMarkerClick(Marker marker) {
			PointOfInterest poi=new PointOfInterest();
			poi=City.getCity(db).getPOIbyName(marker.getTitle());
			selectedPOI.add(poi);
			if(selectedPOI.size()>0)
				getActionBar().show();
			//TODO notifica l'adapter per l'aggiunta
			//adapter.add(poi);
			//adapter.notifyDataSetChanged();
			
			myGallery.addView(insertPhoto(poi.getName()));
			return false;
		}

	};
	
	/*private class CustomAdapter extends ArrayAdapter<PointOfInterest>{
		
		private Activity context;

		public CustomAdapter(Activity context, int resource,
				List<PointOfInterest> objects) {
			super(context, resource, objects);
			this.context=context;
		}
		

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      View rowView = inflater.inflate(R.layout.band_layout, null);
	      // configure view holder
	      ImageView view = (ImageView) convertView.findViewById(R.id.icon);
	      view.setImageResource(R.drawable.ic_launcher);

	    return rowView;
	  }
		
	}*/
	
	public View insertPhoto(String name) {

		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setLayoutParams(new LayoutParams(220, 220));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setBackgroundColor(1);
		
		layout.addView(imageView);
		return layout;
	}
	
}