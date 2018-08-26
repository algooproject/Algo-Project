package com.algotrading.persistence.mongo.dbobject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DailyPrice implements DBObjectable {

	private static final String FIELD_TICKER = "ticker";
	private static final String FIELD_ADJUSTED_CLOSE = "adjustedClose";
	private static final String FIELD_VOLUME = "volume";
	private static final String FIELD_CLOSE = "close";
	private static final String FIELD_OPEN = "open";
	private static final String FIELD_LOW = "low";
	private static final String FIELD_HIGH = "high";
	private static final String FIELD_DATE = "date";

	private String ticker;
	private String date; // yyyy-MM-dd
	private double high;
	private double low;
	private double open;
	private double close;
	private double volume;
	private double adjustedClose;

	public String getTicker() {
		return ticker;
	}

	public String getDate() {
		return date;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getOpen() {
		return open;
	}

	public double getClose() {
		return close;
	}

	public double getVolume() {
		return volume;
	}

	public double getAdjustedClose() {
		return adjustedClose;
	}

	@Override
	public String toString() {
		return "DailyPrice [ticker=" + ticker + ", date=" + date + ", high=" + high + ", low=" + low + ", open=" + open
				+ ", close=" + close + ", volume=" + volume + ", adjustedClose=" + adjustedClose + "]";
	}

	@Override
	public DBObject toDBObject() {
		return new BasicDBObject("_id", getKey()).append(FIELD_DATE, getDate()).append(FIELD_HIGH, getHigh())
				.append(FIELD_LOW, getLow()).append(FIELD_OPEN, getOpen()).append(FIELD_CLOSE, getClose())
				.append(FIELD_VOLUME, getVolume()).append(FIELD_ADJUSTED_CLOSE, getAdjustedClose())
				.append(FIELD_TICKER, getTicker());
	}

	public void fromDBObject(DBObject dbObject) {
		date = (String) dbObject.get(FIELD_DATE);
		high = (double) dbObject.get(FIELD_HIGH);
		low = (double) dbObject.get(FIELD_LOW);
		open = (double) dbObject.get(FIELD_OPEN);
		close = (double) dbObject.get(FIELD_CLOSE);
		volume = (double) dbObject.get(FIELD_VOLUME);
		adjustedClose = (double) dbObject.get(FIELD_ADJUSTED_CLOSE);
		ticker = (String) dbObject.get(FIELD_TICKER);
	}

	public String getKey() {
		return getTicker() + "_" + getDate();
	}

	public static String createKey(String ticker, String date) {
		return ticker + "_" + date;
	}

}
