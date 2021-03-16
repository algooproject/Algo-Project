package com.algotrading.backtesting.replay;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.tickerservice.Ticker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * This class sed as an extension of AvailableStocks, similar to
 * AvailableStocksWithYearChange, returning a new list of
 * available stocks on 1 Jan of every year.
 * However, it is not returning the
 * entire list in the read file. Instead, it returns the top n stocks
 * or top r% of the list, depending on the parameters
 * passed in the constructor.
 *
 */
public class ActivelyTradedStocks implements DynamicAvailableStocks {

	private final Map<String, Stock> allStocks = new LinkedHashMap<>();
//	private static String classFilePath = Constants.SRC_MAIN_RESOURCE_FILEPATH + "/ActivelyTradedStocksData/";

	private final ActiveTradedStocksReader activeTradedStocksReader;
	private Map<String, AvailableStocks> map;
	private final double threshold;
	private final Boolean isAbsoluteTop;

	// constructors
	public ActivelyTradedStocks(ActiveTradedStocksReader activeTradedStocksReader) throws IOException {
		this(activeTradedStocksReader, 50, true);
	}
	public ActivelyTradedStocks(ActiveTradedStocksReader activeTradedStocksReader, double threshold, Boolean isAbsoluteTop) throws IOException {
	    this.activeTradedStocksReader = activeTradedStocksReader;
		this.threshold = threshold;
		this.isAbsoluteTop = isAbsoluteTop;
		read();
	}

	private static TreeMap<String, Double> sortMapByValue(TreeMap<String, Double> map){
		//TreeMap is a map sorted by its keys.classFilePath
		//The comparator is used to sort the TreeMap by keys.
		TreeMap<String, Double> result = new TreeMap<>((keyA, keyB) ->  {
			Double valueA = map.get(keyA);
			Double valueB = map.get(keyB);
			return valueA.compareTo(valueB); // want orders with descending values
		});
		result.putAll(map);
		return result;
	}

	private int getNumberOfStocks( int total ){
		if( isAbsoluteTop ) {
			return Math.min((int) threshold, total);
		} else {
			return (int) (total * threshold / 100);
		}
	}

	private void read() throws IOException {
        Map<String, List<String>> yearToTickerList = activeTradedStocksReader.constructYearToTickerListFromFiles();
		map = new TreeMap<>();
		for( Map.Entry<String, List<String>> entry : yearToTickerList.entrySet() ){
			int numberOfStocks = getNumberOfStocks( entry.getValue().size() );
			if( numberOfStocks == 0 ) {
				throw new RuntimeException("Generating available stock error: " +
						entry.getKey() +
						".txt does not provide sufficient number of stocks");
			}
			AvailableStocks stocks = new AvailableStocks();
			for( int i=0; i < numberOfStocks; i++ ) {
				Stock stock = new Stock(entry.getValue().get(i));
				stocks.add( stock );
				allStocks.put( entry.getValue().get(i), stock );
			}
			map.put( entry.getKey(), stocks );
		}
	}

	@Override
	public AvailableStocks get(Date date) {
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(date);
		System.out.println("dateStr " + dateStr);
		System.out.println("map " + map);
		List<String> list = new ArrayList<>(map.keySet());
		Collections.reverse(list);
		for (String d : list) {
			if (dateStr.compareTo(d) > 0)
				return map.get(d);
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

	public static void main(String[] args) throws IOException {
		// see if that comparator can sort tickers by date
		List<Ticker> tickers = new ArrayList<>();
		Ticker a = new Ticker();
		a.ticker = "A";
		a.date = "20200411";
		tickers.add(a);
		Ticker b = new Ticker();
		b.ticker = "B";
		b.date = "20200402";
		tickers.add(b);
		tickers.sort(Comparator.comparing(ticker -> ticker.date));
		for( Ticker ticker: tickers ){
			System.out.println( ticker );
		}

		// see if keySet preserves order
		TreeMap<String, Double> map = new TreeMap<>();
		map.put( "A", 5.0);
		map.put( "B", 3.0);
		map.put( "C", 4.0 );
		TreeMap<String, Double> sorted = sortMapByValue(map);
		List<String> mapKey = new ArrayList<>( sorted.keySet());
		for( String key: mapKey ){
			System.out.println( key );
		}

		File dir = new File(Constants.SRC_MAIN_RESOURCE_FILEPATH + "/ActivelyTradedStocksData/");
		File[] files = dir.listFiles((dir1, name) -> name.matches("\\d\\d\\d\\d\\d\\d\\d\\d\\.txt"));
		for (File file : files) {
			System.out.println(file.toPath());
			System.out.println( file.toPath().toFile().getName().substring(0,7) );
		}

		String filename = Constants.SRC_MAIN_RESOURCE_FILEPATH + "/ActivelyTradedStocksData/testing.txt";
		BufferedWriter out = new BufferedWriter( new FileWriter( filename) );
		out.write( "Hello Milton" );
		out.close();
	}

}
