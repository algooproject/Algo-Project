package com.algotrading.backtesting.stock;

import com.algotrading.backtesting.stock.io.StockFileGateway;
import com.algotrading.backtesting.stock.io.StockMongoDBGateway;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Stock {

	// private static String FILEPATH = "src/main/resources/";
	private final String ticker;
	private final Map<Date, StockHistory> history;
	private int lotSize; // default to be 1 if not specified when instantiated;
	private Boolean status = true;
	private Date earliestDate = new Date();
	private Date latestDate = new Date();

	Map<Integer, Date> pointerDate = new HashMap<>();
	Map<Date, Integer> datePointer = new HashMap<>();

	public void initialDate() {
		int i = 1;
		for (Map.Entry<Date, StockHistory> entry : getHistory().entrySet()) {
			pointerDate.put(i, entry.getKey());
			datePointer.put(entry.getKey(), i);
			i++;
		}

	}

	@Deprecated
	public Stock(String ticker, Map<Date, StockHistory> history) {
		this(ticker, history, 1);
	}

	public Stock(String ticker) {
		this(ticker, new TreeMap<>(), 1);
	}

	public Stock(String ticker, int lotSize) {
		this(ticker, new TreeMap<>(), lotSize);
	}

	public Stock(String ticker, Map<Date, StockHistory> history, int lotSize) {
		this.ticker = ticker;
		this.history = history;
		this.lotSize = lotSize;

		Boolean datesInit = false;
		for( Date date: history.keySet() ){
			if( datesInit ){
				if( earliestDate.compareTo( date ) > 0 )
					earliestDate = date;
				if( latestDate.compareTo( date ) < 0 )
					latestDate = date;
			}else{
				datesInit = true;
				earliestDate = date;
				latestDate = date;
			}
		}
	}

	public Date getEarliestDate(){ return earliestDate; }
	public Date getLatestestDate(){ return latestDate; }

	public void setEarliestDate(Date earliestDate) {
		this.earliestDate = earliestDate;
	}

	public void setLatestestDate(Date latestDate) {
		this.latestDate = latestDate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public void readLotSize() {
		// TODO
	}

	/** return if has stock record in mongodb */
	public boolean readFromMongoDB() {
		// TODO further refactor and extract to this enrichment method outside stock class
		return new StockMongoDBGateway().fillData(this);
	}

	public void readFromFile(String filePath) {
		// TODO further refactor and extract to this enrichment method outside stock class
		new StockFileGateway(filePath).fillData(this);
	}

	public Map<Integer, Date> getPointerDate() {
		return pointerDate;
	}

	public Map<Date, Integer> getDatePointer() {
		return datePointer;
	}

	public String getTicker() {
		return ticker;
	}

	public Map<Date, StockHistory> getHistory() {
		return history;
	}

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	@Override
	public String toString() {
		return ticker;
	}

}
