package com.algotrading.persistence.mongo.dbobject;

import java.util.function.Supplier;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class StockInfo implements DBObjectable {

	public static final String NAME = "stockInfo";

	public StockInfo() {

	}

	private static final String FIELD_TICKER = "ticker";
	private static final String FIELD_DATE = "date";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_EXCHANGE = "exchange";
	private static final String FIELD_INDUSTRY = "industry";
	private static final String FIELD_ISSUED_SHARES = "issuedShares";
	private static final String FIELD_LOT_SIZE = "lotSize";
	private static final String FIELD_EARNING = "earning";
	private static final String FIELD_DIVEDEND = "dividend";

	private String ticker;
	private String date; // yyyy-MM-dd
	private String name;
	private String exchange;
	private String industry;
	private int issuedShares;
	private int lotSize;
	private double earning;
	private double dividend;

	public StockInfo(String ticker, String date, String name, String exchange, String industry, int issuedShares,
			int lotSize, double earning, double dividend) {
		super();
		this.ticker = ticker;
		this.date = date;
		this.name = name;
		this.exchange = exchange;
		this.industry = industry;
		this.issuedShares = issuedShares;
		this.lotSize = lotSize;
		this.earning = earning;
		this.dividend = dividend;
	}

	public String getTicker() {
		return ticker;
	}

	public String getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public String getExchange() {
		return exchange;
	}

	public String getIndustry() {
		return industry;
	}

	public int getIssuedShares() {
		return issuedShares;
	}

	public int getLotSize() {
		return lotSize;
	}

	public double getEarning() {
		return earning;
	}

	public double getDividend() {
		return dividend;
	}

	@Override
	public String toString() {
		return "StockInfo [ticker=" + ticker + ", date=" + date + ", name=" + name + ", exchange=" + exchange
				+ ", industry=" + industry + ", issuedShares=" + issuedShares + ", lotSize=" + lotSize + ", earning="
				+ earning + ", dividend=" + dividend + "]";
	}

	@Override
	public DBObject toDBObject() {
		return new BasicDBObject("_id", getKey()).append(FIELD_DATE, getDate())
				.append(FIELD_TICKER, getTicker())
				.append(FIELD_NAME, getName())
				.append(FIELD_EXCHANGE, getExchange())
				.append(FIELD_INDUSTRY, getIndustry())
				.append(FIELD_ISSUED_SHARES, getIssuedShares())
				.append(FIELD_LOT_SIZE, getLotSize())
				.append(FIELD_EARNING, getEarning())
				.append(FIELD_DIVEDEND, getDividend());
	}

	@Override
	public void fromDBObject(DBObject dbObject) {
		ticker = (String) dbObject.get(FIELD_TICKER);
		date = (String) dbObject.get(FIELD_DATE);
		name = (String) dbObject.get(FIELD_NAME);
		exchange = (String) dbObject.get(FIELD_EXCHANGE);
		industry = (String) dbObject.get(FIELD_INDUSTRY);
		issuedShares = (int) dbObject.get(FIELD_ISSUED_SHARES);
		lotSize = (int) dbObject.get(FIELD_LOT_SIZE);
		earning = (double) dbObject.get(FIELD_EARNING);
		dividend = (double) dbObject.get(FIELD_DIVEDEND);
	}

	@Override
	public String getKey() {
		return getTicker();
	}

	@Override
	public String getCollectionName() {
		return NAME;
	}

	@Override
	public Supplier<? extends DBObjectable> getSupplier() {
		return StockInfo::new;
	}

}
