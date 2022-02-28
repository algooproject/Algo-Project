package com.algotrading.backtesting.replay;

import com.algotrading.DateUtil;
import com.algotrading.backtesting.config.AlgoConfiguration;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.TickerFileProvider;
import com.algotrading.backtesting.util.TickerMongoProvider;
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

	public AvailableStocks(String filePath, String fileName, boolean fileNameIsDate) throws IOException, ParseException {
		this.fileNameIsDate = fileNameIsDate;
		read(filePath, fileName);
	}

	public AvailableStocks() {
		stocks = new HashMap<>();
	}

	public void read(String filePath, String fileName) throws IOException, ParseException {

		TickerProvider tickerProvider = AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB)
				? new TickerMongoProvider(new TickerServiceClient())
				: new TickerFileProvider(filePath, fileName);

		// cannot use ist<String> stringList = tickerProvider.getAllTickers(); yet, as the api is different in mongo db
		// mongodb need to call tickerServiceClient.findAvailableStockByGroup or tickerServiceClient.findAvailableStockByGroupAndDate
		// where tickerProvider.getAllTickers() is calling tickerServiceClient.getAllTickerStrings()
		// TODO maybe need to pass some custom function in MongoTickerProvider to query a collection of stocks
		// maybe even change the ticker service api implementation to fulfill this
		List<String> stringList = AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB)
				? getAvailableStockFromMongoDb(fileName)
				: tickerProvider.getAllTickers();

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

	private List<String> getAvailableStockFromMongoDb(String fileName) {
		TickerServiceClient client = new TickerServiceClient();
		return fileNameIsDate
		? client.findAvailableStockByGroupAndDate(AlgoConfiguration.getAvailableStockGroup(), DateUtil.yyyymmddToYYYY_MM_DD(fileName.replace(".txt", "")))
		: client.findAvailableStockByGroup(fileName.replace(".txt", ""));
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