package com.staymilano;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import visualization.MapLook;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ListFragment;
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
import android.widget.ListView;
import communications.*;
import model.Direction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.model.LatLng;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		manager=getSupportFragmentManager();
		
		Intent intent = getIntent();
		today = intent.getBooleanExtra("today", false);
		String itineraryID = intent.getStringExtra(ItineraryListActivity.CURRENT_ITINERARY);
		Itinerary it = null;
		
		if(today){
			SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
			UserInfo ui = UserInfo.getUserInfo(db);
			Date date = new Date();
			it = ui.getItineraryByDate(date);
		}
		else if (itineraryID != null) {
			SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
			UserInfo ui = UserInfo.getUserInfo(db);
			it = ui.getItinerary(itineraryID);
		}
		if (it != null) {
			points = it.getPois();
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
		private List<LatLng> POIsequence;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_main, container,	false);
			POIsequence = Itinerary.coordinatesOfPoiList(points);
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
			
			map.addMarker(new MarkerOptions().position(MILAN).title("Milan").snippet("Ciao"));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
			map.setOnMapLoadedCallback(this);
			map.getUiSettings().setZoomControlsEnabled(false);
		}

		@Override
		public void onMapLoaded() {

			// once map is loaded zoom on the city center
			map.animateCamera(CameraUpdateFactory.zoomTo(12), 1000, null);

			// retrieve directions from google server
			getDirections();
		}

		private void getDirections() {

			// get directions for the specified itinerary
			GoogleMapsUtils.getDirection(this, POIsequence,	GoogleMapsUtils.MODE_WALKING);
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
			adapter = new POIAdapter(getActivity(), points);
			listView.setAdapter(adapter);
		}
	}

}