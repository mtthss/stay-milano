package com.staymilano.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.staymilano.database.PointOfInterestDAO;

public class Area implements Serializable {

	private static final long serialVersionUID = 3577242940737875282L;

	private String name;
	private List<PointOfInterest> pois;
	private PolygonOptions pol;

	public Area(SQLiteDatabase db, AreasName nm) {
		this.name = nm.toString();
		pois = new ArrayList<PointOfInterest>();
		this.pois = fillPois(db, nm);
		pol = new PolygonOptions();
		createPolygon();
	}

	private void createPolygon() {
		InputStreamReader stream;
		try {
			stream = new InputStreamReader(new FileInputStream(new File(
					"areas_points.csv")));
			BufferedReader reader = new BufferedReader(stream);
			List<LatLng> points = new ArrayList<LatLng>();
			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					StringTokenizer stringTokenizer = new StringTokenizer(line,
							",");

					if (stringTokenizer.nextToken().equalsIgnoreCase(name)) {
						LatLng latlng = new LatLng(
								Float.parseFloat(stringTokenizer.nextToken()),
								Float.parseFloat(stringTokenizer.nextToken()));
						points.add(latlng);
					} else {
						return;
					}

				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
					}
				}
			}

		} catch (FileNotFoundException e1) {
			return;
		}

	}

	private List<PointOfInterest> fillPois(SQLiteDatabase db, AreasName nm) {
		Cursor cur = PointOfInterestDAO.getPOIByArea(db, nm.toString()
				.toLowerCase());
		while (cur.moveToNext()) {
			PointOfInterest poi = new PointOfInterest();
			poi.setName(cur.getString(1));
			poi.setDescription(cur.getString(2));
			poi.setType(cur.getString(3));
			pois.add(poi);
		}
		return pois;
	}

	public String getName() {
		return name;
	}

	public List<PointOfInterest> getPois() {
		return pois;
	}

	public PolygonOptions getPolygon() {
		return pol;
	}

}
