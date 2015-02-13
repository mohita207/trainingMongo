package com.mongo.exercise;

import java.util.Date;

import com.mongodb.BasicDBObject;

public class Meter extends BasicDBObject {
	private static final long serialVersionUID = 1L;

	public String getMeterId() {
		return this.getString("meterId");
	}

	public void setMeterId(String meterId) {
		this.put("meterId", meterId);
	}

	public String getMeterModel() {
		return this.getString("meterModel");
	}

	public void setMeterModel(String meterModel) {
		this.put("meterModel", meterModel);
	}

	public Date getInstallDate() {
		return this.getDate("installDate");
	}

	public void setInstallDate(Date installDate) {
		this.put("installDate", installDate);
	}

	public Address getAddress() {
		return (Address) this.get("address");
	}

	public void setAddress(Address address) {
		this.put("address", address);
	}

}