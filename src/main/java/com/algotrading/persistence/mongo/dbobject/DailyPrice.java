package com.algotrading.persistence.mongo.dbobject;

import java.util.function.Supplier;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DailyPrice implements DBObjectable {

	public static final String NAME = "dailyPrice";

	private static final String FIELD_TICKER = "ticker";
	private static final String FIELD_ADJUSTED_CLOSE = "adjustedClose";
	private static final String FIELD_VOLUME = "volume";
	private static final String FIELD_CLOSE = "close";
	private static final String FIELD_OPEN = "open";
	private static final String FIELD_LOW = "low";
	private static final String FIELD_HIGH = "high";
	private static final String FIELD_DATE = "date";
	private static final String FIELD_CHANGE = "change";
	private static final String FIELD_TURNOVER = "turnover";

	private String ticker;
	private String date; // yyyy-MM-dd
	private double high;
	private double low;
	private double open;
	private double close;
	private double volume;
	private double adjustedClose;
	private double change;
	private double turnover;

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

	public double getChange() {
		return change;
	}

	public double getTurnover() {
		return turnover;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public void setAdjustedClose(double adjustedClose) {
		this.adjustedClose = adjustedClose;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public void setTurnover(double turnover) {
		this.turnover = turnover;
	}

	@Override
	public String toString() {
		return "DailyPrice [ticker=" + ticker + ", date=" + date + ", high=" + high + ", low=" + low + ", open=" + open
				+ ", close=" + close + ", volume=" + volume + ", adjustedClose=" + adjustedClose + ", change=" + change
				+ ", turnover=" + turnover + "]";
	}

	@Override
	public DBObject toDBObject() {
		return new BasicDBObject("_id", getKey()).append(FIELD_DATE, getDate())
				.append(FIELD_HIGH, getHigh())
				.append(FIELD_LOW, getLow())
				.append(FIELD_OPEN, getOpen())
				.append(FIELD_CLOSE, getClose())
				.append(FIELD_VOLUME, getVolume())
				.append(FIELD_ADJUSTED_CLOSE, getAdjustedClose())
				.append(FIELD_TICKER, getTicker())
				.append(FIELD_CHANGE, getChange())
				.append(FIELD_TURNOVER, getTurnover());
	}

	@Override
	public void fromDBObject(DBObject dbObject) {
		date = (String) dbObject.get(FIELD_DATE);
		high = (double) dbObject.get(FIELD_HIGH);
		low = (double) dbObject.get(FIELD_LOW);
		open = (double) dbObject.get(FIELD_OPEN);
		close = (double) dbObject.get(FIELD_CLOSE);
		volume = (double) dbObject.get(FIELD_VOLUME);
		adjustedClose = (double) dbObject.get(FIELD_ADJUSTED_CLOSE);
		ticker = (String) dbObject.get(FIELD_TICKER);
		change = (double) dbObject.get(FIELD_CHANGE);
		turnover = (double) dbObject.get(FIELD_TURNOVER);
	}

	// @Override
	// public String getKey() {
	// return getTicker() + "_" + getDate();
	// }

	public static String createKey(String ticker, String date) {
		return ticker + "_" + date;
	}

	@Override
	public String getCollectionName() {
		return NAME;
	}

	@Override
	public Supplier<? extends DBObjectable> getSupplier() {
		return DailyPrice::new;
	}

}
