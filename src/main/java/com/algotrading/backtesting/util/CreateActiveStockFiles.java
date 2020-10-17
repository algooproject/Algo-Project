package com.algotrading.backtesting.util;

import com.algotrading.backtesting.config.AlgoConfiguration;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.tickerservice.TickerServiceClient;

import java.io.*;
import java.util.*;

/**
 * this class is used to produce files in each of which it lists the stocks
 * from most active to least in the previous year.
 * Those files are named in the format YYYY0101.txt and placed
 * in the sub folder "/ActivelyTradedStocksData/"'.
 * Here YYYY is the year of the corresponding list
 */
public class CreateActiveStockFiles {
    // can be made local in generateStockListsByDate()
    private static Map<String, Stock> allStocks = new LinkedHashMap<>();
    private static Map<String, List<String>> yearToTickerList;
    private static String classFilePath = Constants.SRC_MAIN_RESOURCE_FILEPATH + "/ActivelyTradedStocksData/";
    private static Date defaultStartDate = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();

    // functions to get ticker list
    private List<String> getListOfAllTickersFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Constants.SRC_MAIN_RESOURCE_FILEPATH + "allStock.txt"));
        String line;
        List<String> tickerList = new ArrayList<>();
        while ((line = br.readLine()) != null)
        {
            if( !line.trim().isEmpty() && !line.startsWith( "#" ) ) {
                tickerList.add(line);
            }
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
        if( AlgoConfiguration.getReadAvailableStockFrom().equals(AlgoConfiguration.FROM_MONGODB) ) {
            stock.readFromMongoDB();
        }
        else {
            stock.read(Constants.SRC_MAIN_RESOURCE_FILEPATH);
        }
        return stock;
    }

    private String getNextYearInString( Calendar date ){
        return Integer.toString( date.get( Calendar.YEAR ) + 1 );
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

    private void dumpDateTickerVolumeToFiles(Map<String, Map<String, Double>> dateTickerVol) throws IOException {
        for( Map.Entry< String, Map< String, Double>> entry: dateTickerVol.entrySet() ){
            String filename = classFilePath + entry.getKey() + ".txt";
            Set<String> stockList = entry.getValue().keySet();
            BufferedWriter out = new BufferedWriter( new FileWriter( filename) );
            for(String stock : stockList){
                out.write( stock );
                out.newLine();
            }
            out.close();
        }
    }


    public void generateStockListsByDate() throws IOException {
        List<String> tickers = getListOfAllTickers();
        Date earliestDate = defaultStartDate;
        Date latestDate = new Date(); // present time
        for( String ticker : tickers ) {
            Stock stock = constructStockFromTickerString(ticker);
            allStocks.put(ticker, stock );
            Date comparingDate = stock.getEarliestDate();
            if( earliestDate.compareTo( comparingDate) > 0 ) {
                earliestDate = comparingDate;
            }

            if( latestDate.compareTo( comparingDate) < 0 ) {
                latestDate = comparingDate;
            }
        }
        Calendar current = Calendar.getInstance();
        current.setTime( earliestDate );
        Calendar latest = Calendar.getInstance();
        latest.setTime( latestDate );

        // if currently 2019 then return 20200101
        String yearString = getNextYearInString( current ) + "0101";
        String lastYearString;
        // dateTickerVol contains volume which is not needed for now
        Map<String, Map<String, Double>> dateTickerVol = new TreeMap<>();
        yearToTickerList = new HashMap<>();
        TreeMap<String, Double> tickerVol = new TreeMap<>();
        while( current.compareTo( latest ) <= 0 ){
            lastYearString = yearString;
            for(Map.Entry<String, Stock> entry : allStocks.entrySet()){
                if( !tickerVol.containsKey( entry.getKey() ) ) {
                    tickerVol.put(entry.getKey(), entry.getValue().getHistory().get(current.getTime()).getVolume());
                }
                else {
                    Double currentVol = tickerVol.get(entry.getKey());
                    tickerVol.replace(entry.getKey(),
                            currentVol + entry.getValue().getHistory().get(current.getTime()).getVolume());
                }
            }
            current.roll( Calendar.DAY_OF_MONTH, true );
            // The active stocks this year are the available stocks since 1 Jan next year
            yearString = getNextYearInString( current ) + "0101";
            if( !lastYearString.equals( yearString ) ){
                dateTickerVol.put( lastYearString, sortMapByValue( tickerVol ) );
                yearToTickerList.put( lastYearString,
                        new ArrayList<>( dateTickerVol.get(lastYearString).keySet()) );
                tickerVol = new TreeMap<>();
            }
        }
        dumpDateTickerVolumeToFiles(dateTickerVol);
    }

}
