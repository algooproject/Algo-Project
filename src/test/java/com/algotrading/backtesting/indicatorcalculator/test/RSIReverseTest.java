package com.algotrading.backtesting.indicatorcalculator.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import com.algotrading.backtesting.indicatorcalculator.RSIReverse;

public class RSIReverseTest {

	public static void main(String[] args) throws Exception {
		Calendar loopdate = new GregorianCalendar(2016, 1, 15);
		int map_length = 20;
		// Date recent = loopdate.getTime();
		// double expected_value = 72.83950617; // expected value of recent
		// // date
		int magnitude = 5;
		// int sma_magnitude = 1;

		// Double[] series = new Double[] { 97.35, 95.8, 96.4, 95.95, 95.0,
		// 95.5, 96.9, 96.2, 95.9, 95.8, 97.95, 96.1,
		// 96.7, 95.0, 94.0, 92.55, 93.6, 92.55, 93.0, 92.6 }; // the
		// // series
		Double[] series = new Double[] { 30.0, 95.8, 96.4, 95.95, 95.0, 95.5, 96.9, 96.2, 95.9, 95.8, 97.95, 96.1, 96.7,
				95.0, 94.0, 92.55, 93.6, 92.55, 93.0, 30.0 }; // the
		// series

		// Double[] series = new Double[] { 30.0, 95.8 }; // the
		// // series

		// map_length = 2;
		Map<Date, Double> datedprice = new TreeMap<Date, Double>();
		for (int i = 0; i < map_length; i++) {
			datedprice.put(loopdate.getTime(), series[i]);
			loopdate.add(Calendar.DATE, -1);
		}

		// Date today = new Date();
		// Date recent = today;

		for (int i = 0; i <= 100; i++) {
			System.out.println(i + "B: " + RSIReverse.getPriceByRSIBinary(datedprice, magnitude, i));
			System.out.println(i + "L: " + RSIReverse.getPriceByRSI(datedprice, magnitude, i));
		}
		// System.out.println(50 + ": " +
		// RSIReverse.getPriceByRSIBinary(datedprice, magnitude, 50));

	}

	// protected static double getPriceByRSI(Map<Date, Double> datedPriceInput,
	// int magnitude, double targetedRsi,
	// int maxTrial) throws Exception {
	// Map<Date, Double> datedPrice = new TreeMap<>(datedPriceInput);
	//
	// Date today = new Date();
	// Date recent = today;
	// double max = 0;
	// double min = 999999;
	// for (double value : datedPrice.values()) {
	// if (value > max) {
	// max = value;
	// }
	// if (value < min) {
	// min = value;
	// }
	// }
	// double start = 0;
	// double increment = (max - min) / maxTrial;
	//
	// while (start >= 0) {
	// datedPrice.put(today, start);
	// RSI rsi = new RSI(datedPrice, recent, magnitude);
	// Double actualRsi = rsi.getLine()
	// .get(recent);
	// if (actualRsi >= targetedRsi) {
	// return start;
	// }
	// start = start + increment;
	// if (start >= max * 100) {
	// return -1;
	// }
	// }
	// return -1;
	// }

}
