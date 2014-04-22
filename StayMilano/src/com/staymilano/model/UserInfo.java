package com.staymilano.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserInfo implements Serializable{

	private static final long serialVersionUID = 8354095811783356274L;
	
	private List<Trip> travel;
	private static UserInfo user;	
	
	private UserInfo(){
		travel=fillTravel();
	}

	private List<Trip> fillTravel() {
		travel=new ArrayList<Trip>();
		return travel;
	}
	
	private static UserInfo create(){
		return new UserInfo();
	}
	
	public static UserInfo getUser(){
		if(user==null){
			create();
		}
		return user;
	}
	
	public int getTravelsize(){
		return travel.size();
	}
	
	public boolean isThereAnItinerary(Date today){
		for(Trip tr:travel){
			return tr.findItineraryByDate(today);
		}
		return false;
	}
}
