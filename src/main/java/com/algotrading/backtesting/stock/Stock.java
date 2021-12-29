package com.algotrading.backtesting.stock;

import com.algotrading.backtesting.stock.io.StockMongoDBGateway;
import com.algotrading.backtesting.util.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
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

	public void read(String filePath) {
		// read(filePath, false);
		read(filePath, true);
	}

	public void read(String filePath, boolean withHeader) {
		// TODO read files from ticker
		String strCsvFile = filePath + this.ticker + ".csv";
		String strLine = "";
		String strCvsSplitBy = ",";
		boolean isFirstLine = withHeader; // will skip header if true
		Double dbLastClose = null;
		Double dbLastAdjClose = null;

		try (BufferedReader br = new BufferedReader(new FileReader(strCsvFile))) {
			while ((strLine = br.readLine()) != null) {
				// System.out.println(strLine);
				if (isFirstLine) {
					isFirstLine = false;
				} else {
					// use comma as separator
					String[] strStockHistory = strLine.split(strCvsSplitBy);
					// System.out.println(strStockHistory[0] + '|' +
					// strStockHistory[1] + '|' + strStockHistory[2] + '|' +
					// strStockHistory[3] + '|' + strStockHistory[4] + '|' +
					// strStockHistory[5] + '|' + strStockHistory[6]);
					Date dtStockHistoryDate = Constants.DATE_FORMAT_YYYYMMDD.parse(strStockHistory[0]);
					Double dbOpen = null;
					Double dbClose = null;
					Double dbHigh = null;
					Double dbLow = null;
					Double dbAdjClose = null;
					Double dbVolume = null;
					if (strStockHistory[1].equals("null")) {
						dbOpen = dbLastClose;
						dbClose = dbLastClose;
						dbHigh = dbLastClose;
						dbLow = dbLastClose;
						dbAdjClose = dbLastAdjClose;
						dbVolume = 0.0;
					} else {
						dbOpen = Double.parseDouble(strStockHistory[1]);
						dbClose = Double.parseDouble(strStockHistory[4]);
						dbHigh = Double.parseDouble(strStockHistory[2]);
						dbLow = Double.parseDouble(strStockHistory[3]);
						dbAdjClose = Double.parseDouble(strStockHistory[5]);
						dbVolume = Double.parseDouble(strStockHistory[6]);
						dbLastClose = dbClose;
						dbLastAdjClose = dbAdjClose;
					}
					history.put(dtStockHistoryDate,
							new StockHistory(dtStockHistoryDate, dbOpen, dbClose, dbHigh, dbLow, dbAdjClose, dbVolume));
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		initialDate();
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
