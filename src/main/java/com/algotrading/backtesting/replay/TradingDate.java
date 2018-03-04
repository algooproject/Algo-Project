package com.algotrading.backtesting.replay;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.algotrading.backtesting.util.Constants;

public class TradingDate {

	private String file;
	private List<Date> tradingDates;
	private int currentTradingDateIndex;
	// private boolean isLastDay = false;

	public TradingDate() {
		tradingDates = new ArrayList<>();
	}

	public TradingDate(String file) throws IOException, ParseException {
		this.file = file;
		tradingDates = new ArrayList<>();
		read();
	}

	public void add(Date date) {
		tradingDates.add(date);
	}

	public void setCurrentDate(Date date) {
//		System.out.println("tradingDates.size()" + tradingDates.size());
		for (int i = 0; i < tradingDates.size(); i++) {
//			System.out.println("date.toString(): " + date.toString());
//			System.out.println("tradingDates.get(i): " + tradingDates.get(i));
			if (date.compareTo(tradingDates.get(i)) <= 0) {
//				System.out.println("date.toString(): " + date.toString());
//				System.out.println("tradingDates.get(i): " + tradingDates.get(i));
				currentTradingDateIndex = i;
				break;
			}
		}
	}

	public Date rollBackCurrentDate(Date date, Date noLaterThan) throws IOException {
		for (int i = tradingDates.size() - 1; i >= 0; i--) {
			if (date.compareTo(tradingDates.get(i)) >= 0 && noLaterThan.compareTo(tradingDates.get(i)) >=0) {
				return tradingDates.get(i);
			}
		}
		throw new IOException("rollBackCurrentDate Error: Earlier date does not exists.");
	}

	public Date rollToCurrentDate(Date date, Date noEarlierThan) throws IOException {
		for (int i = 0; i < tradingDates.size(); i++) {
			if (date.compareTo(tradingDates.get(i)) <= 0 && noEarlierThan.compareTo(tradingDates.get(i)) <=0) {
				return tradingDates.get(i);
			}
		}
		throw new IOException("rollBackCurrentDate Error: Later date does not exists.");
	}	
	
	public Date currentDate() {
		return tradingDates.get(currentTradingDateIndex);
	}

	public void rollDay() {
		currentTradingDateIndex++;
	}

	public boolean isNotLastDate() {
		return currentTradingDateIndex < tradingDates.size();
	}

	public void read() throws IOException, ParseException {
		Path filePath = new File(file).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		tradingDates = stringList.stream()
				.map(dateStr -> parseDate(dateStr))
				.sorted()
				.collect(Collectors.toList());
	}

	private Date parseDate(String dateStr) {
		try {
			return Constants.DATE_FORMAT_YYYYMMDD.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
