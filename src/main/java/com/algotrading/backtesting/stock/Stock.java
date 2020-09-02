package com.algotrading.backtesting.stock;

import com.algotrading.backtesting.config.AlgoConfiguration;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.tickerservice.Ticker;
import com.algotrading.tickerservice.TickerServiceClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Stock {

	// private static String FILEPATH = "src/main/resources/";
	private final String ticker;
	private final Map<Date, StockHistory> history;
	private int lotSize; // default to be 1 if not specified when instantiated;
	private Boolean status = true;

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
		this(ticker, new TreeMap<Date, StockHistory>(), 1);
	}

	// called by available stocks
	public Stock(String ticker, int lotSize) {
		this(ticker, new TreeMap<>(), lotSize);
	}

	public static Stock createStockWithData(String ticker, int lotSize) {
		Stock stock = new Stock(ticker, lotSize);
		boolean isUsingMongoDb = AlgoConfiguration.FROM_MONGODB.equals(AlgoConfiguration.getReadStockFrom());
		if (isUsingMongoDb) {
			boolean hasTickerHistory = stock.readFromMongoDB();
			if (!hasTickerHistory) {
				return null;
			}
//			if (hasTickerHistory) {
//				add(stock);
//			}
		} else {
			File tempFile = new File(AlgoConfiguration.STOCK_READ_PATH_IF_READ_FROM_FILE + stock.getTicker() + ".csv");
			boolean exists = tempFile.exists();
			// System.out.println(filePath + stock.getTicker() + ".csv");
			// System.out.println(exists);
//			if (exists) {
//				stock.read(filePath);
//				add(stock);
//			}
			if (!exists) {
				return null;
			}
		}
		return stock;
	}

	public Stock(String ticker, Map<Date, StockHistory> history, int lotSize) {
		this.ticker = ticker;
		this.history = history;
		this.lotSize = lotSize;
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

//	Stock stock = new MockStock();
//
//	class MockStock() {
//
//		@Override
//		public boolean readFromMongoDB() {
//
//			// non db code for testing
//		}
//	}

	/** return if has stock record in mongodb */
	public boolean readFromMongoDB() {
		List<Ticker> tickers = new TickerServiceClient().findTickerByCode(this.ticker);
		if (tickers.size() == 0) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Ticker ticker : tickers) {
			try {
				Date date = sdf.parse(ticker.date);
				history.put(date, new StockHistory(date, ticker.open, ticker.close, ticker.high, ticker.low, ticker.adjClose, ticker.volume));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		initialDate();
		return true;
	}

	public void read(String filePath) {
		// read(filePath, false);
		read(filePath, true);
	}

	public void read() {
		// read(false);
		read(true);
	}

	public void read(boolean withHeader) {
		read(Constants.SRC_MAIN_RESOURCE_FILEPATH, withHeader);
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
