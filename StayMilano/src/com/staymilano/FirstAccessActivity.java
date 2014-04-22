package com.staymilano;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;

public class FirstAccessActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_access);
	}

	public void createNew(View view){
		Intent intent = new Intent(this, AreaSelection.class);
		startActivity(intent);
	}

}
