package com.algotrading.backtesting.indicatorcalculator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RSI implements IRsiCalculator {

	/** the stock under inspection */
	private Map<Date, Double> datedPrice;

	/** the recent date of the indicator */
	private Date recent;

	/** E.g. 50 points of a 10-day MA. Then magnitude = 10, linelen = 50 */
	private int magnitude;
	// private int smaMagnitude;

	/** the EMA of the recent date */
	private double value;

	/** the EMA line, with the last element the last SMA */
	private Map<Date, Double> line;

	/** the positive increment of date price */
	private Map<Date, Double> plus;

	/** the negative increment of dated price */
	private Map<Date, Double> minus;

	private double alpha;

	// private EMA emaPlus;

	// private EMA emaMinus;

	// public RSI(Map<Date, Double> datedPrice, Date recent, int magnitude, int
	// smaMagnitude) throws Exception {
	public RSI(Map<Date, Double> datedPrice, Date recent, int magnitude) throws Exception {
		// System.out.println(datedPrice);
		// the stock
		this.datedPrice = datedPrice;

		// the most recent day
		this.recent = recent;

		// equivalent to N in RSI.pdf
		this.magnitude = magnitude;

		// equivalent to a in RSI.pdf
		// this.smaMagnitude = smaMagnitude;
		// this.smaMagnitude = 0;

		if (!readyToCalculate(this.datedPrice, this.magnitude)) {
			// if (!readyToCalculate(this.datedPrice, this.magnitude,
			// this.smaMagnitude)) {
			throw new Exception("SMA Instantiation failed!");
		}

		// extracting the positive and negative increment
		// get the positive and negative increment in a list
		List<Map<Date, Double>> plusMinus = differentiate(datedPrice);

		// get the positive part
		this.plus = plusMinus.get(0);

		// get the negative part
		this.minus = plusMinus.get(1);

		// create EMA by the positive part
		// this.emaPlus = new EMA(this.plus, recent, magnitude, smaMagnitude);

		// create EMA by the negative part
		// this.emaMinus = new EMA(this.minus, recent, magnitude, smaMagnitude);

		// get the RSI
		// this.line = calLine(this.emaPlus.getLine(), this.emaMinus.getLine());
		// this.line = calLine(sumPlusOrMinus(this.plus, magnitude),
		// sumPlusOrMinus(this.minus, magnitude));
		this.line = calLine(sumPlusOrMinus(plus, magnitude), sumPlusOrMinus(minus, magnitude));
		this.line = calculate(datedPrice, this.magnitude);
		// this.line = calculate(datedPrice, this.magnitude, this.smaMagnitude);

		// check if recent is a date in line
		if (!this.line.containsKey(recent)) {
			// if not, use the most recent date available
			Map.Entry<Date, Double> entry = this.line.entrySet().iterator().next();
			this.recent = entry.getKey();
			this.value = entry.getValue();
		} else {
			this.value = this.line.get(this.recent);
		}

	}

	public void setRecent(Date recent) {
		if (this.line.containsKey(recent)) {
			this.recent = recent;
			this.value = this.line.get(recent);
		} else {
			// System.out.println("rsi recent: " + recent);
			// System.out.println("RSI No such date ("+recent+") in record");
		}
	}

	/**
	 * setting the alpha used in those EMA's
	 * 
	 * @throws Exception
	 */
	public void setAlpha(double alpha) throws Exception {
		if (this.alpha != alpha) {
			this.alpha = alpha;
			// this.emaPlus.setAlpha(alpha);
			// this.emaMinus.setAlpha(alpha);
			// this.line = calLine(this.emaPlus.getLine(),
			// this.emaMinus.getLine());
			this.line = calculate(datedPrice, this.magnitude);
			// this.line = calculate(datedPrice, this.magnitude,
			// this.smaMagnitude);
			this.value = this.line.get(this.recent);
		}
	}

	/** setting the default alpha used in those EMA's (2/N) */
	/*
	 * public void setAutoAlpha() { if (this.alpha !=
	 * this.emaPlus.getAutoAlpha()) { this.alpha = this.emaPlus.getAutoAlpha();
	 * this.emaPlus.setAlpha(alpha); this.emaMinus.setAlpha(alpha); this.line =
	 * calLine(this.emaPlus.getLine(), this.emaMinus.getLine()); this.value =
	 * this.line.get(this.recent); } }
	 */

	public Date getRecent() {
		return this.recent;
	}

	public int getMagnitude() {
		return this.magnitude;
	}

	public double getValue() {
		return this.value;
	}

	public Map<Date, Double> getLine() {
		return this.line;
	}

	public double getAlpha() {
		return this.alpha;
	}

	/** check whether stock is sufficient for calculation */
	private boolean readyToCalculate(Map<Date, Double> datedprice, int magnitude) {

		// private boolean readyToCalculate(Map<Date, Double> datedprice, int
		// magnitude, int tail_magnitude) {
		if (datedprice == null || magnitude < 1) {
			// System.out.println("Initialization of variables not completed
			// yet! Cannot proceed!");
			return false;
		}

		// check if the date is sufficient
		// if (datedprice.size() < magnitude + tail_magnitude - 1) {
		if (datedprice.size() < magnitude - 1) {
			// System.out.println("The length of line is out of range!");
			return false;
		}
		return true;
	}

	/** the function which extracts the positive and negative part */
	public List<Map<Date, Double>> differentiate(Map<Date, Double> datedprice) {
		Map<Date, Double> plus = new TreeMap<>();
		Map<Date, Double> minus = new TreeMap<>();
		List<Date> dates = new ArrayList<>();
		for (Map.Entry<Date, Double> entry : datedprice.entrySet()) {
			dates.add(entry.getKey());
		}
		double difference;
		for (int i = dates.size() - 1; i >= 1; i--) {
			difference = datedprice.get(dates.get(i)) - datedprice.get(dates.get(i - 1));
			// System.out.println(datedprice.get(dates.get(i)) + " " +
			// datedprice.get(dates.get(i - 1)));
			if (difference >= 0) {
				plus.put(dates.get(i), difference);
				minus.put(dates.get(i), 0.0);
				// System.out.println(dates.get(i).toString() + " " + difference
				// + " 0");
			} else {
				minus.put(dates.get(i), -difference);
				plus.put(dates.get(i), 0.0);
				// System.out.println(dates.get(i).toString() + " 0" + " " +
				// -difference);
			}
		}
		List<Map<Date, Double>> plus_minus = new ArrayList<>();
		plus_minus.add(plus);
		plus_minus.add(minus);
		return plus_minus;
	}

	/** sum up the positive or negative increment */
	private Map<Date, Double> sumPlusOrMinus(Map<Date, Double> plusOrMinus, int magnitude) {
		List<Date> dates = new ArrayList<Date>();
		int pointer = magnitude - 1;
		// System.out.println("Initialization of EMA");

		for (Map.Entry<Date, Double> entry : plusOrMinus.entrySet()) {
			dates.add(entry.getKey());
			// System.out.println(entry.getKey().toString() + "/" +
			// entry.getValue());
			// System.out.println(entry.getKey().toString());
			if (pointer >= magnitude - 1 && pointer < plusOrMinus.size()) {
				if (plusOrMinus.get(entry.getKey()) == null) {
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd");
					// System.out.println("Insufficient boundary: " +
					// dateformat.format(entry.getKey()) + " is missing! ");
					return Collections.<Date, Double>emptyMap();
				}
				pointer++;
			}
		}
		Map<Date, Double> line = new TreeMap<>();
		double value = 0;
		// System.out.println("alpha = " + alpha);

		// for (int i = 0; i < magnitude - 1; i++) {
		// System.out.println("dates.size()" + dates.size());
		// System.out.println("magnitude" + magnitude);

		for (int i = dates.size() - 1; i >= magnitude - 1; i--) {
			value = 0;
			for (int j = i; j >= i - magnitude + 1; j--) {
				value += plusOrMinus.get(dates.get(j));
			}
			line.put(dates.get(i), value / magnitude);
			// System.out.println(dates.get(i).toString() + ": " + value /
			// magnitude);
		}
		// System.out.println("***************************************");

		// for (int i = magnitude; i < dates.size(); i++) {
		/*
		 * for (int i = dates.size() - magnitude - 1; i >= magnitude - 1; i--) {
		 * pointer -= 1;
		 * System.out.println("***************************************");
		 * System.out.println(dates);
		 * System.out.println("***************************************");
		 * System.out.println(pointer);
		 * System.out.println("***************************************");
		 * line.put(dates.get(pointer), value); }
		 */
		return line;
	}

	/** get the line with the positive and negative increment */
	private Map<Date, Double> calLine(Map<Date, Double> plus, Map<Date, Double> minus) {
		Map<Date, Double> line = new TreeMap<>();
		Date date;
		for (Map.Entry<Date, Double> entry : plus.entrySet()) {
			date = entry.getKey();
			if (minus.get(date) == 0) {
				line.put(date, 100.0);
			} else {
				// System.out.println("date:" + date.toString());
				// System.out.println("plus.get(" + date.toString() + "):" +
				// plus.get(date));
				// System.out.println("minus.get(" + date.toString() + "):" +
				// minus.get(date));

				line.put(date, 100 - 100 / (1 + plus.get(date) / minus.get(date)));
				double test = 100 - 100 / (1 + plus.get(date) / minus.get(date));
				// System.out.println("100 - 100 / (1 + plus.get(date) /
				// minus.get(date)):" + test);
				// System.out.println(date.toString() + "RSI: " + test + ".
				// magnitude: " + magnitude);
			}
		}
		// this.value = line.get(this.recent);
		return line;
	}

	/** get RSI, open to outside calls */
	@Override
	// public Map<Date, Double> calculate(Map<Date, Double> datedprice, int
	// magnitude, int sma_magnitude)
	// throws Exception {

	public Map<Date, Double> calculate(Map<Date, Double> datedprice, int magnitude) throws Exception {
		Map<Date, Double> line = new TreeMap<>();

		if (!readyToCalculate(datedprice, magnitude)) {
			// if (!readyToCalculate(datedprice, magnitude, sma_magnitude)) {
			return Collections.<Date, Double>emptyMap();
		}
		Map.Entry<Date, Double> entry = this.line.entrySet().iterator().next();
		// Date recent = entry.getKey();
		List<Map<Date, Double>> plus_minus = differentiate(datedprice);
		Map<Date, Double> plus = plus_minus.get(0);
		// System.out.println("plus");
		// System.out.println(plus);

		Map<Date, Double> minus = plus_minus.get(1);
		// System.out.println("minus");
		// System.out.println(minus);
		// EMA ema_plus = new EMA(plus, recent, magnitude, sma_magnitude);
		// EMA ema_minus = new EMA(minus, recent, magnitude, sma_magnitude);
		// line = calLine(ema_plus.getLine(), ema_minus.getLine());
		line = calLine(sumPlusOrMinus(plus, magnitude), sumPlusOrMinus(minus, magnitude));
		return line;
	}
}
