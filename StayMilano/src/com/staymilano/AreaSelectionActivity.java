package com.staymilano;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import visualization.MapLook;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.MapFragment;
import com.staymilano.database.DBHelper;
import com.staymilano.model.Area;
import com.staymilano.model.AreasName;
import com.staymilano.model.City;
import com.staymilano.model.PointOfInterest;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


public class AreaSelectionActivity extends Activity implements OnMapLoadedCallback{
	
	
	/*private Button mButton1;
	private Button mButton2;
	private Button mButton3;
	private Button mButton4;
	private Button mButton5;
	private Button mButton6;*/
	
	//private Intent intent;
	
	private Boolean detail;
	
	private GoogleMap map;
	List<Marker> markers;

	private SQLiteDatabase db=DBHelper.getInstance(this).getWritableDatabase();
	static final LatLng MILAN = new LatLng(45.4773, 9.1815);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_selection);
		detail=false;
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setOnMapLoadedCallback(this);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
		map.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
		map.setOnMapClickListener(listener);

	/*	intent = new Intent(this, POIListActivity.class);
		mButton1 = (Button) findViewById(R.id.button);
		mButton2 = (Button) findViewById(R.id.button2);
		mButton3 = (Button) findViewById(R.id.button3);
		mButton4 = (Button) findViewById(R.id.button4);
		mButton5 = (Button) findViewById(R.id.button5);
		mButton6 = (Button) findViewById(R.id.button6);
		mButton1.setOnClickListener(mOnClickListener);
		mButton2.setOnClickListener(mOnClickListener);
		mButton3.setOnClickListener(mOnClickListener);
		mButton4.setOnClickListener(mOnClickListener);
		mButton5.setOnClickListener(mOnClickListener);
		mButton6.setOnClickListener(mOnClickListener);*/

	}


	@Override
	public void onMapLoaded() {
		MapLook.drawAreas(City.getCity(db).getPolygons(), map);

	}
	
	private final OnMapClickListener listener = new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng point) {
			if (detail) {
				MapLook.removePOI();
				map.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);
				MapLook.drawAreas(City.getCity(db).getPolygons(), map);

				detail = false;
			} else {
				detail = true;
				List<Area> areas = City.getCity(db).getAllAreas();
				for (Area a : areas) {
					if (PointInPolygon(point, a.getPolygon())) {
						map.moveCamera(CameraUpdateFactory.newLatLngZoom(
								a.getCenter(), 10));
						map.animateCamera(CameraUpdateFactory.zoomTo(15), 1000,
								null);
						markers=MapLook.drawPOI(a.getPois());
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
			// TODO Auto-generated method stub
			return false;
		}
	};
	
	/*private final OnClickListener mOnClickListener=new OnClickListener() {
		

		@Override
		public void onClick(View v) {
			String mArea;
			final int buttonId=v.getId();
			switch (buttonId) {
			case R.id.button:
				mArea=AreasName.DUOMO.toString();
				break;
			case R.id.button2:
				mArea = AreasName.CASTELLO.toString();
				break;
			case R.id.button3:
				mArea = AreasName.PORTATICINESE.toString();
				break;
			case R.id.button4:
				mArea = AreasName.PORTAROMANA.toString();
				break;
			case R.id.button5:
				mArea = AreasName.PALESTRO.toString();
				break;
			default:
				mArea = AreasName.EXPO15.toString();
				break;
			}
			
			intent.putExtra(POIListActivity.AREA_EXTRA, mArea);
			startActivity(intent);
		}
	};*/
	


}