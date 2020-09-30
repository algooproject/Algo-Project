package com.algotrading.backtesting.replay;

import com.algotrading.DateUtil;
import com.algotrading.backtesting.config.AlgoConfiguration;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.util.Constants;
import com.algotrading.tickerservice.TickerServiceClient;
import com.sun.tools.internal.ws.processor.generator.GeneratorException;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;

import java.util.Comparator;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class DecendingValueComparator implements Comparator {
	Map map;
	public DecendingValueComparator(Map map) {
		this.map = map;
	}
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		return valueA.compareTo(valueB); // want orders with decending values
	}
}

public class ActivelyTradedStocks implements DynamicAvailableStocks {

	private static Map<String, Stock> allStocks = new LinkedHashMap<>();
	private static Map<String, Map<String, Double>> date_ticker_vol; // should keep it static?
	private static Map<String, List<String>> yearToTickerList;
	private static Boolean isYearToTickerListGenerated = false;
	private static String classFilePath = Constants.SRC_MAIN_RESOURCE_FILEPATH + "/ActivelyTradedStocksData/";
	private static Date defaultStartDate = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();

	private Map<String, AvailableStocks> map;
	private double threshold;
	private Boolean isAbsoluteTop;

	// constructors
	public ActivelyTradedStocks(){
		this(50, true);
	}
	public ActivelyTradedStocks(double threshold, Boolean isAbsoluteTop){
		this.threshold = threshold;
		this.isAbsoluteTop = isAbsoluteTop;
	}

	// functions to get ticker list
	private List<String> getListOfAllTickersFromFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(Constants.SRC_MAIN_RESOURCE_FILEPATH + "allStock.txt"));
		String line = null;
		List<String> tickerList = new ArrayList<>();
		while ((line = br.readLine()) != null)
		{
			if( !line.trim().isEmpty() && !line.startsWith( "#" ) )
				tickerList.add( line );
		}
		return tickerList;
	}
	private List<String> getListOfAllTickersFromMongoDB(){
		return new TickerServiceClient().getAllTickerStrings();
	}
	private List<String> getListOfAllTickers() throws IOException{
		return AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB) ?
				getListOfAllTickersFromMongoDB() : getListOfAllTickersFromFile();
	}

	private Stock constructStockFromTickerString( String ticker ){
		Stock stock = new Stock( ticker );
		if( AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB) )
			stock.readFromMongoDB();
		else
			stock.read( Constants.SRC_MAIN_RESOURCE_FILEPATH );
		return stock;
	}

	private static TreeMap<String, Double> sortMapByValue(TreeMap<String, Double> map){
		Comparator<String> comparator = new DecendingValueComparator(map);
		//TreeMap is a map sorted by its keys.classFilePath
		//The comparator is used to sort the TreeMap by keys.
		TreeMap<String, Double> result = new TreeMap<String, Double>(comparator);
		result.putAll(map);
		return result;
	}

	private void dumpDate_Ticker_VolumeToFiles() throws IOException {
		for( Map.Entry< String, Map< String, Double>> entry: date_ticker_vol.entrySet() ){
			String filename = classFilePath + entry.getKey() + ".txt";
			Set<String> stockList = entry.getValue().keySet(); // in the same order as the sorted map?
			BufferedWriter out = new BufferedWriter( new FileWriter( filename) ); // overwriting?
			for(String stock : stockList){
				out.write( stock );
				out.newLine();
			}
			out.close();
		}
	}

	private List<Path> getStockFilePathList(){
		List<Path> fileList = new ArrayList<>();
		File dir = new File(".");
		File[] files = dir.listFiles( new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name){
				return name.matches ( "\\d\\d\\d\\d\\d\\d\\d\\d\\.txt" );
			}
		});

		for( File file : files )
			fileList.add( file.toPath() );

		return fileList;
	}

	private String getNextYearInString( Calendar date ){
		return Integer.toString( date.get( Calendar.YEAR ) + 1 );
	}

	public void generateStockListsByDate() throws IOException{
		List<String> tickers = getListOfAllTickers();
		Date earliestDate = defaultStartDate;
		Date latestDate = new Date(); // present time
		for( String ticker : tickers ) {
			Stock stock = constructStockFromTickerString(ticker);
			allStocks.put(ticker, stock );
			Date comparingDate = stock.getEarliestDate();
			if( !comparingDate.equals( null ) && earliestDate.compareTo( comparingDate) > 0 )
				earliestDate = comparingDate;

			if( !comparingDate.equals( null ) && latestDate.compareTo( comparingDate) < 0 )
				latestDate = comparingDate;
		}
		Calendar current = Calendar.getInstance();
		current.setTime( earliestDate );
		Calendar latest = Calendar.getInstance();
		latest.setTime( latestDate );

		String yearString = getNextYearInString( current ) + "0101";
		String lastYearString;
		date_ticker_vol = new TreeMap<String, Map<String, Double>>();
		yearToTickerList = new HashMap<String, List<String>>();
		TreeMap<String, Double> ticker_vol = new TreeMap<String, Double>();
		while( current.compareTo( latest ) <= 0 ){
			lastYearString = yearString;
			for(Map.Entry<String, Stock> entry : allStocks.entrySet()){
				if( !ticker_vol.containsKey( entry.getKey() ) )
					ticker_vol.put( entry.getKey(), entry.getValue().getHistory().get(current.getTime()).getVolume() );
				else {
					Double currentVol = ticker_vol.get(entry.getKey());
					ticker_vol.replace(entry.getKey(),
							currentVol + entry.getValue().getHistory().get(current.getTime()).getVolume());
				}
			}
			current.roll( Calendar.DAY_OF_MONTH, true );
			yearString = getNextYearInString( current ) + "0101";
			if( lastYearString.equals( yearString ) ){
				date_ticker_vol.put( lastYearString, sortMapByValue( ticker_vol ) );
				yearToTickerList.put( lastYearString,
						new ArrayList<>( date_ticker_vol.get(lastYearString).keySet()) ); // order maintained?
				ticker_vol = new TreeMap<String, Double>();
			}
		}
		dumpDate_Ticker_VolumeToFiles();
		isYearToTickerListGenerated = true;
	}

	private int getNumberOfStocks( int total ){
		if( isAbsoluteTop )
			return Math.min( (int) threshold, total );
		else
			return (int) (total * threshold / 100);
	}

	private void constructYearToTickerListFromFiles() throws IOException, ParseException {
		if( isYearToTickerListGenerated )
			return;
		List<Path> fileList = getStockFilePathList();
		Charset charset = Charset.defaultCharset();
		yearToTickerList = new HashMap<String, List<String>>();
		for( Path file : fileList )
			yearToTickerList.put( file.toFile().getName().substring(0,7),
					Files.readAllLines(file, charset) );
		isYearToTickerListGenerated = true;
	}

	public void read(String filePath, String fileName) throws IOException, ParseException {
		constructYearToTickerListFromFiles();
		map = new TreeMap<>();
		for( Map.Entry<String, List<String>> entry : yearToTickerList.entrySet() ){
			int numberOfStocks = getNumberOfStocks( entry.getValue().size() );
			if( numberOfStocks == 0 )
				throw new GeneratorException( "Generating available stock error: " +
						entry.getKey() +
						".txt does not provide sufficient number of stocks" ); // what exception is appropriate?
			AvailableStocks stocks = new AvailableStocks();
			for( int i=0; i < numberOfStocks; i++ ) {
				Stock stock = new Stock(entry.getValue().get(i));
				stocks.add( stock );
				allStocks.put( entry.getValue().get(i), stock );
			}
			map.put( entry.getKey(), stocks ); // should use regex?
		}
	}

	@Override
	public AvailableStocks get(Date date) {
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(date);
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

	public static void main(String[] args) throws IOException, ParseException {
		ActivelyTradedStocks a = new ActivelyTradedStocks();
		// System.out.println(a.toString());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = sdf.parse("20200321");
		// System.out.println(a.get(date));

		Date date2 = sdf.parse("20190101");
		// System.out.println(a.get(date2));
	}

}
