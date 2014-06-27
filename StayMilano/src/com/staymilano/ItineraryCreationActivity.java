package com.staymilano;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

import visualization.MapLook;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.staymilano.database.DBHelper;
import com.staymilano.database.ItineraryDAO;
import com.staymilano.model.Area;
import com.staymilano.model.City;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;
import com.staymilano.model.UserInfo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.GetChars;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItineraryCreationActivity extends FragmentActivity implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    
    public static FragmentManager manager;
    public static Context ctx;
    public static POIAdapter adapter;
    
	static ArrayList<Marker> markers = new ArrayList<Marker>();
    static List<PointOfInterest> selectedPOI=new ArrayList<PointOfInterest>();
	Itinerary it;

	static Intent intent;
	
	public static final String POI = "poi";
	public static final String POI_NAME="poi_name";
	public static final String ITINERARY_ID="id";
	static final LatLng MILAN = new LatLng(45.4773, 9.1815);
	
	static boolean MODIFICATION; 

	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary_creation);

		manager = getSupportFragmentManager();
		ctx = ItineraryCreationActivity.this;

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		//TODO

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

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
	
	
	public static void saveItinerary(String s){
		SQLiteDatabase db = DBHelper.getInstance(ItineraryCreationActivity.ctx)
				.getWritableDatabase();

		UserInfo ui = UserInfo.getUserInfo(db);
		Itinerary it = new Itinerary();
		// Set point of interests
		it.setPois(selectedPOI);
		
		// Set date
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			cal.setTime(sdf.parse(s));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		it.setData(cal);
		
		// Start next activity
		if (ItineraryCreationActivity.MODIFICATION) {
			// Update itinerary in database
			String id= ui.updateItinerary(it,db);
			Intent intent = new Intent(ctx,
					MainActivity.class);
			intent.putExtra("id", id);
			ctx.startActivity(intent);
			((Activity) ctx).finish();
		} else {
			// Save itinerary in database
			String id = ui.saveItinerary(it, db);
			Intent intent = new Intent(ctx,
					StartingPointActivity.class);
			intent.putExtra("id", id);
			ctx.startActivity(intent);
			((Activity) ctx).finish();
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public List<PointOfInterest> getPoiList() {
		return selectedPOI;
	}
	
	public static void removePoi(String name){
		for(Marker m:markers){
			if(m.getTitle().equals(name)){
				m.setIcon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_launcher));
			}
		}
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				MapPOIFragment mf = new MapPOIFragment();
				return mf;

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
			if (position == 0) {
				return "map";
			} else {
				return "list";
			}

		}
	}
	
	////////////////////////////////////////FRAGMENT MAP//////////////////////////////////////////////////

	public static class MapPOIFragment extends Fragment implements
			OnMapLoadedCallback {

		GoogleMap map;
		SQLiteDatabase db;
		Area lastAreaClicked;
		Marker lastMarkerClicked;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_map, container,
					false);

			db = DBHelper.getInstance(ItineraryCreationActivity.ctx)
					.getWritableDatabase();

			intent = getActivity().getIntent();
			String itinerary_id = intent.getStringExtra(ITINERARY_ID);
			if (itinerary_id != null) {
				MODIFICATION = true;
				UserInfo ui = UserInfo.getUserInfo(db);
				Itinerary it = ui.getItinerary(itinerary_id);
				if (it != null) {
					selectedPOI = it.getPois();
				}
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}
			} else {
				MODIFICATION = false;
			}

			setUpMapIfIneed();
			return rootView;
		}

		private void setUpMapIfIneed() {
			if (map == null) {
				map = ((SupportMapFragment) ItineraryCreationActivity.manager
						.findFragmentById(R.id.map)).getMap();
			}
			if (map != null) {
				setupMap();
			}
		}

		private void setupMap() {
			map.setOnMapLoadedCallback(this);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
			map.animateCamera(CameraUpdateFactory.zoomTo((float) 12.5), 1000,
					null);
			map.setOnMapClickListener(listener);
			map.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker marker) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {
					View view = getLayoutInflater(getArguments()).inflate(
							R.layout.infowindow, null);
					TextView title = (TextView) view
							.findViewById(R.id.textView1);
					title.setText(marker.getTitle());
					return view;
				}
			});
		}

		public void onMapLoaded() {
			MapLook.drawAreas(City.getCity(db).getPolygons(), map);
		}
		

		private final OnMapClickListener listener = new OnMapClickListener() {
			CameraUpdate camup;

			@Override
			public void onMapClick(LatLng point) {
				List<Area> areas = City.getCity(db).getAllAreas();
				Area a2 = PointInPolygon(point, areas);

				if (a2 != null && lastAreaClicked == null) {
					exploreArea(a2);
					lastAreaClicked = a2;
				} else if (a2 != null && !lastAreaClicked.getName().equals(a2.getName())) {
					deleteMarkers(lastAreaClicked);
					exploreArea(a2);
					lastAreaClicked = a2;
				} else {
					deleteMarkers(lastAreaClicked);
					camup = CameraUpdateFactory.newLatLngZoom(MILAN,
							(float) 12.5);
					map.animateCamera(camup, 1000, null);
					lastAreaClicked=null;
				}
			}

			private void exploreArea(Area a) {
				for (PointOfInterest poi : a.getPois()) {
					MarkerOptions marker = new MarkerOptions();
					marker.title(poi.getName());
					marker.position(poi.getPosition());
					List<PointOfInterest> prova=selectedPOI;
					if (checkIfSelected(poi)) {
						marker.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.check));
					} else {
						marker.icon(BitmapDescriptorFactory
								.fromResource(poi.getIcon()));
					}
					markers.add(map.addMarker(marker));
				}
				camup = CameraUpdateFactory.newLatLngZoom(a.getCenter(), 15f);
				map.animateCamera(camup, 1000, null);
				map.setOnMarkerClickListener(markerListener);
				map.setOnInfoWindowClickListener(infowinlistener);
			}
			
			private void deleteMarkers(Area a) {
				for(Marker marker:markers){
					for(PointOfInterest poi:a.getPois()){
						if(poi.getName().equals(marker.getTitle())){
							if(!checkIfSelected(poi)){
								marker.remove();
							}
						}
					}
				}
			}

			private boolean checkIfSelected(PointOfInterest poi){
				for(PointOfInterest spoi:selectedPOI){
					if(spoi.getName().equals(poi.getName())){
						return true;
					}
				}
				return false;
			}

			private Area PointInPolygon(LatLng point, List<Area> areas) {
				for (Area a : areas) {
					List<LatLng> points = a.getPolygon().getPoints();
					int i, j, nvert = points.size();
					boolean c = false;

					for (i = 0, j = nvert - 1; i < nvert; j = i++) {
						if ((((points.get(i).longitude) >= point.longitude) != (points
								.get(j).longitude >= point.longitude))
								&& (point.latitude <= (points.get(j).latitude - points
										.get(i).latitude)
										* (point.longitude - points.get(i).longitude)
										/ (points.get(j).longitude - points
												.get(i).longitude)
										+ points.get(i).latitude))
							c = !c;
					}

					if (c)
						return a;

				}
				return null;

			}
		};

		private OnMarkerClickListener markerListener = new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				PointOfInterest poi = new PointOfInterest();
				poi = City.getCity(db).getPOIbyName(marker.getTitle());
				if (selectedPOI.contains(poi)) {
					selectedPOI.remove(poi);
					marker.setIcon(BitmapDescriptorFactory
							.fromResource(poi.getIcon()));
					adapter.notifyDataSetChanged();
				} else {
					selectedPOI.add(poi);
					marker.setIcon(BitmapDescriptorFactory
							.fromResource(R.drawable.check));
					adapter.notifyDataSetChanged();

				}
				return false;
			}

		};
		
		private OnInfoWindowClickListener infowinlistener = new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				if (lastMarkerClicked != null && marker.equals(lastMarkerClicked)) {
					marker.hideInfoWindow();
				} else {
					lastMarkerClicked = marker;
					intent = new Intent(ctx, POIDetailActivity.class);
					intent.putExtra(ItineraryCreationActivity.POI_NAME,
							marker.getTitle());
					startActivity(intent);
				}
			}
		};

	}
	
	////////////////////////////////////LIST FRAGMENT///////////////////////////////////////////

	public static class ListFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_poilist,
					container, false);
			return rootView;
		}
		
		@Override
		public void onResume() {
			super.onResume();
			selectedPOI.clear();
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			ListView listView = (ListView) getActivity().findViewById(
					R.id.listViewPOI);
			View empty = getActivity().findViewById(R.id.emptyList);
			listView.setEmptyView(empty);
			Button button = (Button) getActivity().findViewById(R.id.saveMap);
			button.setText(R.string.save);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showDatePickerDialog(v);
					
				}
			});
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					intent = new Intent(ctx, POIDetailActivity.class);
					intent.putExtra(ItineraryCreationActivity.POI, selectedPOI
							.get(position).getId());
					startActivity(intent);

				}
			});
			adapter = new POIAdapter(getActivity(), selectedPOI);
			listView.setAdapter(adapter);

		}
		
		public void showDatePickerDialog(View v) {
			if (selectedPOI.size() > 0) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getActivity().getFragmentManager(), "datePicker");
				newFragment.setRetainInstance(true);
			} else {
				Toast.makeText(ctx, R.string.error, Toast.LENGTH_SHORT).show();
			}
		}
		
		


	}

}
