package com.staymilano;

import java.util.ArrayList;
import java.util.List;

import visualization.MapLook;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;
import communications.*;
import model.Direction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.staymilano.AreaSelectionActivity.AppSectionsPagerAdapter;
import com.staymilano.database.DBHelper;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;
import com.staymilano.model.UserInfo;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	ViewPager mViewPager;
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	static List<PointOfInterest> points = new ArrayList<PointOfInterest>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		String itineraryID = intent
				.getStringExtra(ItineraryListActivity.CURRENT_ITINERARY);
		if (itineraryID != null) {
			SQLiteDatabase db = DBHelper.getInstance(this)
					.getWritableDatabase();
			UserInfo ui = UserInfo.getUserInfo(db);
			Itinerary it = ui.getItinerary(itineraryID);
			if (it != null) {
				points = it.getPois();
			}
		}

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

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
				Fragment fragment = new ListFragment();
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

	public static class MapSectionFragment extends Fragment implements
			OnMapLoadedCallback, CallBack {

		GoogleMap map;
		static final LatLng MILAN = new LatLng(45.4773, 9.1815);
		private List<LatLng> POIsequence;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			setUpMapIfIneed();
			return rootView;
		}

		private void setUpMapIfIneed() {
			if (map == null) {
				map = ((SupportMapFragment) AreaSelectionActivity.manager
						.findFragmentById(R.id.map)).getMap();
			}
			if (map != null) {
				setupMap();
			}
		}

		private void setupMap() {
			map.addMarker(new MarkerOptions().position(MILAN).title("Milan")
					.snippet("Ciao"));
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

			// fetch ordered sequence of POI from database
			LatLng start = new LatLng(45.56, 9.3);
			LatLng intermediate = new LatLng(45.45, 9.21);
			LatLng end = new LatLng(45.4773, 9.1815);
			POIsequence = new ArrayList<LatLng>();
			POIsequence.add(start);
			POIsequence.add(intermediate);
			POIsequence.add(end);

			// get directions for the specified itinerary
			GoogleMapsUtils.getDirection(this, POIsequence,
					GoogleMapsUtils.MODE_WALKING);
		}

		@Override
		public void onDirectionLoaded(List<Direction> directions) {

			// once the google server provides the directions, draw them on the
			// map
			MapLook.drawDirection(directions, map);
		}

	}

	public static class ListFragment extends Fragment {

		MyPOIAdapter adapter;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View rootView = inflater.inflate(R.layout.fragment_poilist,
					container, false);
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			ListView listView = (ListView) getActivity().findViewById(
					R.id.listViewPOI);
			View empty = getActivity().findViewById(R.id.emptyList);
			listView.setEmptyView(empty);
			adapter = new MyPOIAdapter(getActivity(), points);
			listView.setAdapter(adapter);
		}

	}

}