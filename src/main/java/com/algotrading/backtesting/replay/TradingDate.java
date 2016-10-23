package com.algotrading.backtesting.replay;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TradingDate {

	private static String DATE_PATTERN = "yyyy-MM-dd";

	private String file;
	private List<Date> tradingDates;
	private int currentTradingDateIndex;

	public TradingDate(String file) throws IOException, ParseException {
		this.file = file;
		tradingDates = new ArrayList<>();
		read();
	}

	public void setCurrentDate(Date date) {
		String dateStr = new SimpleDateFormat(DATE_PATTERN).format(date);
		for (int i = 0; i < tradingDates.size(); i++) {
			if (date.compareTo(tradingDates.get(i)) == 0) {
				currentTradingDateIndex = i;
				break;
			}
		}
	}

	public Date currentDate() {
		return tradingDates.get(currentTradingDateIndex);
	}

	public Date rollDay() {
		currentTradingDateIndex++;
		return tradingDates.get(currentTradingDateIndex);
	}

	public boolean isLastDate() {
		return currentTradingDateIndex + 1 == tradingDates.size();
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
			return new SimpleDateFormat(DATE_PATTERN).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
