package com.ftfl.icareapp.util;

public class DietChart {
	
	// ------------------ Variables ------------------- //

	private String mEventName;
	private String mDate;
	private String mMenu;
	private String mTime;
	private Integer mID;

	// --- Constructor with ID and OtherParameters ---- //
	// (ID in last Parameter)
	public DietChart(String mEventName, String mDate, String mMenu,
			String mTime, Integer mID) {
		
		this.mEventName = mEventName;
		this.mDate = mDate;
		this.mMenu = mMenu;
		this.mTime = mTime;
		this.mID = mID;
	}

	// --------- Constructor without ID and Others ------- //
	public DietChart(String mEventName, String mDate, String mMenu, String mTime) {

		this.mEventName = mEventName;
		this.mDate = mDate;
		this.mMenu = mMenu;
		this.mTime = mTime;
	}

	// -------------- Getter Setter Methods --------------- //
	public String getEventName() {
		return mEventName;
	}

	public void setEventName(String mEventName) {
		this.mEventName = mEventName;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String mDate) {
		this.mDate = mDate;
	}

	public String getMenu() {
		return mMenu;
	}

	public void setMenu(String mMenu) {
		this.mMenu = mMenu;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String mTime) {
		this.mTime = mTime;
	}

	public Integer getID() {
		return mID;
	}

	public void setID(Integer mID) {
		this.mID = mID;
	}

}
