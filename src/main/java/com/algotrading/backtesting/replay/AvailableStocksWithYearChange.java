package com.algotrading.backtesting.replay;

import com.algotrading.DateUtil;
import com.algotrading.backtesting.config.AlgoConfiguration;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.tickerservice.TickerServiceClient;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AvailableStocksWithYearChange implements DynamicAvailableStocks {

	/** true = set to read specific path when reading stock, but not from resourceAsStream */
	private boolean isCustomizedStockFilePath = false;

	private Map<String, AvailableStocks> map;

	private Map<String, Stock> allStocks = new LinkedHashMap<>();

	private String filePath;

	private String fileName;

	public AvailableStocksWithYearChange(String filePath, String fileName) throws IOException, ParseException {
		this.filePath = filePath;
		this.fileName = fileName;
//		read(filePath, fileName);
	}

	@Override
	public void load() throws IOException, ParseException
	{
		read(filePath, fileName);
	}

	public void read(String filePath, String fileName) throws IOException, ParseException {
		Path file = new File(filePath + fileName).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB)
				? new TickerServiceClient().findAvailableStockDateByGroup(AlgoConfiguration.getAvailableStockGroup()).stream().map(date -> DateUtil.yyyy_mm_ddToYYYYMMDD(date)).collect(Collectors.toList())
				: Files.readAllLines(file, charset);
		map = new TreeMap<>();
		for (String line : stringList) {
			// System.out.println("###" + line);
			AvailableStocks availableStocks = new AvailableStocks(filePath, line + ".txt", true);
			if (isCustomizedStockFilePath) {
				availableStocks.customizedStockFilePath();
			}
			availableStocks.load();
			map.put(line, availableStocks);
			for (Stock s : availableStocks.get()) {
				allStocks.put(s.getTicker(), s);
			}
		}
	}

	@Override
	public AvailableStocks get(Date date) {
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(date);
		List<String> list = new ArrayList<>(map.keySet());
		Collections.reverse(list);
		for (String d : list) {
			if (dateStr.compareTo(d) > 0) {
				// System.out.println("Getting: " + d + " from " + dateStr);
				return map.get(d);
			}
		}
		return null;
	}

	@Override
	public Map<String, Stock> getAllAvailableStocks() {
		return allStocks;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String str : map.keySet()) {
			sb.append(str + ", " + map.get(str)).append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) throws IOException, ParseException {
		AvailableStocksWithYearChange a = new AvailableStocksWithYearChange(Constants.SRC_MAIN_RESOURCE_FILEPATH,
				"availablestocksdate.txt");
		// System.out.println(a.toString());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = sdf.parse("20200321");
		// System.out.println(a.get(date));

		Date date2 = sdf.parse("20190101");
		// System.out.println(a.get(date2));
	}

	@Override
	public void customizedStockFilePath() {
		isCustomizedStockFilePath = true;
	}

}
