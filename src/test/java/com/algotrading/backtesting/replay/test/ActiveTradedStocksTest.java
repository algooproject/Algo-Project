package com.algotrading.backtesting.replay.test;

import com.algotrading.backtesting.replay.ActiveTradedStocksReader;
import com.algotrading.backtesting.replay.ActivelyTradedStocks;
import com.algotrading.backtesting.replay.AvailableStocks;
import com.algotrading.backtesting.stock.Stock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.algotrading.constants.TestConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class ActiveTradedStocksTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");



    public static final Date DATE_20130101 = new GregorianCalendar(2013, 0, 1).getTime(); // 20130101
    public static final Date DATE_20140101 = new GregorianCalendar(2014, 0, 1).getTime(); // 20140101
    public static final Date DATE_20150101 = new GregorianCalendar(2015, 0, 1).getTime(); // 20150101
    @Mock
    private ActiveTradedStocksReader activeTradedStocksReader;

    @Before
    public void setup() throws IOException {
        Map<String, List<String>> map = new HashMap<>();
        map.put(sdf.format(DATE_20130101), Arrays.asList(HK_0001, HK_0002, HK_0003, HK_0004, HK_0005));
        map.put(sdf.format(DATE_20140101), Arrays.asList(HK_0006, HK_0007, HK_0008));
        map.put(sdf.format(DATE_20150101), Arrays.asList(HK_0009, HK_0010, HK_0011, HK_0012));
        Mockito.when(activeTradedStocksReader.constructYearToTickerListFromFiles()).thenReturn(map);
    }

    @Test
    public void testAbsolute() throws IOException {
        ActivelyTradedStocks activelyTradedStocks = new ActivelyTradedStocks(activeTradedStocksReader, 3, true);
        Map<String, Stock> allAvailableStocks = activelyTradedStocks.getAllAvailableStocks();
        Assert.assertEquals(9, allAvailableStocks.size());
        Assert.assertEquals(HK_0001, allAvailableStocks.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks.get(HK_0003).getTicker());

        Assert.assertEquals(HK_0006, allAvailableStocks.get(HK_0006).getTicker());
        Assert.assertEquals(HK_0007, allAvailableStocks.get(HK_0007).getTicker());
        Assert.assertEquals(HK_0008, allAvailableStocks.get(HK_0008).getTicker());

        Assert.assertEquals(HK_0009, allAvailableStocks.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks.get(HK_0010).getTicker());
        Assert.assertEquals(HK_0011, allAvailableStocks.get(HK_0011).getTicker());

        AvailableStocks availableStocks2013 = activelyTradedStocks.get(DATE_20130101);
        Map<String, Stock> allAvailableStocks2013 = availableStocks2013.getAllAvailableStocks();
        Assert.assertEquals(3, allAvailableStocks2013.size());
        Assert.assertEquals(HK_0001, allAvailableStocks2013.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks2013.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks2013.get(HK_0003).getTicker());

        AvailableStocks availableStocks2014 = activelyTradedStocks.get(DATE_20140101);
        Map<String, Stock> allAvailableStocks2014 = availableStocks2014.getAllAvailableStocks();
        Assert.assertEquals(3, allAvailableStocks2014.size());
        Assert.assertEquals(HK_0006, allAvailableStocks2014.get(HK_0006).getTicker());
        Assert.assertEquals(HK_0007, allAvailableStocks2014.get(HK_0007).getTicker());
        Assert.assertEquals(HK_0008, allAvailableStocks2014.get(HK_0008).getTicker());

        AvailableStocks availableStocks2015 = activelyTradedStocks.get(DATE_20150101);
        Map<String, Stock> allAvailableStocks2015 = availableStocks2015.getAllAvailableStocks();
        Assert.assertEquals(3, allAvailableStocks2015.size());
        Assert.assertEquals(HK_0009, allAvailableStocks2015.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks2015.get(HK_0010).getTicker());
        Assert.assertEquals(HK_0011, allAvailableStocks2015.get(HK_0011).getTicker());
    }
}
