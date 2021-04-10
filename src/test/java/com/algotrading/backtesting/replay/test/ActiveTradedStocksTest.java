package com.algotrading.backtesting.replay.test;

import com.algotrading.backtesting.replay.ActiveTradedStocksReader;
import com.algotrading.backtesting.replay.ActivelyTradedStocks;
import com.algotrading.backtesting.replay.AvailableStocks;
import com.algotrading.backtesting.stock.Stock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
    public static final Date DATE_20130102 = new GregorianCalendar(2013, 0, 2).getTime(); // 20130102
    public static final Date DATE_20131231 = new GregorianCalendar(2013, 11, 31).getTime(); // 20131231
    public static final Date DATE_20140101 = new GregorianCalendar(2014, 0, 1).getTime(); // 20140101
    public static final Date DATE_20150101 = new GregorianCalendar(2015, 0, 1).getTime(); // 20150101
    @Mock
    private ActiveTradedStocksReader activeTradedStocksReader;

    @Before
    public void setup() throws IOException {
        Map<String, List<String>> map = new HashMap<>();
        map.put(sdf.format(DATE_20130101), Arrays.asList(HK_0001, HK_0002, HK_0003,
                HK_0004, HK_0005, HK_0006,
                HK_0007, HK_0008, HK_0009,
                HK_0010 ));
        map.put(sdf.format(DATE_20140101), Arrays.asList(HK_0004, HK_0005, HK_0006,
                HK_0007, HK_0008, HK_0009,
                HK_0010, HK_0011, HK_0012,
                HK_0001));
        map.put(sdf.format(DATE_20150101), Arrays.asList(HK_0009, HK_0010, HK_0011, HK_0012,
                HK_0001, HK_0002, HK_0003,
                HK_0004, HK_0005, HK_0006));
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

        Assert.assertEquals(HK_0004, allAvailableStocks.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0006, allAvailableStocks.get(HK_0006).getTicker());

        Assert.assertEquals(HK_0009, allAvailableStocks.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks.get(HK_0010).getTicker());
        Assert.assertEquals(HK_0011, allAvailableStocks.get(HK_0011).getTicker());

        AvailableStocks availableStocks2013 = activelyTradedStocks.get(DATE_20130101);
        Map<String, Stock> allAvailableStocks2013 = availableStocks2013.getAllAvailableStocks();
        Assert.assertEquals(3, allAvailableStocks2013.size());
        Assert.assertEquals(HK_0001, allAvailableStocks2013.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks2013.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks2013.get(HK_0003).getTicker());

        AvailableStocks availableStocks20130102 = activelyTradedStocks.get(DATE_20130102);
        Map<String, Stock> allAvailableStocks20130102 = availableStocks20130102.getAllAvailableStocks();
        Assert.assertEquals(3, allAvailableStocks20130102.size());
        Assert.assertEquals(HK_0001, allAvailableStocks20130102.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks20130102.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks20130102.get(HK_0003).getTicker());

        AvailableStocks availableStocks20131231 = activelyTradedStocks.get(DATE_20131231);
        Map<String, Stock> allAvailableStocks20131231 = availableStocks20131231.getAllAvailableStocks();
        Assert.assertEquals(3, allAvailableStocks20131231.size());
        Assert.assertEquals(HK_0001, allAvailableStocks20131231.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks20131231.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks20131231.get(HK_0003).getTicker());

        AvailableStocks availableStocks2014 = activelyTradedStocks.get(DATE_20140101);
        Map<String, Stock> allAvailableStocks2014 = availableStocks2014.getAllAvailableStocks();
        Assert.assertEquals(3, allAvailableStocks2014.size());
        Assert.assertEquals(HK_0004, allAvailableStocks2014.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks2014.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0006, allAvailableStocks2014.get(HK_0006).getTicker());

        AvailableStocks availableStocks2015 = activelyTradedStocks.get(DATE_20150101);
        Map<String, Stock> allAvailableStocks2015 = availableStocks2015.getAllAvailableStocks();
        Assert.assertEquals(3, allAvailableStocks2015.size());
        Assert.assertEquals(HK_0009, allAvailableStocks2015.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks2015.get(HK_0010).getTicker());
        Assert.assertEquals(HK_0011, allAvailableStocks2015.get(HK_0011).getTicker());
    }

    @Test
    public void testTop50Percent() throws IOException {
        ActivelyTradedStocks activelyTradedStocks = new ActivelyTradedStocks(activeTradedStocksReader, 50, false );
        Map<String, Stock> allAvailableStocks = activelyTradedStocks.getAllAvailableStocks();
        Assert.assertEquals(12, allAvailableStocks.size());
        Assert.assertEquals(HK_0001, allAvailableStocks.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks.get(HK_0003).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0006, allAvailableStocks.get(HK_0006).getTicker());
        Assert.assertEquals(HK_0007, allAvailableStocks.get(HK_0007).getTicker());
        Assert.assertEquals(HK_0008, allAvailableStocks.get(HK_0008).getTicker());
        Assert.assertEquals(HK_0009, allAvailableStocks.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks.get(HK_0010).getTicker());
        Assert.assertEquals(HK_0011, allAvailableStocks.get(HK_0011).getTicker());
        Assert.assertEquals(HK_0012, allAvailableStocks.get(HK_0012).getTicker());

        AvailableStocks availableStocks2013 = activelyTradedStocks.get(DATE_20130101);
        Map<String, Stock> allAvailableStocks2013 = availableStocks2013.getAllAvailableStocks();
        Assert.assertEquals(5, allAvailableStocks2013.size());
        Assert.assertEquals(HK_0001, allAvailableStocks2013.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks2013.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks2013.get(HK_0003).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks2013.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks2013.get(HK_0005).getTicker());

        AvailableStocks availableStocks20130102 = activelyTradedStocks.get(DATE_20130102);
        Map<String, Stock> allAvailableStocks20130102 = availableStocks20130102.getAllAvailableStocks();
        Assert.assertEquals(5, allAvailableStocks20130102.size());
        Assert.assertEquals(HK_0001, allAvailableStocks20130102.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks20130102.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks20130102.get(HK_0003).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks20130102.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks20130102.get(HK_0005).getTicker());

        AvailableStocks availableStocks20131231 = activelyTradedStocks.get(DATE_20131231);
        Map<String, Stock> allAvailableStocks20131231 = availableStocks20131231.getAllAvailableStocks();
        Assert.assertEquals(5, allAvailableStocks20131231.size());
        Assert.assertEquals(HK_0001, allAvailableStocks20131231.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks20131231.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks20131231.get(HK_0003).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks20131231.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks20131231.get(HK_0005).getTicker());

        AvailableStocks availableStocks2014 = activelyTradedStocks.get(DATE_20140101);
        Map<String, Stock> allAvailableStocks2014 = availableStocks2014.getAllAvailableStocks();
        Assert.assertEquals(5, allAvailableStocks2014.size());
        Assert.assertEquals(HK_0006, allAvailableStocks2014.get(HK_0006).getTicker());
        Assert.assertEquals(HK_0007, allAvailableStocks2014.get(HK_0007).getTicker());
        Assert.assertEquals(HK_0008, allAvailableStocks2014.get(HK_0008).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks2014.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks2014.get(HK_0005).getTicker());

        AvailableStocks availableStocks2015 = activelyTradedStocks.get(DATE_20150101);
        Map<String, Stock> allAvailableStocks2015 = availableStocks2015.getAllAvailableStocks();
        Assert.assertEquals(5, allAvailableStocks2015.size());
        Assert.assertEquals(HK_0009, allAvailableStocks2015.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks2015.get(HK_0010).getTicker());
        Assert.assertEquals(HK_0011, allAvailableStocks2015.get(HK_0011).getTicker());
        Assert.assertEquals(HK_0012, allAvailableStocks2015.get(HK_0012).getTicker());
        Assert.assertEquals(HK_0001, allAvailableStocks2015.get(HK_0001).getTicker());
    }

    @Test
    public void testTop25Percent() throws IOException {
        ActivelyTradedStocks activelyTradedStocks = new ActivelyTradedStocks(activeTradedStocksReader, 25, false );
        Map<String, Stock> allAvailableStocks = activelyTradedStocks.getAllAvailableStocks();
        Assert.assertEquals(6, allAvailableStocks.size());
        Assert.assertEquals(HK_0001, allAvailableStocks.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0009, allAvailableStocks.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks.get(HK_0010).getTicker());

        AvailableStocks availableStocks2013 = activelyTradedStocks.get(DATE_20130101);
        Map<String, Stock> allAvailableStocks2013 = availableStocks2013.getAllAvailableStocks();
        Assert.assertEquals(2, allAvailableStocks2013.size());
        Assert.assertEquals(HK_0001, allAvailableStocks2013.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks2013.get(HK_0002).getTicker());

        AvailableStocks availableStocks20130102 = activelyTradedStocks.get(DATE_20130102);
        Map<String, Stock> allAvailableStocks20130102 = availableStocks20130102.getAllAvailableStocks();
        Assert.assertEquals(2, allAvailableStocks20130102.size());
        Assert.assertEquals(HK_0001, allAvailableStocks20130102.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks20130102.get(HK_0002).getTicker());

        AvailableStocks availableStocks20131231 = activelyTradedStocks.get(DATE_20131231);
        Map<String, Stock> allAvailableStocks20131231 = availableStocks20131231.getAllAvailableStocks();
        Assert.assertEquals(2, allAvailableStocks20131231.size());
        Assert.assertEquals(HK_0001, allAvailableStocks20131231.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks20131231.get(HK_0002).getTicker());

        AvailableStocks availableStocks2014 = activelyTradedStocks.get(DATE_20140101);
        Map<String, Stock> allAvailableStocks2014 = availableStocks2014.getAllAvailableStocks();
        Assert.assertEquals(2, allAvailableStocks2014.size());
        Assert.assertEquals(HK_0004, allAvailableStocks2014.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks2014.get(HK_0005).getTicker());

        AvailableStocks availableStocks2015 = activelyTradedStocks.get(DATE_20150101);
        Map<String, Stock> allAvailableStocks2015 = availableStocks2015.getAllAvailableStocks();
        Assert.assertEquals(2, allAvailableStocks2015.size());
        Assert.assertEquals(HK_0009, allAvailableStocks2015.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks2015.get(HK_0010).getTicker());
    }

    @Test
    public void testDefaultTop50() throws IOException {
        ActivelyTradedStocks activelyTradedStocks = new ActivelyTradedStocks(activeTradedStocksReader);
        Map<String, Stock> allAvailableStocks = activelyTradedStocks.getAllAvailableStocks();
        Assert.assertEquals(12, allAvailableStocks.size());
        Assert.assertEquals(HK_0001, allAvailableStocks.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks.get(HK_0003).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0006, allAvailableStocks.get(HK_0006).getTicker());
        Assert.assertEquals(HK_0007, allAvailableStocks.get(HK_0007).getTicker());
        Assert.assertEquals(HK_0008, allAvailableStocks.get(HK_0008).getTicker());
        Assert.assertEquals(HK_0009, allAvailableStocks.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks.get(HK_0010).getTicker());
        Assert.assertEquals(HK_0011, allAvailableStocks.get(HK_0011).getTicker());
        Assert.assertEquals(HK_0012, allAvailableStocks.get(HK_0012).getTicker());

        AvailableStocks availableStocks2013 = activelyTradedStocks.get(DATE_20130101);
        Map<String, Stock> allAvailableStocks2013 = availableStocks2013.getAllAvailableStocks();
        Assert.assertEquals(10, allAvailableStocks2013.size());
        Assert.assertEquals(HK_0001, allAvailableStocks2013.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks2013.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks2013.get(HK_0003).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks2013.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks2013.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0006, allAvailableStocks2013.get(HK_0006).getTicker());
        Assert.assertEquals(HK_0007, allAvailableStocks2013.get(HK_0007).getTicker());
        Assert.assertEquals(HK_0008, allAvailableStocks2013.get(HK_0008).getTicker());
        Assert.assertEquals(HK_0009, allAvailableStocks2013.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks2013.get(HK_0010).getTicker());

        AvailableStocks availableStocks20130102 = activelyTradedStocks.get(DATE_20130102);
        Map<String, Stock> allAvailableStocks20130102 = availableStocks20130102.getAllAvailableStocks();
        Assert.assertEquals(10, allAvailableStocks20130102.size());
        Assert.assertEquals(HK_0001, allAvailableStocks20130102.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks20130102.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks20130102.get(HK_0003).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks20130102.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks20130102.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0006, allAvailableStocks20130102.get(HK_0006).getTicker());
        Assert.assertEquals(HK_0007, allAvailableStocks20130102.get(HK_0007).getTicker());
        Assert.assertEquals(HK_0008, allAvailableStocks20130102.get(HK_0008).getTicker());
        Assert.assertEquals(HK_0009, allAvailableStocks20130102.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks20130102.get(HK_0010).getTicker());

        AvailableStocks availableStocks20131231 = activelyTradedStocks.get(DATE_20131231);
        Map<String, Stock> allAvailableStocks20131231 = availableStocks20131231.getAllAvailableStocks();
        Assert.assertEquals(10, allAvailableStocks20131231.size());
        Assert.assertEquals(HK_0001, allAvailableStocks20131231.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks20131231.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks20131231.get(HK_0003).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks20131231.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks20131231.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0006, allAvailableStocks20131231.get(HK_0006).getTicker());
        Assert.assertEquals(HK_0007, allAvailableStocks20131231.get(HK_0007).getTicker());
        Assert.assertEquals(HK_0008, allAvailableStocks20131231.get(HK_0008).getTicker());
        Assert.assertEquals(HK_0009, allAvailableStocks20131231.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks20131231.get(HK_0010).getTicker());

        AvailableStocks availableStocks2014 = activelyTradedStocks.get(DATE_20140101);
        Map<String, Stock> allAvailableStocks2014 = availableStocks2014.getAllAvailableStocks();
        Assert.assertEquals(10, allAvailableStocks2014.size());
        Assert.assertEquals(HK_0006, allAvailableStocks2014.get(HK_0006).getTicker());
        Assert.assertEquals(HK_0007, allAvailableStocks2014.get(HK_0007).getTicker());
        Assert.assertEquals(HK_0008, allAvailableStocks2014.get(HK_0008).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks2014.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks2014.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0009, allAvailableStocks2014.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks2014.get(HK_0010).getTicker());
        Assert.assertEquals(HK_0011, allAvailableStocks2014.get(HK_0011).getTicker());
        Assert.assertEquals(HK_0012, allAvailableStocks2014.get(HK_0012).getTicker());
        Assert.assertEquals(HK_0001, allAvailableStocks2014.get(HK_0001).getTicker());

        AvailableStocks availableStocks2015 = activelyTradedStocks.get(DATE_20150101);
        Map<String, Stock> allAvailableStocks2015 = availableStocks2015.getAllAvailableStocks();
        Assert.assertEquals(10, allAvailableStocks2015.size());
        Assert.assertEquals(HK_0009, allAvailableStocks2015.get(HK_0009).getTicker());
        Assert.assertEquals(HK_0010, allAvailableStocks2015.get(HK_0010).getTicker());
        Assert.assertEquals(HK_0011, allAvailableStocks2015.get(HK_0011).getTicker());
        Assert.assertEquals(HK_0012, allAvailableStocks2015.get(HK_0012).getTicker());
        Assert.assertEquals(HK_0001, allAvailableStocks2015.get(HK_0001).getTicker());
        Assert.assertEquals(HK_0002, allAvailableStocks2015.get(HK_0002).getTicker());
        Assert.assertEquals(HK_0003, allAvailableStocks2015.get(HK_0003).getTicker());
        Assert.assertEquals(HK_0004, allAvailableStocks2015.get(HK_0004).getTicker());
        Assert.assertEquals(HK_0005, allAvailableStocks2015.get(HK_0005).getTicker());
        Assert.assertEquals(HK_0006, allAvailableStocks2015.get(HK_0006).getTicker());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testNoSufficientNumberOfStocks() throws IOException{

        // Question: Why not 20130101? Seems not depending on the order of putting to the map
        thrown.expectMessage("Generating available stock error: 20150101.txt does not provide sufficient number of stocks");
        ActivelyTradedStocks activelyTradedStocks = new ActivelyTradedStocks(activeTradedStocksReader, 1, false );
    }
}
