package com.staymilano;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.staymilano.database.DBHelper;
import com.staymilano.database.ItineraryDAO;
import com.staymilano.model.City;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class SplashActivity extends Activity {
	
	private static final long MIN_WAIT_INTERVAL= 1500L;
	private static final long MAX_WAIT_INTERVAL= 3000L;
	private static final int GO_AHEAD_WHAT= 1;
	
	private long mStartTime;
	private boolean mIsDone;
	
	private UiHandler mHandler;
	
	private static final String TAG_LOG=SplashActivity.class.getName();	
	
	//classe interna, gestisce solo un caso (reindirizzamento a firstAccessActivity)
	//ottimizzata per evitare il memory leak
	private static class UiHandler extends Handler{
		
		private WeakReference<SplashActivity> mActivityRef;
		
		public UiHandler(final SplashActivity srcActivity){
			this.mActivityRef= new WeakReference<SplashActivity>(srcActivity);
		}
		
		@Override
		public void handleMessage(Message msg){
			final SplashActivity srcActivity=this.mActivityRef.get();
			if(srcActivity==null){
				return;
			}
			//TODO aggiungere i casi di reindirizzamento a menu (crea/i tuoi itinerari) e a
			//mappa dell'itinerario odierno
			switch(msg.what){
			case GO_AHEAD_WHAT:
				long elapsedTime = SystemClock.uptimeMillis()-srcActivity.mStartTime;
				if(elapsedTime>=MIN_WAIT_INTERVAL && !srcActivity.mIsDone){
					srcActivity.mIsDone=true;
					srcActivity.goAhead();
				}
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.w(TAG_LOG, "SONO IN Splash on create");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		mHandler=new UiHandler(this);
		}
	
	@Override
	protected void onStart(){
		super.onStart();
		mStartTime= SystemClock.uptimeMillis();
		final Message goAheadMessage= mHandler.obtainMessage(GO_AHEAD_WHAT);
		mHandler.sendMessageAtTime(goAheadMessage, mStartTime+MAX_WAIT_INTERVAL);
	}
	

	protected void goAhead() {
        //apro il DB in lettura
        SQLiteDatabase db = DBHelper.getInstance(this).getWritableDatabase();
        
        Date date= new Date();
        
        Cursor cur=ItineraryDAO.getItineraryByDate(db,new SimpleDateFormat("yyyy-MM-dd").format(date));
        
        final Intent intent;
        if(cur.getCount()!=0){
        	intent= new Intent(this,MainActivity.class);
        	startActivity(intent);
        }else{
        	cur=ItineraryDAO.getAllItinerary(db);
        	if(cur.getCount()==0){
	        	intent=new Intent(this,FirstAccessActivity.class);
	        	startActivity(intent);
        	}else{
        		intent=new Intent(this,ItineraryListActivity.class);
        		startActivity(intent);
        	}
        }
        finish();
	}

}
