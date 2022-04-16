package com.algotrading.backtesting.util.test;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;
import com.algotrading.backtesting.util.CreateActiveStockFiles;
import com.algotrading.backtesting.util.TickerProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateActiveStockFilesTest {

    public static final Date date1_2018 = new GregorianCalendar(2018, 0, 11).getTime();
    public static final Date date2_2018 = new GregorianCalendar(2018, 1, 11).getTime();
    public static final Date date1_2019 = new GregorianCalendar(2019, 3, 11).getTime();
    public static final Date date2_2019 = new GregorianCalendar(2019, 7, 17).getTime();
    public static final Date date1_2020 = new GregorianCalendar(2020, 4, 29).getTime();
    public static final Date date2_2020 = new GregorianCalendar(2020, 9, 26).getTime();

    Path folderPath;

    @Mock
    private TickerProvider tickerProvider;

    @Before
    public void setup() throws Exception {
        // all tickers are Stock1, Stock2 and Stock3
        List<String> tickers = new ArrayList<>();
        tickers.add( "Stock1" );
        tickers.add( "Stock2" );
        tickers.add( "Stock3" );
        Mockito.when(tickerProvider.getAllTickers()).thenReturn(tickers);

        // Stock 1: 40k, 30k, 50k in 2018, 2019 and 2020 resp.
        Map<Date, StockHistory> history1 = new LinkedHashMap<>();
        history1.put( date1_2018,
                new StockHistory( date1_2018, 8, 9, 10, 7, 9, 10000 ) );
        history1.put( date2_2018,
                new StockHistory( date2_2018, 8, 9, 10, 7, 9, 30000 ) );

        history1.put( date1_2019,
                new StockHistory( date1_2019, 8, 9, 10, 7, 9, 13000 ) );
        history1.put( date2_2019,
                new StockHistory( date2_2019, 8, 9, 10, 7, 9, 17000 ) );

        history1.put( date1_2020,
                new StockHistory( date1_2020, 8, 9, 10, 7, 9, 18000 ) );
        history1.put( date2_2020,
                new StockHistory( date2_2020, 8, 9, 10, 7, 9, 32000 ) );
        Stock stock1 = new Stock( tickers.get(0), history1, 100);
        Mockito.when(tickerProvider.constructStockFromTickerString(tickers.get(0))).thenReturn(stock1);

        // Stock 2: 50k, 40k, 30k in 2018, 2019 and 2020 resp.
        Map<Date, StockHistory> history2 = new LinkedHashMap<>();
        history2.put( date1_2018,
                new StockHistory( date1_2018, 8, 9, 10, 7, 9, 20000 ) );
        history2.put( date2_2018,
                new StockHistory( date2_2018, 8, 9, 10, 7, 9, 30000 ) );

        history2.put( date1_2019,
                new StockHistory( date1_2019, 8, 9, 10, 7, 9, 13000 ) );
        history2.put( date2_2019,
                new StockHistory( date2_2019, 8, 9, 10, 7, 9, 27000 ) );

        history2.put( date1_2020,
                new StockHistory( date1_2020, 8, 9, 10, 7, 9, 8000 ) );
        history2.put( date2_2020,
                new StockHistory( date2_2020, 8, 9, 10, 7, 9, 22000 ) );
        Stock stock2 = new Stock( tickers.get(1), history2, 100);
        Mockito.when(tickerProvider.constructStockFromTickerString(tickers.get(1))).thenReturn(stock2);

        // Stock 3: 30k, 50k, 40k in 2018, 2019 and 2020 resp.
        Map<Date, StockHistory> history3 = new LinkedHashMap<>();
        history3.put( date1_2018,
                new StockHistory( date1_2018, 8, 9, 10, 7, 9, 20000 ) );
        history3.put( date2_2018,
                new StockHistory( date2_2018, 8, 9, 10, 7, 9, 10000 ) );

        history3.put( date1_2019,
                new StockHistory( date1_2019, 8, 9, 10, 7, 9, 23000 ) );
        history3.put( date2_2019,
                new StockHistory( date2_2019, 8, 9, 10, 7, 9, 27000 ) );

        history3.put( date1_2020,
                new StockHistory( date1_2020, 8, 9, 10, 7, 9, 18000 ) );
        history3.put( date2_2020,
                new StockHistory( date2_2020, 8, 9, 10, 7, 9, 22000 ) );
        Stock stock3 = new Stock( tickers.get(2), history3, 100);
        Mockito.when(tickerProvider.constructStockFromTickerString(tickers.get(2))).thenReturn(stock3);

        folderPath = Files.createTempDirectory("folder");
        folderPath.toFile().deleteOnExit();
    }

    @Test
    public void testCreateFiles() throws Exception{
        CreateActiveStockFiles createActiveStockFiles = new CreateActiveStockFiles(tickerProvider);
        createActiveStockFiles.generateStockListsByDate(folderPath.toString() + "/" );
        List<Path> fileList = new ArrayList<>();
        File dir = new File(folderPath.toString());
        File[] files = dir.listFiles((dir1, name) -> name.matches("\\d\\d\\d\\d\\d\\d\\d\\d\\.txt"));
        assert( files != null );

        Map<String, List<String>> yearToTickerList = new HashMap<>();
        for (File file : files) {
            String fullFilename = file.toPath().toString();
            String filename = fullFilename.substring( fullFilename.length() - 12 );
            Boolean correctFile = filename.equals("20190101.txt") || filename.equals("20200101.txt")
                || filename.equals("20210101.txt") || filename.equals("20220101.txt");
            Assert.assertTrue(filename, correctFile);
            yearToTickerList.put( filename , Files.readAllLines(file.toPath(), Charset.defaultCharset()) );
        }
        Assert.assertEquals("Stock2", yearToTickerList.get("20190101.txt").get(0));
        Assert.assertEquals("Stock1", yearToTickerList.get("20190101.txt").get(1));
        Assert.assertEquals("Stock3", yearToTickerList.get("20190101.txt").get(2));

        Assert.assertEquals("Stock3", yearToTickerList.get("20200101.txt").get(0));
        Assert.assertEquals("Stock2", yearToTickerList.get("20200101.txt").get(1));
        Assert.assertEquals("Stock1", yearToTickerList.get("20200101.txt").get(2));

        Assert.assertEquals("Stock1", yearToTickerList.get("20210101.txt").get(0));
        Assert.assertEquals("Stock3", yearToTickerList.get("20210101.txt").get(1));
        Assert.assertEquals("Stock2", yearToTickerList.get("20210101.txt").get(2));
    }

}
