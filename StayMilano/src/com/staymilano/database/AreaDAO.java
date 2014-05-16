package com.staymilano.database; 
  
import java.util.StringTokenizer; 
  
import android.content.ContentValues; 
import android.database.Cursor; 
import android.database.SQLException; 
import android.database.sqlite.SQLiteDatabase; 
  
public class AreaDAO { 
    //TODO nomi in inglese 
    public static final String ID = "_id"; 
    public static final String NAME = "name"; 
    public static final String LATITUDE = "latitude"; 
    public static final String LONGITUDE = "longitude"; 
    public static final String CENTER = "center"; 
      
    public static final String TABELLA = "area"; 
    public static final String[] COLONNE = new String[]{ID,NAME,LATITUDE,LONGITUDE,CENTER}; 
      
      
    public static ContentValues getContentValues(String line) { 
        ContentValues result = new ContentValues(); 
        StringTokenizer stringTokenizer = new StringTokenizer(line, ","); 
        result.put(NAME, stringTokenizer.nextToken()); 
        result.put(CENTER, stringTokenizer.nextToken()); 
        result.put(LATITUDE, stringTokenizer.nextToken()); 
        result.put(LONGITUDE, stringTokenizer.nextToken()); 
        return result; 
    } 
      
    public static Cursor getPointsByArea(SQLiteDatabase db, String area) throws SQLException { 
        Cursor c = db.query(true, TABELLA, COLONNE, NAME + "=" + "'"+area+"'", null, null, null, null, null); 
        if (c != null) { 
            c.moveToFirst(); 
        } 
        return c; 
    } 
      
    /*public static Cursor getCenterByArea(SQLiteDatabase db, String area) throws SQLException { 
        Cursor c = db.query(true, TABELLA, COLONNE, NAME + "=" + "'"+area+"'", null, null, null, null, null); 
        if (c != null) { 
            c.moveToFirst(); 
        } 
        return c; 
    }*/
  
    public static String getInsertQuery(String line) { 
        StringTokenizer stringTokenizer = new StringTokenizer(line, ","); 
        String result="'"+stringTokenizer.nextToken()+"','"+stringTokenizer.nextToken()+"','"+stringTokenizer.nextToken()+"','"+stringTokenizer.nextToken()+"'"; 
        return result; 
    } 
  
} 
