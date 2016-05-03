package com.roulesophy.stockanalysis.model.stock.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.roulesophy.stockanalysis.model.stock.IStock;
import com.roulesophy.stockanalysis.model.stock.YahooStock;
import com.roulesophy.stockanalysis.stockpattern.IStockPattern;
import com.roulesophy.stockanalysis.stockpattern.PatternA;

public class YahooStockTest {
	
	@Test
	public void testYahooStockTest() throws ParseException {
		IStock hk1382 = new YahooStock("1382.HK");
		System.out.println("Currency:			" + hk1382.currency());
		System.out.println("Name:				" + hk1382.name());
		System.out.println("StockExchange: 		" + hk1382.stockExchange());
		System.out.println("Ticker: 			" + hk1382.ticker());
		System.out.println("Average Volume: 	" + hk1382.averageVolume());
		System.out.println("BVPS:				" + hk1382.bvps());
		Date date = new SimpleDateFormat("yyyyMMdd").parse("20160408");
		System.out.println("Close price: 		" + hk1382.close(date));
		System.out.println("Current Price: 		" + hk1382.currentPrice());
		System.out.println("Dividend Yield:		" + hk1382.dividendYield());
		System.out.println("Dividend Yield %:	" + hk1382.dividendYieldPercentage());
		System.out.println("EMA(50):			" + hk1382.EMA(date, 50));
		System.out.println("EPS:				" + hk1382.eps());
		System.out.println("Ex Date: 			" + hk1382.exDate());
		System.out.println("High Price:			" + hk1382.high(date));
		System.out.println("Low Price: 			" + hk1382.low(date));
		System.out.println("Open Price: 		" + hk1382.open(date));
		System.out.println("Payable Date: 		" + hk1382.payableDate());
		System.out.println("PB:					" + hk1382.pb());
		System.out.println("PE: 				" + hk1382.pe());
		System.out.println("PEG: 				" + hk1382.peg());
		System.out.println("P/Sales: 			" + hk1382.psales());
		System.out.println("RSI(14):			" + hk1382.RSI(date, 14));
		System.out.println("Short Ratio: 		" + hk1382.shortRatio());
		System.out.println("SMA(50):			" + hk1382.SMA(date, 50));
		System.out.println("Volume: 			" + hk1382.volume());
		System.out.println("Year High:			" + hk1382.yearHigh());
		System.out.println("Year Low:			" + hk1382.yearLow());
		
		
		IStockPattern pattern = new PatternA(hk1382);
		System.out.println(pattern.buySellSignal());
	}
}
