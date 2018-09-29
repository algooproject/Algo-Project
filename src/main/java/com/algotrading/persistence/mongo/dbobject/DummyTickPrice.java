package com.algotrading.persistence.mongo.dbobject;

import java.util.Date;
import java.util.function.Supplier;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DummyTickPrice implements DBObjectable {

	public static final String NAME = "dummyTickPrice";

	private static final String FIELD_TICKER = "ticker";
	private static final String FIELD_DATE = "date";
	private static final String FIELD_PRICE = "price";

	private Date date;
	private String ticker;
	private double price;

	public DummyTickPrice() {

	}

	public DummyTickPrice(Date date, String ticker, double price) {
		this.date = date;
		this.ticker = ticker;
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public String getTicker() {
		return ticker;
	}

	public double getPrice() {
		return price;
	}

	@Override
	public DBObject toDBObject() {
		return new BasicDBObject("_id", getKey()).append(FIELD_DATE, getDate())
				.append(FIELD_PRICE, getPrice())
				.append(FIELD_TICKER, getTicker());
	}

	@Override
	public void fromDBObject(DBObject dbObject) {
		date = (Date) dbObject.get(FIELD_DATE);
		ticker = (String) dbObject.get(FIELD_TICKER);
		price = (double) dbObject.get(FIELD_PRICE);
	}

	@Override
	public String toString() {
		return "TickPrice [date=" + date + ", ticker=" + ticker + ", price=" + price + "]";
	}

	@Override
	public String getKey() {
		return getTicker() + "_" + getDate();
	}

	@Override
	public String getCollectionName() {
		return NAME;
	}

	@Override
	public Supplier<? extends DBObjectable> getSupplier() {
		return DummyTickPrice::new;
	}

}
