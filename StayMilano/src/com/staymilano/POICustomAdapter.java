package com.staymilano;

import java.util.List;

import com.staymilano.model.PointOfInterest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class POICustomAdapter extends ArrayAdapter<PointOfInterest> {

	public POICustomAdapter(Context context, int resource,
			List<PointOfInterest> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}
	
	public View getView(int position,View convertView,ViewGroup parent){
		LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView=inflater.inflate(R.layout.row_point, null);
		TextView name=(TextView) convertView.findViewById(R.id.point_name);
		PointOfInterest poi=getItem(position);
		name.setText(poi.getName());
		return convertView;
	}

}
