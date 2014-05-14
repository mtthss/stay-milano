package com.staymilano;

import java.util.ArrayList;
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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.MapFragment;
import com.staymilano.database.DBHelper;
import com.staymilano.model.Area;
import com.staymilano.model.City;
import com.staymilano.model.PointOfInterest;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class AreaSelectionActivity extends FragmentActivity implements OnMapLoadedCallback{
	
	private Boolean detail;
	
	private GoogleMap map;
	List<MarkerOptions> markers;
	
	ListView listView;
	CustomAdapter adapter;
	List<PointOfInterest> selectedPOI=new ArrayList<PointOfInterest>();

	private SQLiteDatabase db=DBHelper.getInstance(this).getWritableDatabase();
	static final LatLng MILAN = new LatLng(45.4773, 9.1815);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_selection);
		//detail mode set to false
		detail=false;
		//creation of the map object and set up
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setOnMapLoadedCallback(this);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
		map.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
		map.setOnMapClickListener(listener);
		//set up list view
		listView=(ListView)findViewById(R.id.selected_poi_listview);
		adapter=new CustomAdapter(this, R.layout.band_layout, selectedPOI);
		listView.setAdapter(adapter);
	}


	@Override
	public void onMapLoaded() {
		MapLook.drawAreas(City.getCity(db).getPolygons(), map);

	}
	
	private final OnMapClickListener listener = new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng point) {
			if (detail) {

				//TODO implement remove poi in MapLook

				//MapLook.removePOI();
				map.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
				MapLook.drawAreas(City.getCity(db).getPolygons(), map);

				detail = false;
			} else {
				detail = true;
				List<Area> areas = City.getCity(db).getAllAreas();
				for (Area a : areas) {
					if (PointInPolygon(point, a.getPolygon())) {
						markers = new ArrayList<MarkerOptions>();
						for (PointOfInterest poi : a.getPois()) {
							MarkerOptions marker = new MarkerOptions();
							marker.title(poi.getName());
							marker.position(poi.getPosition());
							markers.add(marker);
						}
						MapLook.drawPOI(markers, map);
						//TODO gestire centro della zona
						//map.moveCamera(CameraUpdateFactory.newLatLngZoom(a.getCenter(), 10));
						map.animateCamera(CameraUpdateFactory.zoomTo(15), 1000,
								null);
						map.setOnMarkerClickListener(markerListener);

					}
				}

			}

		}

		private boolean PointInPolygon(LatLng point, PolygonOptions polygon) {
			List<LatLng> points = polygon.getPoints();
			int i, j, nvert = points.size();
			boolean c = false;

			for (i = 0, j = nvert - 1; i < nvert; j = i++) {
				if ((((points.get(i).latitude) >= point.longitude) != (points
						.get(j).longitude >= point.longitude))
						&& (point.latitude <= (points.get(j).latitude - points
								.get(i).latitude)
								* (point.longitude - points.get(i).longitude)
								/ (points.get(j).longitude - points.get(i).longitude)
								+ points.get(i).latitude))
					c = !c;
			}

			return c;
		}
	};
	
	
	private OnMarkerClickListener markerListener=new OnMarkerClickListener() {
		
		@Override
		public boolean onMarkerClick(Marker marker) {
			PointOfInterest poi=new PointOfInterest();
			poi=City.getCity(db).getPOIbyName(marker.getTitle());
			selectedPOI.add(poi);
			//TODO notifica l'adapter per l'aggiunta
			adapter.add(poi);
			adapter.notifyDataSetChanged();
			return false;
		}
	};
	
	private class CustomAdapter extends ArrayAdapter<PointOfInterest>{
		
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
		
	}
	
}