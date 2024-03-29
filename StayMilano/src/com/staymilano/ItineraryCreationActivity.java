package com.staymilano;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import visualization.MapLook;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.staymilano.database.DBHelper;
import com.staymilano.model.Area;
import com.staymilano.model.City;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;
import com.staymilano.model.PointOfItinerary;
import com.staymilano.model.UserInfo;

public class ItineraryCreationActivity extends FragmentActivity implements ActionBar.TabListener, DatePickerDialog.OnDateSetListener  {

    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private ViewPager mViewPager;
        
    public static POIAdapter adapter;
    
	static ArrayList<Marker> markers = new ArrayList<Marker>();
    static List<PointOfItinerary> selectedPOI=new ArrayList<PointOfItinerary>();
	static Itinerary it=new Itinerary();

	static Intent intent;
	static SQLiteDatabase db;
	
	public static final String POI = "poi";
	public static final String POI_NAME="poi_name";
	public static final String ITINERARY_ID="id";
	public static final boolean FROM_CREATION=true;
	static final LatLng MILAN = new LatLng(45.4773, 9.1815);
	
	static boolean MODIFICATION; 
	
	int ICONS[]= new int[]{R.drawable.ic_action_map,R.drawable.ic_action_view_as_list};	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary_creation);
		
		db= DBHelper.getInstance(this).getWritableDatabase();
		List<PointOfItinerary> list=new ArrayList<PointOfItinerary>();
		list.addAll(selectedPOI);
		adapter=new POIAdapter(this, selectedPOI);
		
		intent =getIntent();
		String itinerary_id = intent.getStringExtra(ITINERARY_ID);
		if (itinerary_id != null) {
			MODIFICATION = true;
			UserInfo ui = UserInfo.getUserInfo(db);
			it = ui.getItinerary(itinerary_id);
			if (it != null) {
				selectedPOI.clear();
				selectedPOI.addAll(it.getPois());
				adapter.addAll(it.getPois());
			}
		} else {
			MODIFICATION = false;
			selectedPOI.clear();
			adapter.clear();
		}
		
		adapter.notifyDataSetChanged();

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());


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
					.setIcon(getResources().getDrawable(ICONS[i]))
					.setTabListener(this));
		}
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		int correctMonth = monthOfYear+1;
		String s = dayOfMonth + "-" + correctMonth + "-" + year;
		saveItinerary(s);
	
	}
	
	public void saveItinerary(String s){
		UserInfo ui = UserInfo.getUserInfo(db);
		// Set point of interests
		Itinerary newIt=new Itinerary();
		if (selectedPOI.size() > 0) {
			List<PointOfInterest> list = new ArrayList<PointOfInterest>();
			for (PointOfItinerary poi : selectedPOI) {
				list.add((PointOfInterest) poi);
			}
			if(list.size()>0){
			newIt.setPois(list);
			}
		}
		
		// Set date
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			cal.setTime(sdf.parse(s));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		newIt.setData(cal);
		
		// Start next activity
		if (ItineraryCreationActivity.MODIFICATION) {
			// Update itinerary in database
			newIt.setID(intent.getStringExtra(ITINERARY_ID));
			ui.updateItinerary(newIt,db);
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("id", newIt.getID());
			startActivity(intent);
		} else {
			// Save itinerary in database
			String id = ui.saveItinerary(newIt, db);
			Intent intent = new Intent(this, StartingPointActivity.class);
			intent.putExtra(StartingPointActivity.ORIGIN, FROM_CREATION);
			intent.putExtra("id", id);
			startActivity(intent);
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
		Area lastAreaClicked;
		Marker lastMarkerClicked;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_map, container,false);
			setUpMapIfIneed();
			return rootView;
		}

		private void setUpMapIfIneed() {
			if (map == null) {
				SupportMapFragment smf=(SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
				map =smf.getMap();
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
					
					String iconName=marker.getTitle().replace(" ", "_");
					String iconName2=iconName.replace(")", "");
					String iconName3=iconName2.replace("(", "");
					int icon1=getResources().getIdentifier("com.staymilano:drawable/"+iconName3.toLowerCase(), null, null);
					ImageView image=(ImageView) view.findViewById(R.id.image);
					image.setImageResource(icon1);
					
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
				LatLngBounds.Builder areaBounds = new LatLngBounds.Builder(); 
				for (PointOfInterest poi : a.getPois()) {
					MarkerOptions marker = new MarkerOptions();
					marker.title(poi.getName());
					marker.position(poi.getPosition());
					if (checkIfSelected(poi.getName())) {
						marker.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.check));
					} else {
						marker.icon(BitmapDescriptorFactory
								.fromResource(poi.getIcon()));
					}
					markers.add(map.addMarker(marker));
					areaBounds.include(poi.getPosition());
				}
				camup = CameraUpdateFactory.newLatLngBounds(areaBounds.build(), 60);
				map.animateCamera(camup, 1000, null);
				map.setOnMarkerClickListener(markerListener);
				map.setOnInfoWindowClickListener(infowinlistener);
			}
			
			private void deleteMarkers(Area a) {
				for(Marker marker:markers){
					for(PointOfInterest poi:a.getPois()){
						if(poi.getName().equals(marker.getTitle())){
							if(!checkIfSelected(poi.getName())){
								marker.remove();
							}
						}
					}
				}
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
				String title=marker.getTitle();
				if (checkIfSelected(marker.getTitle())) {
					PointOfInterest poi=getPoiFromSelected(marker.getTitle());
					if (poi != null) {
						selectedPOI.remove(poi);
						adapter.remove(poi);
						marker.setIcon(BitmapDescriptorFactory.fromResource(((PointOfInterest) poi)
								.getIcon()));
					}
				} else {
					if(lastMarkerClicked!=null&&lastMarkerClicked.getTitle().equals(marker.getTitle())){
						PointOfInterest poi = City.getCity(db).getPOIbyName(
								marker.getTitle());
						selectedPOI.add(poi);
						adapter.add(poi);
						marker.setIcon(BitmapDescriptorFactory
								.fromResource(R.drawable.check));
					}
					lastMarkerClicked=marker;
				}
				adapter.notifyDataSetChanged();
				return false;
			}

			private PointOfInterest getPoiFromSelected(String title) {
				for(PointOfItinerary poi:selectedPOI){
					if(poi.getName().equals(title)){
						return (PointOfInterest) poi;
					}
				}
				return null;
			}

		};
		
		private boolean checkIfSelected(String string){
			for(PointOfItinerary spoi:selectedPOI){
				if(spoi.getName().equals(string)){
					return true;
				}
			}
			return false;
		}
		
		private OnInfoWindowClickListener infowinlistener = new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
					lastMarkerClicked = marker;
					PointOfInterest point = City.getCity(db).getPOIbyName(marker.getTitle());
					Intent intent = new Intent(getActivity(), POIDetailActivity.class);
					intent.putExtra(POIDetailActivity.TYPE, point.getType());
					intent.putExtra(POIDetailActivity.NAME, point.getName());
					intent.putExtra(POIDetailActivity.POSITION_LAT, point.getPosition().latitude);
					intent.putExtra(POIDetailActivity.POSITION_LNG, point.getPosition().longitude);
					startActivity(intent);
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
					Intent intent = new Intent(getActivity(), POIDetailActivity.class);
					intent.putExtra(POIDetailActivity.TYPE, selectedPOI.get(position).getType());
					intent.putExtra(POIDetailActivity.NAME, selectedPOI.get(position).getName());
					intent.putExtra(POIDetailActivity.POSITION_LAT, selectedPOI.get(position).getPosition().latitude);
					intent.putExtra(POIDetailActivity.POSITION_LNG, selectedPOI.get(position).getPosition().longitude);
					startActivity(intent);

				}
			});
			listView.setAdapter(adapter);

		}
		
		public void showDatePickerDialog(View v) {
			if (selectedPOI.size() > 0) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getActivity().getFragmentManager(), "datePicker");
				newFragment.setRetainInstance(true);
			} else {
				Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
			}
		}
		
		


	}

}
