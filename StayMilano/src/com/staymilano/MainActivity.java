package com.staymilano;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import visualization.MapLook;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import communications.*;
import model.Direction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.staymilano.database.DBHelper;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;
import com.staymilano.model.UserInfo;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	public static FragmentManager manager;
	public static boolean today;
	ViewPager mViewPager;
	AppSectionsPagerAdapter mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
	static List<PointOfInterest> points = new ArrayList<PointOfInterest>();
	static LatLng startLatLng;
	static Intent intent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		manager=getSupportFragmentManager();
		
		Intent intent = getIntent();
		today = intent.getBooleanExtra("today", false);
		String itineraryID = intent.getStringExtra("id");
		
		Itinerary it = null;
		
		if(today){
			SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
			UserInfo ui = UserInfo.getUserInfo(db);
			Date date = new Date();
			it = ui.getItineraryByDate(date);
		}
		else if (itineraryID != null) {
			SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
			UserInfo ui = UserInfo.getRefreshedUserInfo(db);
			it = ui.getItinerary(itineraryID);
		}
		if (it != null) {
			points = it.getPois();
			startLatLng = it.getStart();
			if(startLatLng!=null){
				PointOfInterest startPOI = new PointOfInterest();
				startPOI.setName("start");
				startPOI.setPosition(Double.toString(startLatLng.latitude), Double.toString(startLatLng.longitude));
				points.add(0, startPOI);
			}
		}
		
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);              
        actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					  @Override
					  public void onPageSelected(int position) {
						  actionBar.setSelectedNavigationItem(position);
					  }
				   });

		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					 .setText(mAppSectionsPagerAdapter.getPageTitle(i))
					 .setTabListener(this));
		}

	}

	@Override
	public void onBackPressed() {
		Intent intent=new Intent(this, ItineraryListActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		//do nothing

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		//do nothing

	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			
			switch (i) {
			case 0:
				return new MapSectionFragment();

			default:
				Fragment fragment = new MyListFragment();
				return fragment;
			}
		}

		@Override
		public int getCount() {
			
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			
			return "Section " + (position + 1);
		}
	}

	public static class MapSectionFragment extends Fragment implements OnMapLoadedCallback, CallBack {

    	SQLiteDatabase db;
		GoogleMap map;
		static final LatLng MILAN = new LatLng(45.4773, 9.1815);

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_main, container,	false);
			db = DBHelper.getInstance(ItineraryCreationActivity.ctx).getWritableDatabase();
			
			setUpMapIfIneed();
			return rootView;
		}

		private void setUpMapIfIneed() {
			
			if (map == null) {
				SupportMapFragment smf = (SupportMapFragment) MainActivity.manager.findFragmentById(R.id.mapMain); 
				map = smf.getMap();
			}
			if (map != null) {
				setupMap();
			}
		}

		private void setupMap() {
			
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
			map.setOnMapLoadedCallback(this);
			map.getUiSettings().setZoomControlsEnabled(false);
		}

		@Override
		public void onMapLoaded() {
			LatLngBounds itBounds = null;
			if(points.get(1).getPosition().latitude<points.get(0).getPosition().latitude){
				itBounds = new LatLngBounds(points.get(1).getPosition(), points.get(0).getPosition());
			}else{
				itBounds = new LatLngBounds(points.get(0).getPosition(), points.get(1).getPosition());
			}
			for(PointOfInterest p : points){
				itBounds.including(p.getPosition());
			}
			// Set the camera to the greatest possible zoom level that includes the bounds
			map.animateCamera(CameraUpdateFactory.newLatLngBounds(itBounds, 10));
			// retrieve directions from google server
			getDirections();		
		}

		private void getDirections() {
			
			// get directions for the specified itinerary
			List<LatLng> poiSequence = Itinerary.coordinatesOfPoiList(points);
			GoogleMapsUtils.getDirection(this, poiSequence,	GoogleMapsUtils.MODE_WALKING);
			
			// draw pois
			List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
			for (PointOfInterest poi : points) {
				MarkerOptions marker = new MarkerOptions();
				marker.title(poi.getName());
				marker.position(poi.getPosition());
				if(points.indexOf(poi)==0){
					marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_mini));
				}else{
					marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.select_mini));
				}
				markers.add(marker);
			}
			MapLook.drawSelectedPois(markers, map);	
		}

		@Override
		public void onDirectionLoaded(List<Direction> directions) {

			// once the google server provides the directions, draw them on the map
			MapLook.drawDirection(directions, map);
		}

	}

	public static class MyListFragment extends Fragment {

		POIAdapter adapter;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_poilist,
					container, false);
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			ListView listView = (ListView) getActivity().findViewById(R.id.listViewPOI);
			View empty = getActivity().findViewById(R.id.emptyList);
			listView.setEmptyView(empty);
			Button button=(Button) getActivity().findViewById(R.id.saveMap);
			button.setText(R.string.modify);
			
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					intent=new Intent(getActivity(), POIDetail.class);
					intent.putExtra(ItineraryCreationActivity.POI, points.get(position).getId());
					startActivity(intent);
					
					
				}
			});
			adapter = new POIAdapter(getActivity(), points);
			listView.setAdapter(adapter);
		}
	}

}