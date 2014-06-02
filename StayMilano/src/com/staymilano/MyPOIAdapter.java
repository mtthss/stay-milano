package com.staymilano;

import java.util.List;

import com.staymilano.model.PointOfInterest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPOIAdapter extends ArrayAdapter<PointOfInterest>{
	
	private final Context context;
	private final List<PointOfInterest> values;

	public MyPOIAdapter(Context context, List<PointOfInterest> values) {
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
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		ImageButton imageButton = (ImageButton) rowView.findViewById(R.id.imageButton);		
		
		first.setText((values.get(position)).getName());
		second.setText((values.get(position)).getName());

		return rowView;
	}
}
