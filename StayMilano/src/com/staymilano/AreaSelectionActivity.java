package com.staymilano;

import java.util.ArrayList;

import visualization.MapLook;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.staymilano.database.DBHelper;
import com.staymilano.model.Area;
import com.staymilano.model.AreasName;
import com.staymilano.model.City;
import com.staymilano.model.PointOfInterest;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


public class AreaSelectionActivity extends Activity implements OnMapLoadedCallback{
	
	
	/*private Button mButton1;
	private Button mButton2;
	private Button mButton3;
	private Button mButton4;
	private Button mButton5;
	private Button mButton6;*/
	
	private Intent intent;
	
	private GoogleMap map;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_selection);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		

	/*	intent = new Intent(this, POIListActivity.class);
		mButton1 = (Button) findViewById(R.id.button);
		mButton2 = (Button) findViewById(R.id.button2);
		mButton3 = (Button) findViewById(R.id.button3);
		mButton4 = (Button) findViewById(R.id.button4);
		mButton5 = (Button) findViewById(R.id.button5);
		mButton6 = (Button) findViewById(R.id.button6);
		mButton1.setOnClickListener(mOnClickListener);
		mButton2.setOnClickListener(mOnClickListener);
		mButton3.setOnClickListener(mOnClickListener);
		mButton4.setOnClickListener(mOnClickListener);
		mButton5.setOnClickListener(mOnClickListener);
		mButton6.setOnClickListener(mOnClickListener);*/

	}


	@Override
	public void onMapLoaded() {
		MapLook.drawAreas(
				City.getCity(DBHelper.getInstance(this).getWritableDatabase())
						.getPolygons(), map);

	}
	
	/*private final OnClickListener mOnClickListener=new OnClickListener() {
		

		@Override
		public void onClick(View v) {
			String mArea;
			final int buttonId=v.getId();
			switch (buttonId) {
			case R.id.button:
				mArea=AreasName.DUOMO.toString();
				break;
			case R.id.button2:
				mArea = AreasName.CASTELLO.toString();
				break;
			case R.id.button3:
				mArea = AreasName.PORTATICINESE.toString();
				break;
			case R.id.button4:
				mArea = AreasName.PORTAROMANA.toString();
				break;
			case R.id.button5:
				mArea = AreasName.PALESTRO.toString();
				break;
			default:
				mArea = AreasName.EXPO15.toString();
				break;
			}
			
			intent.putExtra(POIListActivity.AREA_EXTRA, mArea);
			startActivity(intent);
		}
	};*/
	


}