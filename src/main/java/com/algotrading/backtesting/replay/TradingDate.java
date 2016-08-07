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

	private static String FILE_PATH = "src/main/resources/trading_date.txt";
	private static String DATE_PATTERN = "yyyy-MM-dd";

	private List<Date> tradingDates;
	private int currentTradingDateIndex;

	public TradingDate() {
		tradingDates = new ArrayList<>();
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
		Path filePath = new File(FILE_PATH).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		tradingDates = stringList.stream().map(dateStr -> parseDate(dateStr)).sorted().collect(Collectors.toList());
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
