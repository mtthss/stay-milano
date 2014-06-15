package com.staymilano;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class FirstAccessActivity extends Activity {

	private Button createButton;
	
	private Context ctx;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_access);
		
		ctx=this;
		
		createButton=(Button) findViewById(R.id.button2);
		
		createButton.setOnClickListener(listener);
	}

	OnClickListener listener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final int buttonId=v.getId();
			if(buttonId==R.id.button2){
				intent=new Intent(ctx, ItineraryCreationActivity.class);
	        	startActivity(intent);
			}
			
		}
	};
}
