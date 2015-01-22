package com.ftfl.icareapp.util;

public class MyProfile {

	// ------------------- Variables -------------------- //
	private String mName;
	private String mDateOfBirth;
	private String mHeight;
	private String mWeight;
	private String mEyeColor;
	private String mMajorDisase;
	private String mEmail;

	// ------------ Constructor with Parameters --------- //
	public MyProfile(String mName, String mDateOfBirth, String mHeight,
			String mWeight, String mEyeColor, String mMajorDisase, String mEmail) {
		super();
		this.mName = mName;
		this.mDateOfBirth = mDateOfBirth;
		this.mHeight = mHeight;
		this.mWeight = mWeight;
		this.mEyeColor = mEyeColor;
		this.mMajorDisase = mMajorDisase;
		this.mEmail = mEmail;
	}

	// -------------- Getter Setter Methods --------------- //
	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getDateOfBirth() {
		return mDateOfBirth;
	}

	public void setDateOfBirth(String mDateOfBirth) {
		this.mDateOfBirth = mDateOfBirth;
	}

	public String getHeight() {
		return mHeight;
	}

	public void setHeight(String mHeight) {
		this.mHeight = mHeight;
	}

	public String getWeight() {
		return mWeight;
	}

	public void setWeight(String mWeight) {
		this.mWeight = mWeight;
	}

	public String getEyeColor() {
		return mEyeColor;
	}

	public void setEyeColor(String mEyeColor) {
		this.mEyeColor = mEyeColor;
	}

	public String getMajorDisase() {
		return mMajorDisase;
	}

	public void setMajorDisase(String mMajorDisase) {
		this.mMajorDisase = mMajorDisase;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

}
