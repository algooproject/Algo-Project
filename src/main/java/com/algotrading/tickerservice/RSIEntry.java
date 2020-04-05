package com.algotrading.tickerservice;

public class RSIEntry {

	public String ticker;
	public int magnitude;
	public String date;
	public double value;

	@Override
	public String toString() {
		return "RSIEntry{" + "ticker='" + ticker + '\'' + ", magnitude=" + magnitude + ", date='" + date + '\''
				+ ", value=" + value + '}';
	}

}
