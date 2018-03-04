package com.algotrading.interactivebroker.util;

import java.util.Date;

public class Logger {

	public void info(String info) {
		System.out.println(new Date() + " : " + info);
	}

}
