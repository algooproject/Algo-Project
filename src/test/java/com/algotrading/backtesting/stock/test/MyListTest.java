package com.algotrading.backtesting.stock.test;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.time.DateUtils;

import com.algotrading.backtesting.stock.IStock;
import com.algotrading.backtesting.stock.YahooStock;

import yahoofinance.YahooFinance;

public class MyListTest {
	private static DecimalFormat decimalFormat = new DecimalFormat("0.00");
	public static void main(String[] args) throws ParseException {
		Logger logger = Logger.getLogger(YahooFinance.class.getName());
        logger.setLevel(Level.OFF);
        Date date = DateUtils.truncate(new Date(), Calendar.DATE);
		
		List<IStock> selfWatchList = initWatchList();
		processList(date, selfWatchList, "Self Watch List");
		
		List<IStock> mrMarketWatchList = initMrMarketList();
		processList(date, mrMarketWatchList, "MrMarket Watch List");
	}

	private static void processList(Date date, List<IStock> watchList, String watchListName) {
		System.out.println("====== " + watchListName);
		for (IStock stock : watchList) {
			double rsi = stock.RSI(date, 14).doubleValue();
			double sma10 = stock.SMA(date, 10).doubleValue();
			double closingPrice = stock.currentPrice().doubleValue();
			if (rsi < 30 && closingPrice < sma10) {
				System.out.println(stock.ticker() + ", closingPrice=" + closingPrice + ", RSI=" + decimalFormat.format(rsi));
			}
			stock.exportHistoryFile();
		}
	}

	private static List<IStock> initWatchList() {
		List<IStock> watchList = new ArrayList<IStock>();
		watchList.add(new YahooStock("0034.HK"));
		watchList.add(new YahooStock("0281.HK"));
		watchList.add(new YahooStock("1288.HK"));
		watchList.add(new YahooStock("0635.HK"));
		watchList.add(new YahooStock("1382.HK"));
		watchList.add(new YahooStock("0178.HK"));
		watchList.add(new YahooStock("1098.HK"));
		watchList.add(new YahooStock("0939.HK"));
		watchList.add(new YahooStock("0298.HK"));
		watchList.add(new YahooStock("0163.HK"));
		watchList.add(new YahooStock("0533.HK"));
		watchList.add(new YahooStock("0101.HK"));
		watchList.add(new YahooStock("0900.HK"));
		watchList.add(new YahooStock("2800.HK"));
		watchList.add(new YahooStock("0296.HK"));
		return watchList;
	}
	
	private static List<IStock> initMrMarketList() {
		List<IStock> watchList = new ArrayList<IStock>();
		watchList.add(new YahooStock("0278.HK"));
		watchList.add(new YahooStock("0194.HK"));
		watchList.add(new YahooStock("0216.HK"));
		watchList.add(new YahooStock("0163.HK"));
		watchList.add(new YahooStock("0034.HK"));
		watchList.add(new YahooStock("1098.HK"));
		watchList.add(new YahooStock("0369.HK"));
		watchList.add(new YahooStock("0281.HK"));
		watchList.add(new YahooStock("0225.HK"));
		watchList.add(new YahooStock("0898.HK"));
		watchList.add(new YahooStock("0212.HK"));
		watchList.add(new YahooStock("0026.HK"));
		watchList.add(new YahooStock("0635.HK"));
		watchList.add(new YahooStock("0617.HK"));
		watchList.add(new YahooStock("0355.HK"));
		watchList.add(new YahooStock("0005.HK"));
		watchList.add(new YahooStock("0900.HK"));
		watchList.add(new YahooStock("0610.HK"));
		watchList.add(new YahooStock("0533.HK"));
		watchList.add(new YahooStock("0224.HK"));
		watchList.add(new YahooStock("0050.HK"));
		watchList.add(new YahooStock("0878.HK"));
		watchList.add(new YahooStock("0035.HK"));
		watchList.add(new YahooStock("0010.HK"));
		watchList.add(new YahooStock("0101.HK"));
		watchList.add(new YahooStock("0242.HK"));
		watchList.add(new YahooStock("0882.HK"));
		return watchList;
	}
}
