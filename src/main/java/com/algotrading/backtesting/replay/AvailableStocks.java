package com.algotrading.backtesting.replay;

import com.algotrading.DateUtil;
import com.algotrading.backtesting.config.AlgoConfiguration;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.tickerservice.TickerServiceClient;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
		Path file = new File(filePath + fileName).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB)
				? getAvailableStockFromMongoDb(fileName)
				: Files.readAllLines(file, charset);
		stocks = new HashMap<>();
		LotSize lotSize = new LotSize(filePath + "lotSize.csv");
		boolean isUsingMongoDb = AlgoConfiguration.FROM_MONGODB.equals(AlgoConfiguration.getReadStockFrom());
		for (String line : stringList) {
			// System.out.println(line);
			Stock stock = new Stock(line, lotSize.getLotSize(line));
			// System.out.println("Reading " + stock.getTicker());

			if (isUsingMongoDb) {
				boolean hasTickerHistory = stock.readFromMongoDB();
			 	if (hasTickerHistory) {
			 		add(stock);
				}
			} else {
				File tempFile = new File(filePath + stock.getTicker() + ".csv");
				boolean exists = tempFile.exists();
				// System.out.println(filePath + stock.getTicker() + ".csv");
				// System.out.println(exists);
				if (exists) {
					stock.readFromFile(filePath);
					add(stock);
				}
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
		return new ArrayList<Stock>(stocks.values());
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