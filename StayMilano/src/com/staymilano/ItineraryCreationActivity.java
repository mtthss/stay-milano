package com.staymilano;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import visualization.MapLook;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.staymilano.database.DBHelper;
import com.staymilano.model.Area;
import com.staymilano.model.City;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;
import com.staymilano.model.UserInfo;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    
    static List<PointOfInterest> selectedPOI=new ArrayList<PointOfInterest>();
	Itinerary it;
	static List<MarkerOptions> markers;
	static final LatLng MILAN = new LatLng(45.4773, 9.1815);

	public static final String POI = "poi";
	
	static Intent intent;

	

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_creation);

        manager=getSupportFragmentManager();
        ctx=ItineraryCreationActivity.this;
        
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        
        intent=getIntent();
        String itineraryid=intent.getStringExtra("id");

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(false);              
        actionBar.setDisplayShowTitleEnabled(false);
        
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }
	
	
	public void showDatePickerDialog(View v) {
		if (selectedPOI.size() > 0) {
			DialogFragment newFragment = new DatePickerFragment(this);
			newFragment.show(getFragmentManager(), "datePicker");
			newFragment.setRetainInstance(true);
		}else{
			Toast.makeText(ctx, R.string.error, Toast.LENGTH_SHORT).show();
		}
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
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    
    public List<PointOfInterest> getPoiList(){
    	return selectedPOI;
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                	MapPOIFragment mf=new MapPOIFragment();
                	mf.setRetainInstance(true);
                    return mf;

                default:
                    Fragment fragment = new ListFragment();
                    fragment.setRetainInstance(true);
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

    public static class MapPOIFragment extends Fragment implements OnMapLoadedCallback{
    	    	
    	GoogleMap map;
    	SQLiteDatabase db;

    	
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);
            
            db = DBHelper.getInstance(ItineraryCreationActivity.ctx).getWritableDatabase();
            
            String itinerary_id=intent.getStringExtra("id");
            if(itinerary_id!=null){
            	UserInfo ui=UserInfo.getUserInfo(db);
            	Itinerary it=ui.getItinerary(itinerary_id);
            	if(it!=null){
            		selectedPOI=it.getPois();
            	}
            }
            
            setUpMapIfIneed();
            return rootView;
        }
        
    	private void setUpMapIfIneed() {
			if(map==null){
				map=((SupportMapFragment)ItineraryCreationActivity.manager.findFragmentById(R.id.map)).getMap();
			}
			if(map!=null){
				setupMap();
			}	
		}

		private void setupMap() {
			map.setOnMapLoadedCallback(this);
    		map.moveCamera(CameraUpdateFactory.newLatLngZoom(MILAN, 10));
    		map.animateCamera(CameraUpdateFactory.zoomTo((float) 12.5), 1000, null);
    		map.setOnMapClickListener(listener);			
		}

		public void onMapLoaded() {
    		MapLook.drawAreas(City.getCity(db).getPolygons(), map);
    	}
    	
    	private final OnMapClickListener listener = new OnMapClickListener() {

    		@Override
    		public void onMapClick(LatLng point) {
    				List<Area> areas = City.getCity(db).getAllAreas();
    				Area a = PointInPolygon(point, areas);
    				if (a != null) {
    					map.clear();
    					markers = new ArrayList<MarkerOptions>();
    					for (PointOfInterest poi : a.getPois()) {
    						MarkerOptions marker = new MarkerOptions();
    						marker.title(poi.getName());
    						marker.position(poi.getPosition());
    						marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
    						markers.add(marker);	
    					}
    					MapLook.drawPOI(markers, map);
    					map.animateCamera(CameraUpdateFactory.newLatLngZoom(a.getCenter(), (float) 12.5));
    					map.animateCamera(CameraUpdateFactory.zoomTo((float) 15.5), 1000,null);
    					map.setOnMarkerClickListener(markerListener);
    				}else{
    					map.clear();
        				map.animateCamera(CameraUpdateFactory.zoomTo((float) 12.5), 1000, null);
        				MapLook.drawAreas(City.getCity(db).getPolygons(), map);
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
    			if(selectedPOI.contains(poi)){
    				selectedPOI.remove(poi);
    				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
    				adapter.notifyDataSetChanged();
    			}else{
    				selectedPOI.add(poi);
    				marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.check));
    				adapter.notifyDataSetChanged();
    				
    			}
    			return false;
    		}

    	};
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class ListFragment extends Fragment {
    	
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View rootView = inflater.inflate(R.layout.fragment_poilist, container, false);
    		return rootView;
    	}

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
          super.onActivityCreated(savedInstanceState);
          
          ListView listView=(ListView) getActivity().findViewById(R.id.listViewPOI);
          View empty = getActivity().findViewById(R.id.emptyList);
          listView.setEmptyView(empty);
          Button button=(Button) getActivity().findViewById(R.id.saveMap);
          button.setText(R.string.save);
          listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent=new Intent(ctx, POIDetail.class);
				intent.putExtra(ItineraryCreationActivity.POI, selectedPOI.get(position).getId());
				startActivity(intent);
				
				
			}
		});
          adapter = new POIAdapter(getActivity(), selectedPOI);
          listView.setAdapter(adapter);
          
        }
        
    }
}

