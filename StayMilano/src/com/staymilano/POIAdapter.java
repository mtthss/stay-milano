package com.staymilano;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.staymilano.model.PointOfInterest;
import com.staymilano.model.PointOfItinerary;
import com.staymilano.model.StartingPoint;

public class POIAdapter extends ArrayAdapter<PointOfItinerary>{
	
	private final Context context;
	private List<PointOfItinerary> values=new ArrayList<PointOfItinerary>();

	public POIAdapter(Context context, List<PointOfItinerary> list) {
		super(context, R.layout.row_point);
		this.context = context;
		this.values = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_point, parent, false);
		
		TextView first = (TextView) rowView.findViewById(R.id.firstLine);
		TextView second = (TextView) rowView.findViewById(R.id.secondLine);
		ImageView icon=(ImageView) rowView.findViewById(R.id.imageView1);
		
		String s=(values.get(position)).getName();
		first.setText((values.get(position)).getName());
		second.setText((values.get(position)).getType());
		if(values.get(position).getType().equals(StartingPoint.STARTING_POINT)){
			icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_edit));
		}else{
			icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_about));

		}
		return rowView;
	}
}
