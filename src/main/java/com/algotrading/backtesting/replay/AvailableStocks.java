package com.algotrading.backtesting.replay;

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

import com.algotrading.backtesting.stock.Stock;

public class AvailableStocks {

	private Map <String, Stock> stocks;

	public AvailableStocks(String fileList) throws IOException, ParseException {
		read(fileList);
	}

	public AvailableStocks(String filePath, String fileName) throws IOException, ParseException {
		read(filePath, fileName);
	}	
	
	public AvailableStocks() {
		stocks = new HashMap<>();
	}

	public void read(String filePath) throws IOException, ParseException {
		Path file = new File(filePath).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(file, charset);
		stocks = new HashMap<>();
		for (String line : stringList) {
			Stock stock = new Stock(line);
			stock.read();
			add(stock);
		}
	}

	public void read(String filePath, String fileName) throws IOException, ParseException {
		Path file = new File(filePath + fileName).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(file, charset);
		stocks = new HashMap<>();
		for (String line : stringList) {
			Stock stock = new Stock(line);
			stock.read(filePath);
			add(stock);
		}
	}

	public List<Stock> get() {
		return new ArrayList<Stock>(stocks.values());
	}
	
	public Stock get(String ticker)
	{
		return stocks.get(ticker);
	}

	public void add(Stock stock) {
		stocks.put(stock.getTicker(), stock);
	}

}