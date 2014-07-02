package com.staymilano;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.Direction;
import model.Stallo;
import visualization.BikeMiLook;
import visualization.MapLook;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bikemiutils.BikeMiCallBack;
import com.example.bikemiutils.BikeMiUtils;
import com.google.android.gms.internal.ik;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.staymilano.database.DBHelper;
import com.staymilano.model.BikeStation;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;
import com.staymilano.model.PointOfItinerary;
import com.staymilano.model.StartingPoint;
import com.staymilano.model.UserInfo;

import communications.CallBack;
import communications.GoogleMapsUtils;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	ViewPager mViewPager;
	AppSectionsPagerAdapter mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
	static Itinerary it;
	
	static List<PointOfItinerary> points;
	static POIAdapter adapter;
	
	public static final String WIFIITINERARY="wifi_itinerary";
	
	int ICONS[]= new int[]{R.drawable.ic_action_map,R.drawable.ic_action_view_as_list};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		String itineraryID = intent.getStringExtra("id");

		
		it=new Itinerary();
		if (itineraryID != null) {
			SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
			UserInfo ui = UserInfo.getUserInfo(db);
			it = ui.getItinerary(itineraryID);
		}
		
		points=new ArrayList<PointOfItinerary>();
		points=it.getAllPointOfThisItinerary();
		adapter=new POIAdapter(this, points);
		adapter.addAll(points);
		adapter.notifyDataSetChanged();
		
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
					 .setIcon(getResources().getDrawable(ICONS[i]))
					 .setTabListener(this));

		}

	}

	
	@Override
	public void onBackPressed() {
		Intent intent=new Intent(this, ItineraryListActivity.class);
		startActivity(intent);
		finish();
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
	
	/////////////////////////////MAP FRAGMENT////////////////////////////////////////////////////////////////////////

	public static class MapSectionFragment extends Fragment implements OnMapLoadedCallback, CallBack, BikeMiCallBack{

    	SQLiteDatabase db;
		GoogleMap map;
		
		//boolean bikeAdded=false;

		
		static final LatLng MILAN = new LatLng(45.4773, 9.1815);

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_main, container,	false);
			db = DBHelper.getInstance(getActivity()).getWritableDatabase();
			setHasOptionsMenu(true);
			
			setUpMapIfIneed();
			return rootView;
		}
		
		@Override
		public void onResume() {
			super.onResume();
			setUpMapIfIneed();
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.option_menu, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if(item.getItemId()==R.id.action_show_bicycle){
				//TODO inserire posizione attuale
					BikeMiUtils.getBikeMiStations(this, it.getStartingPoint().position);
			}else if(item.getItemId()==R.id.action_remove_bicycle){
				if (it.getSelectedBikeSt().size() != 0) {
					new AlertDialog.Builder(getActivity())
							.setTitle("Remove Bike Stations")
							.setMessage(
									"Are you sure you want to remove all bike stations?")
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											BikeMiLook.removeMarkers();
											UserInfo.removeBikeStations(db, it);
											points = it
													.getAllPointOfThisItinerary();
											adapter.clear();
											adapter.addAll(points);
											adapter.notifyDataSetChanged();
											map.clear();
											getDirections();
										}
									})
							.setNegativeButton(android.R.string.no,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// do nothing
										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();
				} else {
					if (!BikeMiLook.removeMarkers()) {
						Toast.makeText(getActivity(),
								"No Bike Station to eliminate",
								Toast.LENGTH_LONG);
					}else{
						setCamera();
					}
				}
			}else if(item.getItemId()==R.id.action_share){
				Intent intent=new Intent(getActivity(), WiFiActivity.class);
				intent.putExtra(WIFIITINERARY, UserInfo.itineraryToString(it));
				startActivity(intent);
			}else if(item.getItemId()==R.id.action_starting_point){
				new AlertDialog.Builder(getActivity())
			    .setTitle("Change starting point")
			    .setMessage("Are you sure you want to change this starting point?")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	Intent intent = new Intent(getActivity(),StartingPointActivity.class);
			        	intent.putExtra("id", it.getID());
						startActivity(intent);
			        }
			     })
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			}else if(item.getItemId()==R.id.action_settings){
				Intent intent=new Intent(getActivity(), ItineraryCreationActivity.class);
				String s=it.getID();
				intent.putExtra(ItineraryCreationActivity.ITINERARY_ID,s);
				startActivity(intent);
			}
			return super.onOptionsItemSelected(item);
		}

		private void setUpMapIfIneed() {
			
			if (map == null) {
				SupportMapFragment smf = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapMain); 
				map = smf.getMap();
			}
			if (map != null) {
				setupMap();
			}
		}

		private void setupMap() {
			
			CameraPosition cameraPosition = new CameraPosition.Builder()
		    .target(MILAN) 
		    .tilt(40)
		    .zoom(10)					// Sets the tilt of the camera to 30 degrees
		    .build();                   // Creates a CameraPosition from the builder
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
			map.setOnMapLoadedCallback(this);
			map.getUiSettings().setZoomControlsEnabled(false);
		}

		@Override
		public void onMapLoaded() {
			setCamera();
			getDirections();		
		}
		
		private void setCamera(){
			LatLngBounds.Builder itBounds = new LatLngBounds.Builder();  
			for(LatLng p : it.getAllItineraryCoordinates()){
				itBounds.include(p);
			}
			map.animateCamera(CameraUpdateFactory.newLatLngBounds(itBounds.build(), 60), 1500, null);
		}

		private void getDirections() {

			// get directions for the specified itinerary
			List<LatLng> poiSequence = it.getAllItineraryCoordinates();
			GoogleMapsUtils.getDirection(this, poiSequence,
					GoogleMapsUtils.MODE_WALKING);

			// draw pois
			List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
			MarkerOptions marker = new MarkerOptions();
			marker.title("start");
			marker.position(it.getStartingPoint().getPosition());
			marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
			markers.add(marker);
			for (PointOfInterest poi : it.getPois()) {
				marker = new MarkerOptions();
				marker.title(poi.getName());
				marker.position(poi.getPosition());
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.selected));
				markers.add(marker);
			}
			for(BikeStation bikeSt:it.getSelectedBikeSt()){
				marker = new MarkerOptions();
				marker.title(bikeSt.getName());
				marker.position(bikeSt.getPosition());
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.purplebicycle));
				markers.add(marker);
			}
			MapLook.drawPOI(markers, map);
		}

			
		@Override
		public void onDirectionLoaded(List<Direction> directions) {
			// once the google server provides the directions, draw them on the map
			MapLook.drawDirection(directions, map);
		}

		@Override
		public void onBikeMiStationsComputed(List<Stallo> result) {
			List<MarkerOptions> bikeMarkers=new ArrayList<MarkerOptions>();
			LatLngBounds.Builder itBounds = new LatLngBounds.Builder(); 
			for(Stallo st:result){
				if(!it.hasThisBikeStYet(st.getName())){
				MarkerOptions marOp=new MarkerOptions();
				marOp.position(st.getPosition());
				marOp.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerbikepurplemini));
				marOp.title(st.getName());
				marOp.snippet(st.getEmptySlots()+" empy slots and "+st.getFreeBikes()+" free bikes");
				bikeMarkers.add(marOp);
				itBounds.include(st.getPosition());
				}
			}
			
			BikeMiLook.drawMarkers(map, bikeMarkers);
			map.animateCamera(CameraUpdateFactory.newLatLngBounds(itBounds.build(), 60), 1500, null);

			map.setOnMarkerClickListener(listener);
		}
		
		private OnMarkerClickListener listener= new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(final Marker marker) {
				if(!isPointMarker(marker)){
					new AlertDialog.Builder(getActivity())
					.setTitle(marker.getTitle()+" BikeMi Station")
					.setMessage("This bike station has: \n"+marker.getSnippet()+"\nAre you sure you want to add this BikeMi station to your itinerary?")
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	BikeStation bike=UserInfo.saveBikeStation(db,it,marker.getTitle(),marker.getPosition());
				        	map.clear();
				        	getDirections();
				        	points.add(bike);
				        	adapter.add(bike);
				        	adapter.notifyDataSetChanged();
				        	BikeMiLook.removeMarkers();	
				        	setCamera();
				        }
				     })
				    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            // do nothing
				        }
				     })
				    .setIcon(R.drawable.purplebicycle)
				     .show();
				}
				return true;
			}

			private boolean isPointMarker(Marker marker) {
				for(PointOfInterest poi:it.getPois()){
					if(poi.getName().equals(marker.getTitle())){
						return true;
					}
				}
				return false;
			}
			
			
		};

	}

	/////////////////////////LIST FRAGMENT/////////////////////////////////////////////////////////
	public static class MyListFragment extends Fragment {


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_poilist,container, false);
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
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(), ItineraryCreationActivity.class);
					String s=it.getID();
					intent.putExtra(ItineraryCreationActivity.ITINERARY_ID,s);
					startActivity(intent);
					
				}
			});
			
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (it.getAllPointOfThisItinerary().get(position).getType().equals(StartingPoint.STARTING_POINT)) {
						new AlertDialog.Builder(getActivity())
					    .setTitle("Change starting point")
					    .setMessage("Are you sure you want to change this starting point?")
					    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					        	Intent intent = new Intent(getActivity(),
										StartingPointActivity.class);
					        	intent.putExtra("id", it.getID());
								startActivity(intent);
					        }
					     })
					    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // do nothing
					        }
					     })
					    .setIcon(android.R.drawable.ic_dialog_alert)
					     .show();
					} else {
						Intent intent = new Intent(getActivity(), POIDetailActivity.class);
						intent.putExtra(POIDetailActivity.TYPE, it.getAllPointOfThisItinerary().get(position).getType());
						intent.putExtra(POIDetailActivity.NAME, it.getAllPointOfThisItinerary().get(position).getName());
						Bundle bundle=new Bundle();
						bundle.putParcelable(POIDetailActivity.POSITION, it.getAllPointOfThisItinerary().get(position).getPosition());
						intent.putExtra("bundle", bundle);
						startActivity(intent);
						}
					}

			});
			listView.setAdapter(adapter);
		}
	}

}