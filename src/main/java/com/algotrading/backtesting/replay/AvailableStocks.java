package com.algotrading.backtesting.replay;

import com.algotrading.backtesting.config.AlgoConfiguration;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.StockCreationException;
import com.algotrading.backtesting.util.TickerFileProvider;
import com.algotrading.backtesting.util.TickerMongoAvailableStocksProvider;
import com.algotrading.backtesting.util.TickerProvider;
import com.algotrading.tickerservice.TickerServiceClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailableStocks {

	private Map<String, Stock> stocks;

	private boolean fileNameIsDate;

	public AvailableStocks(String filePath, String stockListName, boolean fileNameIsDate) throws Exception {
		this.fileNameIsDate = fileNameIsDate;
		read(filePath, stockListName);
	}

	public AvailableStocks() {
		stocks = new HashMap<>();
	}

	public void read(String filePath, String stockListFileName) throws Exception {

		TickerProvider tickerProvider = AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB)
				? new TickerMongoAvailableStocksProvider(stockListFileName, fileNameIsDate, new TickerServiceClient()) // mongodb need to call tickerServiceClient.findAvailableStockByGroup or tickerServiceClient.findAvailableStockByGroupAndDate
				: new TickerFileProvider(filePath, stockListFileName + ".txt");

		List<String> allStockCodes = tickerProvider.getAllTickers();

		stocks = new HashMap<>();

		for (String stockCode : allStockCodes) {
			try {
				Stock stock = tickerProvider.constructStockWithLotSizeFromTickerString(stockCode);
				add(stock);
			} catch (StockCreationException e) {
				System.err.println("Cannot add " + stockCode + " to available stock");
				e.printStackTrace();
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