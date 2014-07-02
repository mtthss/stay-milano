package com.staymilano.database; 
  
import java.util.StringTokenizer; 
  
import android.content.ContentValues; 
import android.database.Cursor; 
import android.database.SQLException; 
import android.database.sqlite.SQLiteDatabase; 
  
public class AreaDAO { 

    public static final String ID = "_id"; 
    public static final String NAME = "name"; 
    public static final String LATITUDE = "latitude"; 
    public static final String LONGITUDE = "longitude"; 
      
    public static final String TABELLA = "area"; 
    public static final String[] COLONNE = new String[]{ID,NAME,LATITUDE,LONGITUDE}; 
   
      
    public static Cursor getPointsByArea(SQLiteDatabase db, String area) throws SQLException { 
        Cursor c = db.query(true, TABELLA, COLONNE, NAME + "=" + "'"+area+"'", null, null, null, null, null); 
        if (c != null) { 
            c.moveToFirst(); 
        } 
        return c; 
    } 
  
    public static long insertArea(SQLiteDatabase db,String line) { 
        StringTokenizer stringTokenizer = new StringTokenizer(line, ","); 
        String name=stringTokenizer.nextToken(),
        		lat=stringTokenizer.nextToken(),
        		lng=stringTokenizer.nextToken(); 
        ContentValues values=new ContentValues();
        values.put(NAME, name);
        values.put(LATITUDE, lat);
        values.put(LONGITUDE, lng);
        return db.insert(TABELLA, null, values); 
    } 
  
} 
