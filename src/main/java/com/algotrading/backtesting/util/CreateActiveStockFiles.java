package com.algotrading.backtesting.util;

import com.algotrading.backtesting.stock.Stock;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    private Map<String, Stock> allStocks = new LinkedHashMap<>();
    private Map<String, List<String>> yearToTickerList;
    private static Date defaultStartDate = new GregorianCalendar(4000, Calendar.JANUARY, 1).getTime();

    private final TickerProvider tickerProvider;

    public CreateActiveStockFiles(TickerProvider tickerProvider) {
        this.tickerProvider = tickerProvider;
    }

    public void generateStockListsByDate(String outputFolderPath) throws IOException {
        List<String> tickers = tickerProvider.getAllTickers();
        Date earliestDate = defaultStartDate;
        Date latestDate = new Date(); // present time
        for( String ticker : tickers ) {
            Stock stock = tickerProvider.constructStockFromTickerString(ticker);
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
            for(Map.Entry<String, Stock> entry : allStocks.entrySet()) {
                if (entry.getValue().getHistory().containsKey(current.getTime())) {
                    if (!tickerVol.containsKey(entry.getKey())) {
                        tickerVol.put(entry.getKey(), entry.getValue().getHistory().get(current.getTime()).getVolume());
                    } else {
                        Double currentVol = tickerVol.get(entry.getKey());
                        tickerVol.replace(entry.getKey(),
                                currentVol + entry.getValue().getHistory().get(current.getTime()).getVolume());
                    }
                }
            }
            current.add( Calendar.DAY_OF_MONTH, 1 );
            // The active stocks this year are the available stocks since 1 Jan next year
            yearString = getNextYearInString( current ) + "0101";
            if( !lastYearString.equals( yearString ) ){
                dateTickerVol.put( lastYearString, sortMapByValue( tickerVol ) );
                yearToTickerList.put( lastYearString,
                        new ArrayList<>( dateTickerVol.get(lastYearString).keySet()) );
                tickerVol = new TreeMap<>();
            }
        }
        dumpDateTickerVolumeToFiles(dateTickerVol, outputFolderPath);
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
            return valueB.compareTo(valueA); // want orders with descending values
        });
        result.putAll(map);
        return result;
    }

    private void dumpDateTickerVolumeToFiles(Map<String, Map<String, Double>> dateTickerVol, String outputFolderPath) throws IOException {
        for( Map.Entry< String, Map< String, Double>> entry: dateTickerVol.entrySet() ){
            String filename = outputFolderPath + entry.getKey() + ".txt";
            Set<String> stockList = entry.getValue().keySet();
            BufferedWriter out = new BufferedWriter( new FileWriter( filename) );
            for(String stock : stockList){
                out.write( stock );
                out.newLine();
            }
            out.close();
        }
    }

    public static void main(String[] args) throws IOException {
        TickerProvider tickerProvider = TickerProviderFactory.ofTickerProvider();

        CreateActiveStockFiles createActiveStockFiles = new CreateActiveStockFiles(tickerProvider);
        createActiveStockFiles.generateStockListsByDate(Constants.SRC_MAIN_RESOURCE_FILEPATH + "/ActivelyTradedStocksData/");
    }

}
