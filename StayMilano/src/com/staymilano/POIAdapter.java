package com.staymilano;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.staymilano.model.PointOfInterest;

public class POIAdapter extends ArrayAdapter<PointOfInterest>{
	
	private final Context context;
	private final List<PointOfInterest> values;

	public POIAdapter(Context context, List<PointOfInterest> values) {
		super(context, R.layout.row_point,values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_point, parent, false);
		
		TextView first = (TextView) rowView.findViewById(R.id.firstLine);
		TextView second = (TextView) rowView.findViewById(R.id.secondLine);
		ImageView icon=(ImageView) rowView.findViewById(R.id.imageView1);
		
		first.setText((values.get(position)).getName());
		second.setText((values.get(position)).getType());
		if(values.get(position).getName().equals("start")){
			icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_settings));
		}else{
			icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_about));

		}
		return rowView;
	}
}
