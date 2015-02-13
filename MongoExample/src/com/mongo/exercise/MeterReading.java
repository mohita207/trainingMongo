package com.mongo.exercise;

import java.util.Date;

import com.mongodb.BasicDBObject;

public class MeterReading extends BasicDBObject {
	private static final long serialVersionUID = 1L;

	public Date getReadingTime() {
		return this.getDate("readingTime");
	}

	public void setReadingTime(Date readingTime) {
		this.put("readingTime", readingTime);
	}

	public double getKwh() {
		return this.getDouble("kwh");
	}

	public void setKwh(double kwh) {
		this.put("kwh", kwh);
	}

	public double getMeterId() {
		return this.getDouble("kwh");
	}

	public void setMeterId(String meterId) {
		this.put("meterId", meterId);
	}
}