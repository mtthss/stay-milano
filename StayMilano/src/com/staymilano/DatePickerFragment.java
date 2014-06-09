package com.staymilano;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.staymilano.database.DBHelper;
import com.staymilano.model.Itinerary;
import com.staymilano.model.PointOfInterest;
import com.staymilano.model.UserInfo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	SQLiteDatabase db;
	List<PointOfInterest> selectedPoi;
	
	
	
	public DatePickerFragment(AreaSelectionActivity asa) {
		selectedPoi = asa.getPoiList();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Get database
		db = DBHelper.getInstance(AreaSelectionActivity.ctx).getWritableDatabase();
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// create itinerary with given points of interest and date
		UserInfo ui = UserInfo.getUserInfo(db);
		Itinerary it = new Itinerary();
		
		// set point of interests
		it.setPois(selectedPoi);
		
		// set date
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String s = dayOfMonth + "-" + monthOfYear + "-" + year;
		try {
			cal.setTime(sdf.parse(s));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		it.setData(cal);
		
		// save itinerary in database
		ui.saveItinerary(it, db);
		
		// start next activity
		Intent intent=new Intent(getActivity(), StartingPointActivity.class);
		startActivity(intent);
	}
}