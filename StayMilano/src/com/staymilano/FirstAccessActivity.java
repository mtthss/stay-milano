package com.staymilano;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
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

public class FirstAccessActivity extends ActionBarActivity {

	private Button createButton;
	private Button downloadButton;
	
	private Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_access);
		
		ctx=this;
		
		createButton=(Button) findViewById(R.id.button2);
		downloadButton=(Button) findViewById(R.id.button1);
		
		createButton.setOnClickListener(listener);
		downloadButton.setOnClickListener(listener);
	}

	OnClickListener listener= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final int buttonId=v.getId();
			if(buttonId==R.id.button2){
				final Intent intent=new Intent(ctx, AreaSelectionActivity.class);
	        	startActivity(intent);
			}
			
		}
	};
}
