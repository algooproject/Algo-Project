package com.algotrading.persistence.mongo.dbobject;

import java.util.Date;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TickPrice implements DBObjectable {

	private Date date;
	private String ticker;
	private double price;

	public TickPrice(Date date, String ticker, double price) {
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
		return new BasicDBObject("_id", UUID.randomUUID()
				.toString()).append("date", getDate())
						.append("price", getPrice())
						.append("ticker", getTicker());
	}

	@Override
	public String toString() {
		return "TickPrice [date=" + date + ", ticker=" + ticker + ", price=" + price + "]";
	}

}
