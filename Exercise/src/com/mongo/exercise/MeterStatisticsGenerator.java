package com.mongo.exercise;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class MeterStatisticsGenerator {

	private static final String METER_TABLE = "meter";
	private static final String METER_READING_TABLE = "meterReading";
	
	private DB db = null;

	public void initDatabase() throws FileNotFoundException, IOException {
		Properties dbProperties = new Properties();
		dbProperties.load((getClass().getResourceAsStream("/db.properties")));
		MongoClient mongo = new MongoClient(dbProperties.getProperty("host"),
				Integer.parseInt(dbProperties.getProperty("port")));
		db = mongo.getDB(dbProperties.getProperty("dbname"));

	}

	public void addMeter(Meter meter) {
		DBCollection meterTable = db.getCollection(METER_TABLE);
		meterTable.insert(meter);
	}

	public void addMeterReading(MeterReading meterReading) {
		DBCollection meterReadingTable = db.getCollection(METER_READING_TABLE);
		meterReadingTable.insert(meterReading);
	}

	public Double calculateAverageUsage(String meterId) {
		DBCollection meterReadingTable = db.getCollection(METER_READING_TABLE);
		DBObject match = new BasicDBObject("$match", new BasicDBObject(
				"meterId", meterId));

		DBObject fields = new BasicDBObject("meterId", 1);
		fields.put("kwh", 1);
		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields);

		DBObject groupFields = new BasicDBObject("_id", "$meterId");
		groupFields.put("average", new BasicDBObject("$avg", "$kwh"));
		DBObject group = new BasicDBObject("$group", groupFields);

		List<DBObject> pipeline = Arrays.asList(match, project, group);
		AggregationOutput output = meterReadingTable.aggregate(pipeline);
		for (DBObject result : output.results()) {
			return (Double) result.get("average");
		}
		return 0.0;
	}

	public DBObject findHighestUsedMeter() {
		DBCollection meterReadingTable = db.getCollection(METER_READING_TABLE);

		DBObject fields = new BasicDBObject("meterId", 1);
		fields.put("kwh", 1);
		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields);

		DBObject groupFields = new BasicDBObject("_id", "$meterId");
		groupFields.put("sum", new BasicDBObject("$sum", "$kwh"));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("sum", -1));

		List<DBObject> pipeline = Arrays.asList(project, group, sort);
		AggregationOutput output = meterReadingTable.aggregate(pipeline);
		for (DBObject result : output.results()) {
			String meterId = (String) result.get("_id");
			DBCollection meterTable = db.getCollection(METER_TABLE);
			DBObject meter = meterTable.findOne(new BasicDBObject("meterId",
					meterId));
			return meter;
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void modifyReading(String meterId, Date readingTime) {
		DBCollection meterReadingTable = db.getCollection(METER_READING_TABLE);
		BasicDBObject updatedReading = new BasicDBObject();
		updatedReading.append("$set", new BasicDBObject().append("kwh", 100));

		BasicDBObject searchQuery = new BasicDBObject().append("meterId",
				meterId).append("readingTime", readingTime);

		WriteResult result = meterReadingTable.update(searchQuery,
				updatedReading);
		if (result.getError() == null || result.getError().isEmpty()) {
			System.out.println("Updated kwh value for - meterId : " + meterId
					+ " readingTime : " + readingTime);
		}
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		MeterStatisticsGenerator metStatGen = new MeterStatisticsGenerator();
		metStatGen.initDatabase();
		for (int i = 1; i <= 10; i++) {
			Meter m = new Meter();
			m.setMeterId(METER_TABLE + i);
			m.setInstallDate(new Date());
			m.setMeterModel("M" + i);
			Address address = new Address("Dummy Street", i, 10000, "Texas");
			m.setAddress(address);
			metStatGen.addMeter(m);
			Random randonKhw = new Random();
			for (int j = 0; j < 24; j++) {
				MeterReading meterReading = new MeterReading();
				meterReading.setKwh(-1.0 + randonKhw.nextDouble() * 10.0);
				meterReading.setMeterId(m.getMeterId());
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR, j);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				meterReading.setReadingTime(cal.getTime());
				metStatGen.addMeterReading(meterReading);
			}
		}
		double avgUsage = metStatGen.calculateAverageUsage("meter1");
		System.out.println("Average usage - " + avgUsage);
		DBObject meter = metStatGen.findHighestUsedMeter();
		System.out.println("Meter with highest KWH usage - MeterId : "
				+ meter.get("meterId") + ", Address : " + meter.get("address"));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		metStatGen.modifyReading("meter1", cal.getTime());
	}
}