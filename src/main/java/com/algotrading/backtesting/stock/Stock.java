package com.algotrading.backtesting.stock;

import com.algotrading.backtesting.util.Constants;
import com.algotrading.tickerservice.Ticker;
import com.algotrading.tickerservice.TickerServiceClient;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

	public Stock(String ticker, int lotSize) {
		this(ticker, new TreeMap<Date, StockHistory>(), lotSize);
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

	public boolean resourcesExist() {
		return getClass().getResourceAsStream("/yahoostockdata/" + this.ticker + ".csv") != null;
	}

	public void read(boolean withHeader) {
		readFromStream(getClass().getResourceAsStream("/yahoostockdata/" + this.ticker + ".csv"), withHeader);
	}


	// TODO remove filePath arg if we are using getResourceAsStream
	public void read(String filePath, boolean withHeader) {
		// TODO read files from ticker
		String strCsvFile = filePath + this.ticker + ".csv";
		try {
			readFromStream(new FileInputStream(new File(strCsvFile)), withHeader);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void readFromStream(InputStream inputStream, boolean withHeader) {
		String strLine = "";
		String strCvsSplitBy = ",";
		boolean isFirstLine = withHeader; // will skip header if true
		Double dbLastClose = null;
		Double dbLastAdjClose = null;

		// read file from yahoo-stock-downloader repo
		try {
			List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
			for (int i = (isFirstLine ? 1 : 0); i < lines.size(); i++) {
				strLine = lines.get(i);
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
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
