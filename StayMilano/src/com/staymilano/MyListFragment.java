package com.staymilano;

import java.util.ArrayList;
import java.util.List;

import com.staymilano.model.PointOfInterest;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class MyListFragment extends ListFragment {
	
	ArrayAdapter<String> adapter;
	List<PointOfInterest> pois;
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		pois=new ArrayList<PointOfInterest>();
		adapter=new ArrayAdapter<String>(getActivity(),R.layout.band_layout);
	}
}
