package com.algotrading.stockanalysis.model.stock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import com.algotrading.stockanalysis.model.indicatorcalculator.YahooStockEmaCalculator;
import com.algotrading.stockanalysis.model.indicatorcalculator.YahooStockEmaCalculator1;
import com.algotrading.stockanalysis.model.indicatorcalculator.YahooStockRsiCalculator1;
import com.algotrading.stockanalysis.model.indicatorcalculator.YahooStockSmaCalculator1;

import yahoofinance.Stock;
import yahoofinance.Utils;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistQuotesRequest;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class YahooStock implements IStock {

	private Stock stock;
	private static YahooStockPool stockPool = new YahooStockPool();
	private Map<String, HistoricalQuote> stockHistory;
	public static String DATE_FORMAT = "yyyyMMdd";
	Calendar to;
    Calendar from; 
	
	public YahooStock(String ticker) {
		if (stockPool.stockExist(ticker)) {
			stock = stockPool.getStock(ticker);
			stockHistory = stockPool.getStockHistory(ticker);
			from = stockPool.getFrom(ticker);
			to = stockPool.getTo(ticker);
		} else {
			try {
				stock = YahooFinance.get(ticker, true);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.YEAR, -100);
				to = HistQuotesRequest.DEFAULT_TO;
				from = cal;
				List<HistoricalQuote> historticalQuoteList = stock.getHistory(cal, Interval.DAILY);
				stockHistory = new TreeMap<String, HistoricalQuote>();
				for(HistoricalQuote quote : historticalQuoteList) {
					stockHistory.put(new SimpleDateFormat(DATE_FORMAT).format(quote.getDate().getTime()), quote);
				}
				stockPool.put(ticker, stock, stockHistory, from, to);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void exportHistoryFile() {
		OutputStream os = null;
		InputStream is = null;
		try {
	        Map<String, String> params = new LinkedHashMap<String, String>();
	        params.put("s", this.ticker());
	
	        params.put("a", String.valueOf(this.from.get(Calendar.MONTH)));
	        params.put("b", String.valueOf(this.from.get(Calendar.DAY_OF_MONTH)));
	        params.put("c", String.valueOf(this.from.get(Calendar.YEAR)));
	
	        params.put("d", String.valueOf(this.to.get(Calendar.MONTH)));
	        params.put("e", String.valueOf(this.to.get(Calendar.DAY_OF_MONTH)));
	        params.put("f", String.valueOf(this.to.get(Calendar.YEAR)));
	
	        params.put("g", Interval.DAILY.getTag());
	
	        params.put("ignore", ".csv");
	
	        String url = YahooFinance.HISTQUOTES_BASE_URL + "?" + Utils.getURLParameters(params);
	
	        // Get CSV from Yahoo
	        YahooFinance.logger.log(Level.INFO, ("Sending request: " + url));
	
	        URL request = new URL(url);
	        URLConnection connection = request.openConnection();
	        connection.setConnectTimeout(YahooFinance.CONNECTION_TIMEOUT);
	        connection.setReadTimeout(YahooFinance.CONNECTION_TIMEOUT);
	        is = connection.getInputStream();
			os = new FileOutputStream(new File("src/main/resources/" + this.ticker() + ".csv"));
			int read = 0;
			byte[] bytes = new byte[1024];
		
			while ((read = is.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeFile(os, is);
		}
	
	}

	private void closeFile(OutputStream os, InputStream is) {
		try {
			if (os != null) os.close();
			if (is != null) is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, HistoricalQuote> getStockHistory() {
		return stockHistory;
	}

	public BigDecimal high(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getHigh() : null;
	}

	public BigDecimal open(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getOpen() : null;
	}

	public BigDecimal low(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getLow() : null;
	}

	public BigDecimal close(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getClose() : null;
	}
	
	public BigDecimal adjClose(Date date) {
		HistoricalQuote historicalQuote = getHistoricalPrice(date);
		return historicalQuote != null ? historicalQuote.getAdjClose() : null;
	}

	private HistoricalQuote getHistoricalPrice(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		HistoricalQuote historicalQuote = stockHistory.get(new SimpleDateFormat(DATE_FORMAT).format(cal.getTime()));
		return historicalQuote;
	}

	public BigDecimal RSI(Date date, int scale) {	
		YahooStockEmaCalculator emaCalculator = new YahooStockEmaCalculator1(this);
		return new YahooStockRsiCalculator1(this, emaCalculator).calculate(date, scale);
	}

	public BigDecimal EMA(Date date, int scale) {
		return new YahooStockEmaCalculator1(this).calculate(date, scale);
	}

	public BigDecimal SMA(Date date, int scale) {
		return new YahooStockSmaCalculator1(this).calculate(date, scale);
	}

	public String currency() {
		return stock.getCurrency();
	}

	public String name() {
		return stock.getName();
	}

	public String ticker() {
		return stock.getSymbol();
	}

	public String stockExchange() {
		return stock.getStockExchange();
	}

	public BigDecimal currentPrice() {
		return stock.getQuote().getPrice();
	}

	public BigDecimal yearLow() {
		return stock.getQuote().getYearLow();
	}

	public BigDecimal yearHigh() {
		return stock.getQuote().getYearHigh();
	}

	public BigDecimal volume() {
		return new BigDecimal(stock.getQuote().getVolume());
	}

	public BigDecimal averageVolume() {
		return new BigDecimal(stock.getQuote().getAvgVolume());
	}

	public BigDecimal eps() {
		return stock.getStats().getEps();
	}

	public BigDecimal pe() {
		return stock.getStats().getPe();
	}

	public BigDecimal peg() {
		return stock.getStats().getPeg();
	}

	public BigDecimal pb() {
		return stock.getStats().getPriceBook();
	}

	public BigDecimal psales() {
		return stock.getStats().getPriceSales();
	}

	public BigDecimal bvps() {
		return stock.getStats().getBookValuePerShare();
	}

	public BigDecimal shortRatio() {
		return stock.getStats().getShortRatio();
	}

	public Date payableDate() {
		Calendar payDate = stock.getDividend().getPayDate();
		return payDate != null ? payDate.getTime() : null;
	}

	public Date exDate() {
		Calendar exDate = stock.getDividend().getExDate();
		return exDate != null ? exDate.getTime() : null;
	}

	public BigDecimal dividendYield() {
		return stock.getDividend().getAnnualYield();
	}

	public BigDecimal dividendYieldPercentage() {
		return stock.getDividend().getAnnualYieldPercent();
	}

}
