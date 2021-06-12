package com.algotrading.backtesting.indicatorcalculator.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.algotrading.backtesting.indicatorcalculator.RSI;

public class RSITest {

	@Test
	public void test001_RSI() throws Exception {
		// Test case setting begins
		// Jan is 0, Feb is 1, etc.
		Calendar loopdate = new GregorianCalendar(2011, 1, 1);
		int map_length = 30;
		Date recent = loopdate.getTime();
		double expected_value = 37.231382; // expected value of recent
											// date
		int magnitude = 14;
		int sma_magnitude = 0;

//		Double[] series = new Double[] { 97.35, 95.8, 96.4, 95.95, 95.0, 95.5, 96.9, 96.2, 95.9, 95.8, 97.95, 96.1,
//				96.7, 95.0, 94.0, 92.55, 93.6, 92.55, 93.0, 92.6 }; // the
//																	// series

		// descending order
		Double[] series = new Double[] { 55.129017, 53.812099, 55.083599, 54.856556, 55.129017, 56.99086, 57.808289,
				59.624718, 57.853668, 58.398609, 60.078819, 61.441151, 60.987038, 60.669167, 60.396698, 60.215054,
				60.351299, 61.032452, 60.89621, 61.486557, 61.077858, 59.443077, 59.851765, 60.124229, 58.943546,
				59.715527, 59.715527, 58.444031, 57.808289, 57.081688 };

		Map<Date, Double> datedprice = new TreeMap<Date, Double>();
		for (int i = 0; i < map_length; i++) {
//			System.out.println(loopdate.getTime());
			datedprice.put(loopdate.getTime(), series[i]);
			loopdate.add(Calendar.DATE, -1);
		}

		// print the input line
		System.out.println("Input");
		for (Map.Entry<Date, Double> entry : datedprice.entrySet()) {
			System.out.println(entry.getKey().toString() + "/" + entry.getValue());
		}
		System.out.println("before construct RSI");
		RSI rsi = new RSI(datedprice, recent, magnitude);
		System.out.println("after construct RSI");
		// Test case setting ends

		// print the resulting line from rsi
		System.out.println("Result");
		Map<Date, Double> result_line = rsi.getLine();
		for (Map.Entry<Date, Double> entry : result_line.entrySet()) {
			System.out.println(entry.getKey().toString() + "/" + entry.getValue());
		}

		// print the expecting line and compare
		System.out.println("Expected");
		Map<Date, Double> expected_line = new TreeMap<Date, Double>();
		loopdate = new GregorianCalendar(2011, 1, 1);
		/*
		 * Double[] rsi_points = new Double[] { 0.0, 3.950617284, 7.901234568,
		 * 11.85185185, 15.80246914, 19.75308642, 29.62962963, 44.44444444, 66.66666667,
		 * 100.0, 100.0, 100.0 }; // this // is // the // expecting // line
		 */
//		Double[] rsi_points = new Double[] { 72.83950617, 35.8974358974359, 52.5, 50.6493506493506, 36.6666666666668,
//				23.6559139784947, 57.843137254902, 45.0, 57.03125, 62.3287671232876, 90.9090909090908, 71.551724137931,
//				83.2000000000001, 70.0, 65.909090909091 }; // this
//		// is
//		// the
//		// expecting
//		// line

		// descending order
		Double[] rsi_points = new Double[] { 37.231382, 28.830813, 32.761856, 31.20655, 32.0322, 38.494514, 41.944365,
				51.460478, 38.912882, 42.016087, 54.447985, 70.053731, 67.138427, 64.918486, 62.924977, 61.573444,
				63.177366, 71.870545, 71.132917, 79.524081, 77.843891, 68.13024, 75.849655, 81.571413, 73.539972, 100.0,
				100.0, 100.0, 100.0 };

//		for (int i = 0; i < map_length - magnitude - sma_magnitude + 1; i++) {
//			expected_line.put(loopdate.getTime(), rsi_points[i]);
//			System.out.println(loopdate.getTime().toString() + "/" + expected_line.get(loopdate.getTime()));
//			assertEquals(result_line.get(loopdate.getTime()), rsi_points[i], 0.0001); // comparing
//																						// each
//																						// element
//			loopdate.add(Calendar.DATE, -1);
//
//		}

		// for smma
		System.out.println(map_length);
		for (int i = 0; i < map_length - 1; i++) {
			expected_line.put(loopdate.getTime(), rsi_points[i]);
			System.out.println(i + "/" + loopdate.getTime().toString() + "/" + expected_line.get(loopdate.getTime()));
//			assertEquals(result_line.get(loopdate.getTime()), rsi_points[i], 0.0001); // comparing
			// each
			// element
			loopdate.add(Calendar.DATE, -1);

		}

		assertEquals(rsi.getValue(), expected_value, 0.0001);
	}

}
