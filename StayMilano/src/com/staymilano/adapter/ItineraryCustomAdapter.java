package com.staymilano.adapter;

import java.util.List;

import com.staymilano.model.Itinerary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItineraryCustomAdapter extends ArrayAdapter<Itinerary>{

	public ItineraryCustomAdapter(Context context, int resource,
			List<Itinerary> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}
	
	public View getView(int position,View convertView,ViewGroup parent){
		LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView=inflater.inflate(0, null);
		TextView date=(TextView) convertView.findViewById(position);
		Itinerary it=getItem(position);
		date.setText(it.getDate().toString());
		return convertView;
	}

}
