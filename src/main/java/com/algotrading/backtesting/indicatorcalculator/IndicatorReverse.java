package com.algotrading.backtesting.indicatorcalculator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class IndicatorReverse {

	public static double getPriceByRSI(Map<Date, Double> datedPriceInput, int magnitude, double targetedRsi)
			throws Exception {
		return getPriceByRSI(datedPriceInput, magnitude, targetedRsi, 1000);
	}

	public static double getPriceByRSI(Map<Date, Double> datedPriceInput, int magnitude, double targetedRsi,
			int maxTrial) throws Exception {
		Map<Date, Double> datedPrice = new TreeMap<>(datedPriceInput);

		Date today = new Date();
		Date recent = today;
		double max = 0;
		double min = 999999;
		for (double value : datedPrice.values()) {
			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}
		}
		double start = 0;
		double increment = (max - min) / maxTrial;

		while (start >= 0) {
			datedPrice.put(today, start);
			RSI rsi = new RSI(datedPrice, recent, magnitude);
			Double actualRsi = rsi.getLine()
					.get(recent);
			if (actualRsi >= targetedRsi) {
				return start;
			}
			start = start + increment;
			if (start >= max * 100) {
				return -1;
			}
		}
		return -1;
	}

	public static double getPriceByRSIBinary(Map<Date, Double> datedPriceInput, int magnitude, double targetedRsi)
			throws Exception {
		Map<Date, Double> datedPrice = new TreeMap<>(datedPriceInput);

		Date today = new Date();
		Date recent = today;
		double max = 0;
		double min = 999999;
		for (double value : datedPrice.values()) {
			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}
		}
		double error = (max - min) / 10000;
		double start = (max + min) / 2;
		double low = 0;
		double high = -1;
		int i = 0;
		while (i < 1000) {

			datedPrice.put(today, start);
			RSI rsi = new RSI(datedPrice, recent, magnitude);
			Double actualRsi = rsi.getLine()
					.get(recent);
			if (Math.abs(actualRsi - targetedRsi) < error) {
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " + i + " low:"
						+ low + " high:" + high + " start:" + start + " actual:" + actualRsi + " target:"
						+ targetedRsi);
				return start;
			}
			if (actualRsi < targetedRsi) {
				if (high == -1) {
					start = start * 1.5;
				} else {
					low = start;
					start = (high + low) / 2;
				}
			}
			if (actualRsi > targetedRsi) {
				high = start;
				start = (high + low) / 2;
			}
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " + i + " low:" + low
					+ " high:" + high + " start:" + start + " actual:" + actualRsi + " target:" + targetedRsi);
			i++;
		}
		return -1;
	}

	///

	public static double getPriceBySMA(Map<Date, Double> datedPriceInput, int magnitude, double targetedSma)
			throws Exception {
		return getPriceBySMA(datedPriceInput, magnitude, targetedSma, 1000);
	}

	public static double getPriceBySMA(Map<Date, Double> datedPriceInput, int magnitude, double targetedSma,
			int maxTrial) throws Exception {
		Map<Date, Double> datedPrice = new TreeMap<>(datedPriceInput);

		Date today = new Date();
		Date recent = today;
		double max = 0;
		double min = 999999;
		for (double value : datedPrice.values()) {
			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}
		}
		double start = 0;
		double increment = (max - min) / maxTrial;

		while (start >= 0) {
			datedPrice.put(today, start);
			SMA sma = new SMA(datedPrice, recent, magnitude);
			Double actualSma = sma.getLine()
					.get(recent);
			if (actualSma >= targetedSma) {
				return start;
			}
			start = start + increment;
			if (start >= max * 100) {
				return -1;
			}
		}
		return -1;
	}

	public static double getPriceBySMABinary(Map<Date, Double> datedPriceInput, int magnitude, double targetedSma)
			throws Exception {
		Map<Date, Double> datedPrice = new TreeMap<>(datedPriceInput);

		Date today = new Date();
		Date recent = today;
		double max = 0;
		double min = 999999;
		for (double value : datedPrice.values()) {
			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}
		}
		double error = (max - min) / 10000;
		double start = (max + min) / 2;
		double low = 0;
		double high = -1;
		int i = 0;
		while (i < 1000) {

			datedPrice.put(today, start);
			SMA sma = new SMA(datedPrice, recent, magnitude);
			Double actualSma = sma.getLine()
					.get(recent);
			if (Math.abs(actualSma - targetedSma) < error) {
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " + i + " low:"
						+ low + " high:" + high + " start:" + start + " actual:" + actualSma + " target:"
						+ targetedSma);
				return start;
			}
			if (actualSma < targetedSma) {
				if (high == -1) {
					start = start * 1.5;
				} else {
					low = start;
					start = (high + low) / 2;
				}
			}
			if (actualSma > targetedSma) {
				high = start;
				start = (high + low) / 2;
			}
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " + i + " low:" + low
					+ " high:" + high + " start:" + start + " actual:" + actualSma + " target:" + targetedSma);
			i++;
		}
		return -1;
	}
}
