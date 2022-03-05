package com.algotrading.backtesting.replay;

import com.algotrading.backtesting.config.AlgoConfiguration;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.TickerFileProvider;
import com.algotrading.backtesting.util.TickerMongoAvailableStocksProvider;
import com.algotrading.backtesting.util.TickerProvider;
import com.algotrading.tickerservice.TickerServiceClient;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailableStocks {

	private Map<String, Stock> stocks;

	private boolean fileNameIsDate;

	public AvailableStocks(String filePath, String stockListName, boolean fileNameIsDate) throws IOException, ParseException {
		this.fileNameIsDate = fileNameIsDate;
		read(filePath, stockListName);
	}

	public AvailableStocks() {
		stocks = new HashMap<>();
	}

	public void read(String filePath, String stockListFileName) throws IOException, ParseException {

		TickerProvider tickerProvider = AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB)
				? new TickerMongoAvailableStocksProvider(stockListFileName, fileNameIsDate, new TickerServiceClient()) // mongodb need to call tickerServiceClient.findAvailableStockByGroup or tickerServiceClient.findAvailableStockByGroupAndDate
				: new TickerFileProvider(filePath, stockListFileName + ".txt");

		List<String> stringList = tickerProvider.getAllTickers();

		stocks = new HashMap<>();
		LotSize lotSize = new LotSize(filePath + "lotSize.csv");

		for (String line : stringList) {
			Stock stock = new Stock(line, lotSize.getLotSize(line));

			boolean hasTickerHistory = tickerProvider.fillStockHistory(stock);
			if (hasTickerHistory) {
				add(stock);
			}
		}
	}

	public List<Stock> get() {
		return new ArrayList<>(stocks.values());
	}

	public Stock get(String ticker) {
		return stocks.get(ticker);
	}

	public void add(Stock stock) {
		stocks.put(stock.getTicker(), stock);
	}

	public Map<String, Stock> getAllAvailableStocks() {
		return stocks;
	}

	@Override
	public String toString() {
		return stocks.values().toString();
	}
}