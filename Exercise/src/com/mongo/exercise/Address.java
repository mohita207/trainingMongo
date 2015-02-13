package com.mongo.exercise;

import com.mongodb.BasicDBObject;

public class Address extends BasicDBObject{

	private static final long serialVersionUID = 1L;

	public Address(String streeName, Integer houseNumber, Integer zipCode,
			String state) {
		this.put("streeName", streeName);
		this.put("houseNumber", houseNumber);
		this.put("zipCode", zipCode);
		this.put("state", state);
	}

	public String getStreeName() {
		return this.getString("streeName");
	}

	public void setStreeName(String streeName) {
		this.put("streeName", streeName);
	}

	public Integer getHouseNumber() {
		return this.getInt("houseNumber");
	}

	public void setHouseNumber(Integer houseNumber) {
		this.put("houseNumber", houseNumber);
	}

	public Integer getZipCode() {
		return this.getInt("zipCode");
	}

	public void setZipCode(Integer zipCode) {
		this.put("zipCode", zipCode);
	}

	public String getState() {
		return this.getString("state");
	}

	public void setState(String state) {
		this.put("state", state);	}
}